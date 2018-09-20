package com.search.coupon.agent.ui.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.utils.DeviceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.tv_go_next)
    TextView tv_go_next;
    @BindView(R.id.iv_indicator)
    ImageView iv_indicator;
    private int mGuidePage[] = {
            R.drawable.guide_page0,
            R.drawable.guide_page1,
            R.drawable.guide_page2,
    };
    private int mIndicator[] = {
            R.drawable.icon_indicator1,
            R.drawable.icon_indicator2,
            R.drawable.icon_indicator3,
    };
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_guide, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        ImgAdapter imgAdapter = new ImgAdapter();
        vpGuide.setAdapter(imgAdapter);

    }

    @Override
    public void dealLogicAfterInitView() {
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                iv_indicator.setImageResource(mIndicator[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.tv_go_next})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_go_next:
                goPage(MainActivity.class);
                ShareData.setShareStringData(ShareData.IS_OPEN_GUIDE, DeviceUtils.getCurrentAppVersionName(GuideActivity.this));
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

    class ImgAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGuidePage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mGuidePage[position]);
            container.addView(imageView);
            return imageView;
//            RelativeLayout viewRoot = new RelativeLayout(GuideActivity.this);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            viewRoot.setLayoutParams(params);
//
//            ImageView imageView = new ImageView(GuideActivity.this);
//            ViewGroup.LayoutParams imgParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(imgParams);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setImageResource(mGuidePage[position]);
//
//            TextView textView = new TextView(GuideActivity.this);
//            textView.setText("立即体验");
//            textView.setTextColor(getResources().getColor(R.color.color_3889FF));
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            textView.setBackgroundResource(R.drawable.shape_btn_blue);
//            textView.setPadding(DensityUtil.dip2px(GuideActivity.this, 50),
//                    DensityUtil.dip2px(GuideActivity.this, 10),
//                    DensityUtil.dip2px(GuideActivity.this, 50),
//                    DensityUtil.dip2px(GuideActivity.this, 10));
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//            lp.setMargins(0, 0, 0, DensityUtil.dip2px(GuideActivity.this, 50));
//            textView.setLayoutParams(lp);
//            textView.setOnClickListener(v -> {
//                goPage(MainActivity.class);
//                ShareData.setShareStringData(ShareData.IS_OPEN_GUIDE, DeviceUtils.getCurrentAppVersionName(GuideActivity.this));
//            });
//            viewRoot.addView(imageView);
//            viewRoot.addView(textView);
//            container.addView(viewRoot);
//            return viewRoot;
        }
    }
}
