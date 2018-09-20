package com.search.coupon.agent.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseApplication;
import com.search.coupon.agent.listener.WebViewLoadFinishListener;
import com.search.coupon.agent.utils.GsonUtil;
import com.search.coupon.agent.utils.LogUtils;
import com.search.coupon.agent.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2018/1/30 0030.
 * 自定义通用的WebView  与H5交互
 */

public class CustomWebView extends FrameLayout implements View.OnClickListener {
    //加载成功
    private final int TYPE_LOAD_SUCCESS = 1;
    //加载失败
    private final int TYPE_LOAD_FAIL = 2;
    //加载中
    private final int TYPE_LOADING = 3;
    //加载是否失败：用于加载完成后的状态显示
    private boolean isError;
    //是否是第一次加载
    private boolean isFirst = true;

    private ProgressWebView mWebView;
    private RelativeLayout mErrorLayout;
    private TextView mTvError;
    private LinearLayout ltLoaing;
    private List<String> titles = new ArrayList<String>();
    private String app2WebData;
    private Context context;
    private WebViewLoadFinishListener pageFinishListener;

    private TopBar topBar;

    public CustomWebView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_webview, this, true);
        mWebView = view.findViewById(R.id.mWebView);
        mErrorLayout = view.findViewById(R.id.mErrorLayout);
        mErrorLayout.setOnClickListener(this);
        mTvError = view.findViewById(R.id.mTvError);
        ltLoaing = view.findViewById(R.id.lt_loading);
        initWebview();
    }

    public void setWebViewLoadFinishListener(WebViewLoadFinishListener webViewLoadFinishListener) {
        this.pageFinishListener = webViewLoadFinishListener;
    }

    private void initWebview() {
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.requestFocusFromTouch();
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings settings = mWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSaveFormData(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setTextZoom(100);

        settings.setUserAgentString(settings.getUserAgentString() + "/" + GsonUtil.toJson(NetUtils.getRequestHeader(context)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mErrorLayout:
                mWebView.reload();
                break;
        }
    }

    public class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("JY", "shouldOverrideUrlLoading---" + url);
            try {
                Log.d("UMHybrid", "shouldOverrideUrlLoading url:" + url);
                String decodedURL = java.net.URLDecoder.decode(url, "UTF-8");
//                UMHybrid.getInstance(context).execute(decodedURL, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(url) && url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                getContext().startActivity(intent);
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String url = request.getUrl().toString();
                try {
                    Log.d("UMHybrid", "shouldOverrideUrlLoading url:" + url);
                    String decodedURL = java.net.URLDecoder.decode(url, "UTF-8");
//                    UMHybrid.getInstance(context).execute(decodedURL, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(url) && url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    getContext().startActivity(intent);
                    return true;
                }
                return false;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        //加载开始
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.e("JY", "onPageStarted---" + url);
            super.onPageStarted(view, url, favicon);
            if (isError) {
                isError = false;
            }
            if (isFirst) {
                setWebUi(TYPE_LOADING);
            }
        }

        //加载结束
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isError) {
                setWebUi(TYPE_LOAD_FAIL);
            } else {
                setWebUi(TYPE_LOAD_SUCCESS);
                if (pageFinishListener != null) {
                    pageFinishListener.onPageLoadFinish();
                }
            }
            view.loadUrl("javascript:setWebViewFlag()");
            if (!TextUtils.isEmpty(url)) {
//                MobclickAgent.onPageStart(url);
            }
        }

        //加载出错
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.getUrl().toString().equals(view.getUrl())) {
                isError = true;
                setWebUi(TYPE_LOAD_FAIL);
            }
        }

        //加载出错
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            isError = true;
            setWebUi(TYPE_LOAD_FAIL);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        }

    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            ProgressBar progressbar = mWebView.getProgressbar();
            progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5, 0, 0));
            progressbar.setIndeterminate(false);
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)
                    && !title.contains("blank")
                    && !title.contains("172.")
                    && !title.contains("app.")
                    && !title.contains("HLPlatform")
                    && !title.contains("http")) {
                titles.add(title);
                if (topBar != null) {
                    topBar.setTitle(title);
                }
            }
        }
    }

    //根据加载完成后的状态显示不同的页面
    private void setWebUi(int loadStatus) {
        switch (loadStatus) {
            case TYPE_LOADING:
                ltLoaing.setVisibility(VISIBLE);
                mWebView.setVisibility(GONE);
                mErrorLayout.setVisibility(GONE);
                break;
            case TYPE_LOAD_SUCCESS:
                isFirst = false;
                ltLoaing.setVisibility(GONE);
                mWebView.setVisibility(VISIBLE);
                mErrorLayout.setVisibility(GONE);
                break;
            case TYPE_LOAD_FAIL:
                ltLoaing.setVisibility(GONE);
                mWebView.setVisibility(GONE);
                mErrorLayout.setVisibility(VISIBLE);
                break;
        }
    }

    /**
     * Sync Cookie
     */
    public void syncCookie(Context context, String url) {
        try {
            LogUtils.e("Nat: webView.syncCookie.url", url);
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                LogUtils.e("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", "INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));
            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                LogUtils.e("Nat: webView.syncCookie.newCookie", newCookie);
            }
        } catch (Exception e) {
            LogUtils.e("Nat: webView.syncCookie failed", e.toString());
        }
    }

    public void synCookies(String url, String cookie) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CookieSyncManager.createInstance(BaseApplication.getApplication());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }

    public void removeCookie() {
        CookieSyncManager.createInstance(BaseApplication.getApplication());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

//    public void setJsDefault() {
//        mWebView.addJavascriptInterface(new JSNotify(), "xiangshang");
//    }

    public ProgressWebView getmWebView() {
        return mWebView;
    }

    public List<String> getTitles() {
        return titles;
    }

    public RelativeLayout getmErrorLayout() {
        return mErrorLayout;
    }

    public TextView getmTvError() {
        return mTvError;
    }

    public void setTopBar(TopBar topBar) {
        this.topBar = topBar;
    }

    public String getApp2WebData() {
        return app2WebData;
    }

    public void setApp2WebData(String app2WebData) {
        this.app2WebData = app2WebData;
    }
}
