package com.search.coupon.agent.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.BankCardListAdapter;
import com.search.coupon.agent.bean.BindBankCardInfoBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.response.BankCardListResponse;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.view.BankCardListItemDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by RickBerg on 2018/2/24 0024.
 *
 * 我的银行卡列表
 */
public class BankCardListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private BankCardListAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout mLlNetError;
    private LinearLayout mLlEmpty;
    private List<BindBankCardInfoBean> mList;
    private int mCurPage = 1;
    private final static int PAGE_SIZE = 50;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.layout_bank_card_list);
    }

    @Override
    public void dealLogicBeforeInitView() {
        EventBus.getDefault().register(this);
        mList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
    }

    @Override
    public void initView() {
        topBar.setTitle("我的银行卡");
        mRecyclerView = findViewById(R.id.rv_bank_card_list);
        mRefreshLayout = findViewById(R.id.bank_card_list_refresh_layout);
        mLlNetError = findViewById(R.id.ll_bank_card_net_error);
        mLlEmpty = findViewById(R.id.ll_bank_card_empty);
        findViewById(R.id.bt_add_bank_card).setOnClickListener(this);
        findViewById(R.id.bt_bank_card_refresh).setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {
        mAdapter = new BankCardListAdapter(this, mList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new BankCardListItemDivider(this, 10));
        mRefreshLayout.setOnRefreshListener(refreshlayout -> fetchBindBankCardList(false));
        mRefreshLayout.setOnLoadmoreListener(refreshlayout -> {
            mCurPage = 1;
            fetchBindBankCardList(false);
        });
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_add_bank_card:
//                mLlEmpty.setVisibility(View.GONE);
                goPage(BindBankCardActivity.class);
                break;
            case R.id.bt_bank_card_refresh:
                mLlNetError.setVisibility(View.GONE);
                mRefreshLayout.autoRefresh();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBindBankCardList(true);
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    private void fetchBindBankCardList(boolean showProgress) {
        RequestParams params = new RequestParams(UrlConfig.URL_QUERY_BIND_BANK_CARD_INFO);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("curPage", mCurPage);
        params.add("pageSize", PAGE_SIZE);
        startRequest(Task.BIND_BANK_CARD_INFO, params, BankCardListResponse.class, showProgress);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.BIND_BANK_CARD_INFO:
                mRefreshLayout.finishRefresh();
                if (data.getRspCode().equals("200")) {
                    BankCardListResponse response = (BankCardListResponse) data.getBody();
                    List<BindBankCardInfoBean> list = response.getList();
                    if (list != null && list.size() > 0) {
                        mLlEmpty.setVisibility(View.GONE);
                        mLlNetError.setVisibility(View.GONE);
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (mList.size() == 0) {
                            mLlEmpty.setVisibility(View.VISIBLE);
                            mLlNetError.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (mList.size() == 0) {
                        mLlNetError.setVisibility(View.VISIBLE);
                    } else {
                        handlerRequestErr(data);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getTag() == Constants.FINISTH) {
            finish();
        }
    }

}
