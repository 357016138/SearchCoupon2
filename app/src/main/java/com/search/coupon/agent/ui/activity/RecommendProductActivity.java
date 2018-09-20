package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.RecommendProductAdapter;
import com.search.coupon.agent.bean.RecommendProductListResult;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.listener.OperatePosListener;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UserManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author baipeng
 * @Title RecommendProductActivity
 * @Date 2018/2/26 15:55
 * @Description 推荐产品.
 */
public class RecommendProductActivity extends BaseActivity implements OperatePosListener {
    @BindView(R.id.lt_no_data)
    LinearLayout lt_no_data;
    @BindView(R.id.recyclerview_product)
    RecyclerView productRecyclerview;
    @BindView(R.id.refreshLayout_product)
    SmartRefreshLayout product_refreshLayout;
    @BindView(R.id.btn_submit_apply)
    TextView btn_submit_apply;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    private RecommendProductAdapter productAdapter;
    private ArrayList<RecommendProductListResult.ProductListBean> mProductList = new ArrayList<>();
    private String inquiryCode;
    private ArrayList<String> productCodeList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        inquiryCode = bundle.getString("inquiryCode");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("推荐产品");
        topBar.setLineVisible(true);
        setBtnState(false);

//        product_refreshLayout.autoRefresh();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        productRecyclerview.setLayoutManager(llm);

        productAdapter = new RecommendProductAdapter(this, mProductList);
        productRecyclerview.setAdapter(productAdapter);
        productAdapter.setOperateListener(this);
        productAdapter.notifyDataSetChanged();

        /**
         *刷新
         * */
        product_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                productCodeList.clear();
                getRecommendProductList();
            }
        });
        product_refreshLayout.setEnableLoadmore(false);
    }

    @Override
    public void dealLogicAfterInitView() {
        btn_submit_apply.setOnClickListener(this);
        getRecommendProductList();
    }

    @OnClick({R.id.btn_submit_apply})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_apply:
                Bundle bd = new Bundle();
                bd.putStringArrayList("productCodeList", productCodeList);
                bd.putString("inquiryCode", inquiryCode);
                goPage(ReportApplyDetailActivity.class, bd);
                break;
            default:
                break;

        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recommend_product);
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void operatePos(String operateType, int pos) {
        switch (operateType) {
            case "1":
                productCodeList.clear();
                boolean isHasCheck = false;
                for(int i = 0; i < mProductList.size(); i++) {
                    RecommendProductListResult.ProductListBean productBean = mProductList.get(i);
                    if(i == pos) {
                        if(!productBean.isCheck()) {
                            isHasCheck = true;
                            productCodeList.add(productBean.getProductCode());
                        }
                    } else if(productBean.isCheck()) {
                        isHasCheck = true;
                        productCodeList.add(productBean.getProductCode());
                    }
                }
                if(productCodeList.size() > 3) {
                    toast("最多能选择3个产品");
                    return;
                } else {
                    RecommendProductListResult.ProductListBean productData = mProductList.get(pos);
                    if(!productData.isCheck()) {
                        productData.setCheck(true);
                    } else {
                        productData.setCheck(false);
                    }
                }
                setBtnState(isHasCheck);
                productAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void setBtnState(boolean isEnable) {
        if(isEnable) {
            btn_submit_apply.setEnabled(true);
            btn_submit_apply.setBackground(getResources().getDrawable(R.drawable.bg_loan));
            ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
        } else {
            btn_submit_apply.setEnabled(false);
            btn_submit_apply.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            ll_btn.setBackground(null);
        }
    }

    private void getRecommendProductList() {
        RequestParams params = new RequestParams(UrlConfig.URL_RECOMMEND_PRODUCT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", UserManager.getUserId());
        params.add("inquiryCode", inquiryCode);
        startRequest(Task.RECOMMEND_PRODUCT, params, RecommendProductListResult.class, true);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.RECOMMEND_PRODUCT:
                if (handlerRequestErr(data)) {
                    RecommendProductListResult productListResult = (RecommendProductListResult) data.getBody();
                    mProductList.addAll(productListResult.getIntoInfoUploadList());
                    productAdapter.notifyDataSetChanged();
                }

                product_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    RecommendProductListResult productListResult = (RecommendProductListResult) data.getBody();
                    //------------------数据异常情况-------------------
                    if (productListResult == null || productListResult.getIntoInfoUploadList() == null || productListResult.getIntoInfoUploadList().size() <= 0) {
                        showNodata();
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    showList();
                    mProductList.clear();
                    mProductList.addAll(productListResult.getIntoInfoUploadList());
                    productAdapter.notifyDataSetChanged();
                } else {
                    showNodata();
                }
                break;
            default:
                break;
        }

    }

    private void showNodata() {
        lt_no_data.setVisibility(View.VISIBLE);
        product_refreshLayout.setVisibility(View.GONE);
    }

    private void showList() {
        lt_no_data.setVisibility(View.GONE);
        product_refreshLayout.setVisibility(View.VISIBLE);
    }
}
