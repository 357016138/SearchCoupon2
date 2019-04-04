package com.search.coupon.agent.ui.activity;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.view.CustomWebView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by song on 2018/1/17 0017.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.wv_common)
    CustomWebView wvCommon;


    /**
     * 记录点击返回时间
     **/
    private long exitTime = 0;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main,BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

   
    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);



    }

    @Override
    public void dealLogicAfterInitView() {
        wvCommon.getmWebView().loadUrl("http://tao.vxsousuo.com/index.php?r=index/wap");

    }

    @Override
    public void OnTopLeftClick() {

    }

    @Override
    public void OnTopRightClick() {

    }


    @OnClick({R.id.floatingActionButton})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                openTaobao(1000);
                break;
            default:
                break;

        }
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    private static boolean checkPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    /**
     * @param reqCode
     */
    private void openTaobao(int reqCode) {

        if (checkPackage("com.taobao.taobao")) {
            //
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("taobao://shop.m.taobao.com");
            intent.setData(uri);
            //跳转
            startActivityForResult(intent, reqCode);

        }
    }



    /**
     * 退出程序，防止用户误退
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast("再按一次，退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                app.exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
