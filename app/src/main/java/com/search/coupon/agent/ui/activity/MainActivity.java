package com.search.coupon.agent.ui.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Keys;
import com.search.coupon.agent.ui.fragment.BillFragment;
import com.search.coupon.agent.ui.fragment.HomeFragment;
import com.search.coupon.agent.ui.fragment.MineFragment;
import com.search.coupon.agent.ui.fragment.PublishFragment;
import com.search.coupon.agent.utils.LogUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.view.DownloadDialog;
import com.search.coupon.agent.view.OneButtonDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by song on 2018/1/17 0017.
 */
public class MainActivity extends BaseActivity {
    public static final String CURRENT_POSITION = "current_position";
    public static final String IS_CLEAR_FRAGMENT = "is_clear_fragment";
    public static final int HOME = 0;
    public static final int BILL = 1;
    public static final int USE = 2;
    public static final int MINE = 3;

    @BindView(R.id.flt_main)
    FrameLayout fltMain;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.rlt_home)
    RelativeLayout rltHome;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.rlt_bill)
    RelativeLayout rltBill;
    @BindView(R.id.tv_use)
    TextView tvUse;
    @BindView(R.id.rlt_use)
    RelativeLayout rltUse;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.rlt_mine)
    RelativeLayout rltMine;

    private TabItem[] items;
    private Fragment homeFragment;
    private Fragment billFragment;
    private Fragment useFragment;
    private Fragment mineFragment;
    private Fragment currentFragment;
    private int currentPosition = HOME;
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

        initTabItem();
        clearFragment();
        switchFragment(currentPosition);

//        showDialog();
    }

    @Override
    public void dealLogicAfterInitView() {


    }

    @Override
    public void OnTopLeftClick() {

    }

    @Override
    public void OnTopRightClick() {

    }

    @OnClick({R.id.rlt_home, R.id.rlt_bill, R.id.rlt_use, R.id.rlt_mine})
    @Override
    public void onClickEvent(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rlt_home:
                switchFragment(HOME);
                break;
            case R.id.rlt_bill:
                if (!isLogin()) return;
                switchFragment(BILL);
                break;
            case R.id.rlt_use:
                switchFragment(USE);
                break;
            case R.id.rlt_mine:
                switchFragment(MINE);
                break;
            default:
                break;
        }
    }

    private void initTabItem() {
        items = new TabItem[4];
        items[0] = new TabItem(tvHome, R.drawable.ic_main_home_t, R.drawable.ic_main_home_f, true);
        items[1] = new TabItem(tvOrder, R.drawable.ic_main_bill_t, R.drawable.ic_main_bill_f, true);
        items[2] = new TabItem(tvUse, R.drawable.ic_main_use_t, R.drawable.ic_main_use_f, true);
        items[3] = new TabItem(tvMine, R.drawable.ic_main_mine_t, R.drawable.ic_main_mine_f, true);
    }

    private void clearFragment() {
        try {
            if (currentFragment != null) {
                FragmentTransaction begin = getSupportFragmentManager().beginTransaction();
                for (TabItem item : items) {
                    if (item.getFragment() != null) {
                        begin.remove(item.getFragment());
                        item.clearFragment();
                    }
                }
                begin.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        homeFragment = null;
        billFragment = null;
        useFragment = null;
        mineFragment = null;
        currentFragment = null;
    }
    public void switchFragment(int position) {
        Fragment fragment = getFragment(position);
        if (currentFragment != null && fragment == currentFragment) {
            return;
        }
        for (TabItem item : items) {
            if (items[position] != item) {
                item.setChecked(false);
            }
        }
        items[position].setFragment(fragment);
        items[position].setChecked(true);

        currentPosition = position;
        currentFragment = fragment;
    }

    private Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case HOME:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                fragment = homeFragment;
                break;
            case BILL:
                if (billFragment == null) {
                    billFragment = new BillFragment();
                }
                fragment = billFragment;
                break;
            case USE:
                if (useFragment == null) {
                    useFragment = new PublishFragment();
                }
                fragment = useFragment;
                break;
            case MINE:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                fragment = mineFragment;
                break;
        }
        return fragment;
    }





    /**
     * TabItem对象，把底下的Tab看成对象操作
     * */
    class TabItem {
        private Drawable checkDrawable;
        private Drawable unCheckDrawable;
        private TextView tvTabItem;
        private int checkColor;
        private int unCheckColor;
        private Fragment fragment;
        private boolean saveData;

        TabItem(TextView ivTabItem, int checkDrawableId, int unCheckDrawableId, boolean saveData) {
            this.tvTabItem = ivTabItem;
            this.checkDrawable = getResources().getDrawable(checkDrawableId);
            this.unCheckDrawable = getResources().getDrawable(unCheckDrawableId);
            this.saveData = saveData;
            this.unCheckColor = getResources().getColor(R.color.color_6E757F);
            this.checkColor = getResources().getColor(R.color.color_0C9BFF);

        }

        public void setChecked(boolean isChecked) {
            tvTabItem.setCompoundDrawablesWithIntrinsicBounds(null, isChecked ? checkDrawable : unCheckDrawable, null, null);
            tvTabItem.setTextColor(isChecked ? checkColor : unCheckColor);
            if (isChecked) {
                showFragment();
            } else {
                hideFragment();
            }
        }

        private void showFragment() {
            if (fragment != null) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                if (saveData) {
                    if (fragment.isHidden()) {
                        trans.show(fragment);
                    } else {
                        trans.add(R.id.flt_main, fragment);
                    }
                } else {
                    if (fragment.isDetached()) {
                        trans.attach(fragment);
                    } else {
                        trans.add(R.id.flt_main, fragment);
                    }
                }
                trans.commit();
            }
            List f = getSupportFragmentManager().getFragments();
            LogUtils.e((f != null ? f.size() : 0) + "");
        }

        private void hideFragment() {
            if (fragment != null) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                if (saveData) {
                    trans.hide(fragment);
                } else {
                    trans.detach(fragment);
                }
                trans.commit();
            }
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

//        public void showBadger(String newsNum) {
//            if (TextUtils.isEmpty(newsNum) || "0".equals(newsNum)) {
//                tvTabNewsItem.setVisibility(View.GONE);
//            } else {
//                tvTabNewsItem.setText(newsNum);
//                tvTabNewsItem.setVisibility(View.VISIBLE);
//            }
//        }

        public void clearFragment() {
            fragment = null;
        }

        public Fragment getFragment() {
            return fragment;
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePushData(intent);
    }

    public void handlePushData(Intent intent) {
        Bundle bundle = intent.getBundleExtra(Keys.KEY_DATA);
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.CURRENT_POSITION)) {
                int tab = bundle.getInt(MainActivity.CURRENT_POSITION);
                switchFragment(tab);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //判断剪切版时候有内容
        if(!clipboardManager.hasPrimaryClip())
            return;
        ClipData clipData = clipboardManager.getPrimaryClip();
        //获取 text
        String content = clipData.getItemAt(0).getText().toString();
        if (StringUtils.isEmpty(content)) return;
        showTipDialog(content);
        clipboardManager.setText("");
    }


    /**
     * 展示弹框
     */
    protected void showTipDialog(String content) {
        final OneButtonDialog dialog = new OneButtonDialog(this);
        dialog.setContent(content);
        dialog.setOkText("搜索");
        dialog.setOnDownLoadClickListener(new DownloadDialog.OnDownLoadClickListener() {
            @Override
            public void onLeftClick() {
                dialog.dismiss();
            }
            @Override
            public void onRightClick() {
                dialog.dismiss();
                Bundle bd = new Bundle();
                bd.putString("content",content);
                goPage(SearchActivity.class,bd);
            }
        });
        dialog.show();
    }
}
