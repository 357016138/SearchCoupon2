package com.search.coupon.agent.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.HomeAdapter;
import com.search.coupon.agent.bean.BannerBean;
import com.search.coupon.agent.bean.SearchBean;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.listener.OperateListener;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.ui.activity.MsgNoticeActivity;
import com.search.coupon.agent.ui.activity.ProductDetailActivity;
import com.search.coupon.agent.ui.activity.SearchActivity;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 首页
 * Created by song on 2018/1/30.
 */
public class HomeFragment extends BaseFragment implements OperateListener{
    Unbinder unbinder;
    @BindView(R.id.iv_msg_new)
    ImageView iv_new_msg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;
    Banner banner;
    private HomeAdapter adapter;
    private int pageNum = 1;            // 当前页码
    private final int pageSize = 15;   // 每页条数
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view,inflater);
        initData();
        return view;
    }

    /**
     * 初始化控件
     * */
    private void initView(View view,LayoutInflater inflater) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
        activity = getActivity();
        EventBus.getDefault().register(this);

        //recyclerview 布局设置start
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
        //recyclerview 布局设置end

        adapter = new HomeAdapter(activity, 0);
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);


        View header = inflater.inflate(R.layout.fragment_home_banner, fragmentBill_recyclerview, false);
        banner = header.findViewById(R.id.banner);
        adapter.setHeaderView(header);

        //设置轮播图配置
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(2000);//设置轮播时间
        banner.start();


        /**
         * 下拉刷新
         * */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getNewDataList();
            }
        });
        /**
         *上拉加载更多
         * */
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getNewDataList();
            }
        });

        /**
         * 监测RecyclerView 滑动 ，改变标题栏背景色
         * */
        fragmentBill_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //返回当前recyclerview的可见的item数目，也就是datas.length
            //dx是水平滚动的距离，dy是垂直滚动距离，向上滚动的时候为正，向下滚动的时候为负
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                 //向下滚动
                if (dy > 0) {
                    int firstVisiblesItems = llm.findFirstVisibleItemPosition();
                    if (firstVisiblesItems >= 2) {
                        rl_search.setBackgroundColor(getActivity().getResources().getColor(R.color.color_0F90F9));
                    }
                }
                //向上滚动
                if (dy < 0){
                    int firstVisiblesItems = llm.findFirstVisibleItemPosition();
                    if (firstVisiblesItems < 2) {
                        rl_search.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent));
                    }
                }

            }
        });

    }

    /**
     * 初始化数据
     * */
    private void initData() {
        getNewDataList();
        getBannerData();
    }


    /**
     * 点击事件处理
     * */
    @OnClick({R.id.rl_msg,R.id.rl_search})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                goPage(SearchActivity.class);
                break;
            case R.id.rl_msg:
                if (!isLogin()) return;
                goPage(MsgNoticeActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    /***
     * 信息列表
     * */
    private void getNewDataList() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_NEW_DATA_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("searchUserId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("pageSize", pageSize);
        params.add("pageNum", pageNum);
        params.add("sort", "2");
        startRequest(Task.GET_NEW_DATA_LIST, params, SearchBean.class);

    }
    /***
     * 获取banner信息
     * */
    private void getBannerData() {
        RequestParams params = new RequestParams(UrlConfig.URL_BANNER_DATA);
        startRequest(Task.BANNER_DATA, params,  new TypeToken<List<BannerBean>>() {}.getType(), false);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_NEW_DATA_LIST:    //商品信息网络请求的回调
                refreshLayout.finishRefresh();   //停止控件下拉刷新状态
                refreshLayout.finishLoadmore();  //停止控件上拉加载状态
                if (handlerRequestErr(data)) {
                    SearchBean searchBean = (SearchBean) data.getBody();
                    //------------------数据异常情况-------------------
                    if (searchBean == null || searchBean.getGroups() == null || searchBean.getGroups().size() <= 0) {
                       return;
                    }
                    //-----------------数据正常情况--------------------
                    List<SearchBean.ProductBean> dataListProm = searchBean.getGroups();
                    //通过适配器模式 显示数据
                    if (pageNum == 1) {
                        adapter.setData(dataListProm);
                    } else {
                        adapter.getData().addAll(dataListProm);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    refreshLayout.setEnableLoadmore(dataListProm.size() >= pageSize);
                    //刷新界面
                    adapter.notifyDataSetChanged();

                }
                break;
            case Task.BANNER_DATA:             //网络请求的回调
                if (handlerRequestErr(data)) {
                   //从data里面取出banner数据列表,然后数据显示到banner上，并设置点击事件 跳转到WebView
                    List<BannerBean> bannerBeanList = (List<BannerBean>) data.getBody();
                    if (bannerBeanList != null&& bannerBeanList.size() > 0) {
                        banner.update(bannerBeanList);
                        banner.setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                if(bannerBeanList.get(position).getPageUrl() != null){
//                                    goWebPage("", bannerBeanList.get(position).getPageUrl());
                                    goWebPage("", bannerBeanList.get(position).getPageUrl());
                                }
                            }
                        });
                    }
                }
                break;

            default:
                break;

        }
    }



    /**
     * 轮播图图片适配器
     * */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            String imgUrl = ((BannerBean) path).getImageUrl();
            if(StringUtils.isEmpty(imgUrl))
                Glide.with(context).load(R.drawable.icon_banner_default).into(imageView);
            else
                Glide.with(context).load(imgUrl).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getTag() == Constants.GET_NEW_MSG) {
            if (UserUtils.isLogin()) {
//                getMsgData();
            } else {
                iv_new_msg.setVisibility(View.INVISIBLE);
            }
        }
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

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }



}
