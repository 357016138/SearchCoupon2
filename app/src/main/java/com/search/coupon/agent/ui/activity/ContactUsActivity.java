package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("联系我们");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    @OnClick({R.id.tv_qq1,R.id.tv_qq2})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_qq1:                  //客服1
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=3517208338";//uin是发送过去的qq号码
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    toast( "请检查是否安装QQ");
                }
                break;
            case R.id.tv_qq2:                //客服2
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=2595802618";//uin是发送过去的qq号码
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    toast( "请检查是否安装QQ");
                }
                break;

            default:
                break;

        }
    }



    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

}


