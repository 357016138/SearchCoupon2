package com.search.coupon.agent.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.PreferenceProductAdapter;
import com.search.coupon.agent.bean.PreferProductListResult;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.DiaLogUtils;
import com.search.coupon.agent.utils.UserManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author baipeng
 * @Title PreferenceProductActivity
 * @Date 2018/2/28 17:08
 * @Description PreferenceProductActivity.
 */
public class PreferenceProductActivity extends BaseActivity implements OperateListener {
    @BindView(R.id.lt_no_data)
    LinearLayout lt_no_data;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerview_product)
    RecyclerView productRecyclerview;
    @BindView(R.id.refreshLayout_product)
    SmartRefreshLayout product_refreshLayout;
    @BindView(R.id.btn_call)
    TextView btn_call;
    private PreferenceProductAdapter productAdapter;
    private int curPage = 1;             //当前页数
    private final int PAGESIZE = 15;//每页查询多少个
    private List<PreferProductListResult.ProductList> productList = new ArrayList<>();
    private String inquiryCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_preference_product);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        inquiryCode = bundle.getString("inquiryCode");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("优选产品");
        topBar.setLineVisible(true);

        product_refreshLayout.autoRefresh();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        productRecyclerview.setLayoutManager(llm);

        productAdapter = new PreferenceProductAdapter(this, productList);
        productRecyclerview.setAdapter(productAdapter);
        productAdapter.setOperateListener(this);
        productAdapter.notifyDataSetChanged();

        /**
         *刷新
         * */
        product_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                curPage = 1;
                getPreferProductList(curPage, PAGESIZE, inquiryCode);
            }
        });

        /**
         *加载更多
         * */
        product_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                curPage += 1;
                getPreferProductList(curPage, PAGESIZE, inquiryCode);
            }
        });
    }

    @Override
    public void dealLogicAfterInitView() {
        btn_call.setOnClickListener(this);
    }

    @OnClick({R.id.btn_call})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_call:
                callDialog();
                break;
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    public void operate(String operateType, Object bean) {
        switch (operateType) {
            case "1":
                break;
            default:
                break;
        }
    }

    public void getPreferProductList(int curPage, int pageSize, String inquiryCode) {
        RequestParams params = new RequestParams(UrlConfig.URL_PREFERENCE_PRODUCT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", UserManager.getUserId());
        params.add("curPage", curPage);
        params.add("pageSize", pageSize);
        params.add("inquiryCode", inquiryCode);
        startRequest(Task.PREFERENCE_PRODUCT, params, PreferProductListResult.class);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PREFERENCE_PRODUCT:
                product_refreshLayout.finishRefresh();
                product_refreshLayout.finishLoadmore();
                if (handlerRequestErr(data)) {
                    PreferProductListResult productListBean = (PreferProductListResult) data.getBody();
                    //------------------数据异常情况-------------------
                    if (productListBean == null || productListBean.getProductList() == null || productListBean.getProductList().size() <= 0) {
                        if (curPage == 1) {
                            showNodata();
                        }
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    if (curPage == 1) {
                        showList();
                        productList.clear();
                        productList.addAll(productListBean.getProductList());
                    } else {
                        productList.addAll(productListBean.getProductList());
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    product_refreshLayout.setEnableLoadmore(productListBean.getProductList().size() >= PAGESIZE);
                    productAdapter.notifyDataSetChanged();
                } else {
                    if (curPage == 1) {
                        showNodata();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showNodata() {
        lt_no_data.setVisibility(View.VISIBLE);
        tv_title.setText("您还没有优选产品");
        product_refreshLayout.setVisibility(View.GONE);
    }

    private void showList() {
        lt_no_data.setVisibility(View.GONE);
        product_refreshLayout.setVisibility(View.VISIBLE);
    }

    private void callDialog() {
        DiaLogUtils diaLogUtils = DiaLogUtils.creatDiaLog(this);
        diaLogUtils.setContent("10106618");
        diaLogUtils.setSureButton("呼叫", v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 10106618));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            diaLogUtils.destroyDialog();
        });
        diaLogUtils.setCancelButton("取消", v -> {
            diaLogUtils.destroyDialog();
        });
        diaLogUtils.showDialog();
    }
}
