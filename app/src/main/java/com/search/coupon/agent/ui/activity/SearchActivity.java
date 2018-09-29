package com.search.coupon.agent.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.SearchAdapter;
import com.search.coupon.agent.bean.SearchBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.LogUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.search.coupon.agent.utils.TaoBaoConstants;
import com.search.coupon.agent.utils.TaoBaoKeUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.search.coupon.agent.common.BaseActivity.BasePageSet.NO_TOPBAR_DEFAULT_PAGE;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener,OperateListener {
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.no_data_refreshLayout)
    SmartRefreshLayout no_data_refreshLayout;
    @BindView(R.id.fragmentBill_refreshLayout)
    SmartRefreshLayout fragmentBill_refreshLayout;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;
    private SearchAdapter adapter;

    private int pageNum = 1;            // 当前页码
    private final int pageSize = 15;   // 每页条数
    private String content;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_search,NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {
        content = getIntentData().getString("content");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        et_search.setOnEditorActionListener(this);   //初始化监听

        //recyclerview 布局设置start
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
        //recyclerview 布局设置end

        adapter = new SearchAdapter(this, 0);
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);

        //禁用下拉刷新
        no_data_refreshLayout.setEnableRefresh(false);
        fragmentBill_refreshLayout.setEnableRefresh(false);
        /**
         *上拉加载更多
         * */
        fragmentBill_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;

                String mKeyWord = et_search.getText().toString().replace(" ","");
                if(!StringUtils.isEmpty(mKeyWord)) {
                    getSearchList(mKeyWord);
                }

            }
        });
    }

    @Override
    public void dealLogicAfterInitView() {
        //弹出软键盘
//        DeviceUtils.showSoftInputFromWindow(this,et_search);

        getSearchList(content);
    }

    @OnClick({R.id.iv_search,R.id.iv_back})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                getSearchContent(true);
                break;
            case R.id.iv_back:
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

    private void getSearchContent(boolean flag) {

        if (flag){
            String mKeyWord = et_search.getText().toString().replace(" ","");
            if(StringUtils.isEmpty(mKeyWord)) {
                toast("请输入搜索内容");
                return;
            }

            getSearchList(mKeyWord);
            rl_search.setFocusable(true);
            rl_search.setFocusableInTouchMode(true);
        }

    }

    /***
     * 搜索信息列表
     * */
    private void getSearchList(String mKeyWord) {

        RequestParams requestParams = new RequestParams(Constants.TBK_ITEM_INFO);
        requestParams.params.putAll(TaoBaoKeUtil.getCommonParams(Constants.TBK_ITEM_INFO_METHOD));
        requestParams.add("num_iids","576461962321");
        requestParams.add("platform", TaoBaoConstants.PLATFORM);
        String sign = TaoBaoKeUtil.signTopRequest(requestParams.params, TaoBaoConstants.SECRET);
        requestParams.params.put("sign",sign);

        startRequest(Task.GET_SEARCH_LIST, requestParams, null);

    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_SEARCH_LIST:
                fragmentBill_refreshLayout.finishRefresh();
                fragmentBill_refreshLayout.finishLoadmore();
                no_data_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {

//                    SearchBean searchBean = (SearchBean) data.getBody();
//                    //------------------数据异常情况-------------------
//                    if (searchBean == null || searchBean.getGroups() == null || searchBean.getGroups().size() <= 0) {
//                        if (pageNum == 1) {
//                            showNodata();
//                        }
//                        return;
//                    }
//                    //-----------------数据正常情况--------------------
//                    List<SearchBean.ProductBean> dataListProm = searchBean.getGroups();
//                    if (pageNum == 1) {
//                        showList();
//                        adapter.setData(dataListProm);
//                    } else {
//                        adapter.getData().addAll(dataListProm);
//                    }
//                    //如果返回数据不够10条，就不能继续上拉加载更多
//                    fragmentBill_refreshLayout.setEnableLoadmore(dataListProm.size() >= pageSize);
//                    adapter.notifyDataSetChanged();

                } else {
                    if (pageNum == 1) {
                        showNodata();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showNodata() {
        no_data_refreshLayout.setVisibility(View.VISIBLE);
        fragmentBill_refreshLayout.setVisibility(View.GONE);
    }

    private void showList() {
        no_data_refreshLayout.setVisibility(View.GONE);
        fragmentBill_refreshLayout.setVisibility(View.VISIBLE);
    }

    //EditText的搜索键监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            getSearchContent(true);
            return true;
        }
        return false;
    }

    /**
     * 列表布局的回调
     * */
    @Override
    public void operate(String operateType, Object str) {
        Bundle bd = new Bundle();
        String uniqueId = (String) str;
        bd.putString("uniqueId", uniqueId);
        switch (operateType) {
            case "1":                   //条目点击事件
                goPage(ProductDetailActivity.class, bd);
                break;
            default:
                break;
        }
    }
}
