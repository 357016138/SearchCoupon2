package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.oginotihiro.cropview.CropView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropPicActivity extends BaseActivity {
    @BindView(R.id.cropView)
    CropView cropView;

    Uri srouceUri;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crop_pic,BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {
        srouceUri = getIntent().getData();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        cropView.of(srouceUri)
                .asSquare()
                .withOutputSize(300, 300)
                .initialize(this);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    @OnClick({R.id.tv_ok,R.id.tv_cancel})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:                  //确定
                Bitmap croppedBitmap = cropView.getOutput();
                Intent intent = new Intent();
                intent.putExtra("data", croppedBitmap);
                setResult(RESULT_OK,intent);
                finish();
//                CropUtil.saveOutput(context, saveUri, croppedImage, quality);
                break;
            case R.id.tv_cancel:             //取消
                finish();
                break;
            default:
                break;
        }
    }



    @Override
    public void OnTopLeftClick() {
    }

    @Override
    public void OnTopRightClick() {

    }

}


