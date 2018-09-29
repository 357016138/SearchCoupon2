package com.search.coupon.agent.network;

import android.app.Dialog;
import android.content.Context;

import com.search.coupon.agent.R;
import com.search.coupon.agent.utils.NetUtils;
import com.search.coupon.agent.view.LoadingDialog;

import java.io.IOException;
import java.net.ConnectException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ____ Bye丶 on 2017/3/22.
 */
public class BaseHandler implements Callback {
    protected Context context;
    protected NetManager manager;
    protected ResponseListener listener;
    protected Call call;
    protected boolean isEncrypt;
    protected boolean isIntercept;
    private Dialog dialog;
    protected int tag;
    private boolean isDialog;
    private boolean cancel;

    public BaseHandler(Context context) {
        this.context = context;
        manager = new NetManager();
    }

    public Call start(int tag, RequestParams params, ResponseListener listener, boolean isDialog) {
        if (params == null) {
            return null;
        }
        this.tag = tag;
        this.isDialog = isDialog;
        this.listener = listener;
        this.isEncrypt = params.isEncrypt();
        this.isIntercept = params.isIntercept();
        if (!hasNet()) {
            return null;
        }
        //设置自定义超时时间
        if (params.getConnectTime() != -1) {
            manager.setConnectTime(params.getConnectTime());
        }
        onStart();
        switch (params.getHttpType()) {
            case GET:
                call = manager.get(context, params, this);
                break;
            case POST:
                call = manager.post(context, params, this);
                break;
            case UPLOAD:
                call = manager.upload(context, params, this);
                break;
            case POSTJSON:
                call = manager.postJson(context, params, this);
                break;
        }
        if (params.getConnectTime() != -1) {
            manager.setDefaultTime();
        }
        return call;
    }

    private boolean hasNet() {
        if (!NetUtils.isOpenNetwork(context)) {
            onFailure(call, new ConnectException(context.getString(R.string.text_net_error)));
            return false;
        }
        return true;

    }

    protected void onStart() {
        if (isDialog) {
            showDialog();
        }
    }

    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        cancel = true;
    }

    public boolean isCancel() {
        return cancel;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        call = null;

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        call = null;
    }

    protected void showDialog() {
        if (!isDialog) {
            return;
        }
        try {
            if (dialog == null) {
                dialog = new LoadingDialog(context);
                dialog.setCanceledOnTouchOutside(false);
            }
            if (dialog.isShowing()) {
                dissDialog();
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dissDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDialog() {
        return isDialog;
    }


    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
