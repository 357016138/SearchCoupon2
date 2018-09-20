package com.search.coupon.agent.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.MessageAdapter;
import com.search.coupon.agent.bean.MessageBean;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DeviceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author baipeng
 * @Title MsgListFragment
 * @Date 2018/3/6 11:31
 * @Description MsgListFragment.
 */
public class MsgListFragment extends BaseFragment implements OperateListener {
    Unbinder unbinder;
    @BindView(R.id.no_data_refreshLayout)
    SmartRefreshLayout no_data_refreshLayout;
    @BindView(R.id.fragmentBill_refreshLayout)
    SmartRefreshLayout fragmentBill_refreshLayout;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;
    private MessageAdapter adapter;

    private int pageNum = 1;            // 当前页码
    private final int PAGESIZE = 15;     // 每页条数
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_webview, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件
     * */
    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
        //recyclerview 布局设置start
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
        //recyclerview 布局设置end

        adapter = new MessageAdapter(getActivity());
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);

        /**
         * 下拉刷新
         * */
        fragmentBill_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getMessageList(pageNum+"");
            }
        });
        /**
         *上拉加载更多
         * */
        fragmentBill_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getMessageList(pageNum+"");
            }
        });
    }

    /**
     * 初始化数据
     * */
    private void initData() {
        getMessageList(pageNum+"");
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /***
     * 信息列表
     * */
    private void getMessageList(String pageNum) {
        RequestParams params = new RequestParams(UrlConfig.URL_MESSAGE_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("pageNum", pageNum);
        startRequest(Task.MESSAGE_LIST, params, new TypeToken<List<MessageBean>>(){}.getType());
    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.MESSAGE_LIST:
                fragmentBill_refreshLayout.finishRefresh();
                fragmentBill_refreshLayout.finishLoadmore();
                no_data_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    List<MessageBean> beanList = (List<MessageBean>) data.getBody();
                    //------------------数据异常情况-------------------
                    if (beanList == null||beanList.size() == 0) {
                        if (pageNum == 1) {
                            showNodata();
                        }
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    if (pageNum == 1) {
                        showList();
                        adapter.setData(beanList);
                    } else {
                        adapter.getData().addAll(beanList);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    fragmentBill_refreshLayout.setEnableLoadmore(beanList.size() >= PAGESIZE);
                    adapter.notifyDataSetChanged();

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

    /**
     * 列表布局的回调
     * */
    @Override
    public void operate(String operateType, Object str) {
//        Bundle bd = new Bundle();
//        String uniqueId = (String) str;
//        bd.putString("uniqueId", uniqueId);
//        switch (operateType) {
//            case "1":                   //条目点击事件
//                goPage(ProductDetailActivity.class, bd);
//                break;
//            default:
//                break;
//        }
    }
}
