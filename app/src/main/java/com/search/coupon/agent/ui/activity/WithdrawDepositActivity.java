package com.search.coupon.agent.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BankOfDeposit;
import com.search.coupon.agent.bean.BindBankCardInfoBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Keys;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.response.BankCardListResponse;
import com.search.coupon.agent.utils.AESUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.UtilTools;
import com.search.coupon.agent.view.PasswordEditText;
import com.search.coupon.agent.view.PasswordKeyboard;
import com.search.coupon.agent.view.wheelview.adapter.ArrayWheelAdapter;
import com.search.coupon.agent.view.wheelview.widget.WheelView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by RickBerg on 2018/2/11 0011.
 */

public class WithdrawDepositActivity extends BaseActivity implements  PasswordKeyboard.PasswordKeyboardListener {

    private PasswordEditText mEditText;
    private TextView mTvBankName;

    private Dialog mInputPasswordDialog;
    private Dialog mSelectBankDialog;
    private List<BindBankCardInfoBean> mBankList;
    private List<String> mNameList;
    private TextView mTvBalanceOfAccount;
    private EditText mEtDepositAmount;
    private double mBalance;
    private boolean isNeedShowDialog = false;
    private BindBankCardInfoBean mSelectedBank;
    private TextView mBtWithDrawDeposit;
    private Button mBtOk;
    private LinearLayout mLlSuccess;
    private LinearLayout mLlFailed;
    private Dialog inputPasswordDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.withdraw_deposit_activity_layout, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntent().getBundleExtra(Keys.KEY_DATA);
        mBalance = bundle.getDouble("balanceValue");
        mBankList = new ArrayList<>();
        mNameList = new ArrayList<>();
    }

    @Override
    public void initView() {
        mTvBalanceOfAccount = findViewById(R.id.tv_amount);
        mTvBankName = findViewById(R.id.tv_bank_name);
        mEtDepositAmount = findViewById(R.id.tv_deposit_amount);
        mLlSuccess = findViewById(R.id.ll_apply_success);
        mLlFailed = findViewById(R.id.ll_apply_deposit_failed);
        findViewById(R.id.bt_done).setOnClickListener(this);
        mBtWithDrawDeposit = findViewById(R.id.bt_withdraw_deposit);
        mBtWithDrawDeposit.setOnClickListener(this);
        mBtWithDrawDeposit.setEnabled(false);
        mBtWithDrawDeposit.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
        findViewById(R.id.ll_select_bank).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_withdraw_all).setOnClickListener(this);
        findViewById(R.id.bt_add_bank_card_retry).setOnClickListener(this);

        mEtDepositAmount.addTextChangedListener(new TextWatcher() {
            String beforeStr;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString().trim();
                resetBtnState(!TextUtils.isEmpty(amount));
                if (amount.contains(".")) {
                    String sub = amount.substring(amount.indexOf(".") + 1, amount.length());
                    if (sub.length() > 2) {
                        mEtDepositAmount.setText(beforeStr);
                        mEtDepositAmount.setSelection(s.length() - 1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void dealLogicAfterInitView() {
        mTvBalanceOfAccount.setText(formatString(mBalance));
        fetchBankList();

    }

    @Override
    public void OnTopLeftClick() {
    }

    @Override
    public void OnTopRightClick() {

    }


    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_withdraw_deposit:
                String depositAmount = mEtDepositAmount.getText().toString().trim();
                if (depositAmount.contains(",")){
                    depositAmount = depositAmount.replace(",","");
                }
                double amount;
                try {
                    amount = Double.valueOf(depositAmount);
                } catch (NumberFormatException e) {
                    UtilTools.toast(this, "金额格式错误");
                    return;
                }
                if (amount == 0) {
                    UtilTools.toast(this, "金额不能为0");
                    return;
                }
                if (amount < 100) {
                    UtilTools.toast(this, "提现金额不能小于100");
                    return;
                }
                if (amount > mBalance) {
                    UtilTools.toast(this, "金额已超过可提现余额");
                    return;
                }
                initInputPasswordDialog();
                break;
            case R.id.ll_select_bank:
                initDialog(mNameList);
                break;
            case R.id.tv_withdraw_all:
                String content = formatString(mBalance);
                mEtDepositAmount.setText(content);
                mEtDepositAmount.setSelection(content.length());
                break;
            case R.id.bt_done:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_add_bank_card_retry:
                mLlFailed.setVisibility(View.GONE);
                break;

        }
    }

    private void fetchBankList() {
        RequestParams params = new RequestParams(UrlConfig.URL_QUERY_BIND_BANK_CARD_INFO);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("curPage", 1);
        params.add("pageSize", 30);
        startRequest(Task.BIND_BANK_CARD_INFO, params, BankCardListResponse.class, false);
    }


    private void initDialog(List<String> list) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.invitation_bottom_dialog_layout, null);
        final WheelView wv = view.findViewById(R.id.wv_stores);

        ArrayWheelAdapter adapter = new ArrayWheelAdapter(this);
        adapter.setData(list);
        wv.setWheelAdapter(adapter);
        wv.setWheelSize(3);
        wv.setSelection(3);
        wv.setDrawSelectorOnTop(true);
        wv.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.color_CCCCCC);
        style.textSize = 24;
        style.selectedTextColor = getResources().getColor(R.color.color_374953);
        style.textColor = getResources().getColor(R.color.color_6E757F);
        wv.setStyle(style);
        selectStoreDialog.setContentView(view);
        view.findViewById(R.id.bt_cancel).setOnClickListener(v -> selectStoreDialog.dismiss());

        view.findViewById(R.id.bt_conform).setOnClickListener(v -> {
            int position = wv.getCurrentPosition();
            mSelectedBank = mBankList.get(position);
            mTvBankName.setText(mNameList.get(position));
            selectStoreDialog.dismiss();
        });
        DisplayMetrics dm = new DisplayMetrics();
        int height = dm.heightPixels;
        Window window = selectStoreDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_window_style);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        selectStoreDialog.setCanceledOnTouchOutside(false);
        selectStoreDialog.show();
    }


    private void applyForDeposit(double amount) {
        RequestParams params = new RequestParams(UrlConfig.URL_APPLY_FOR_DEPOSIT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("cashAmount", amount);
        params.add("bankCardNo", mSelectedBank.getBankCardNo());
        params.add("openAccBank", mSelectedBank.getOpenAccBank());
        startRequest(Task.APPLY_FOR_DEPOSIT, params, null);
    }

    private void initInputPasswordDialog() {
        inputPasswordDialog = new Dialog(this, R.style.input_password_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_input_password_dialog, null);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            inputPasswordDialog.dismiss();
        });
        PasswordKeyboard keyboard = view.findViewById(R.id.pk_keyboard);
        keyboard.setKeyboardListener(this);
        mEditText = view.findViewById(R.id.et_password);
        mBtOk = view.findViewById(R.id.bt_ok);
        mBtOk.setClickable(false);
        mBtOk.setBackgroundResource(R.drawable.bg_button_disable);
        inputPasswordDialog.setContentView(view);
        DisplayMetrics dm = new DisplayMetrics();
        int height = dm.heightPixels;
        Window window = inputPasswordDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_window_style);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        inputPasswordDialog.setCanceledOnTouchOutside(false);
        inputPasswordDialog.show();
    }

    private void verificationPassword() {
        RequestParams params = new RequestParams(UrlConfig.URL_VERIFICATION_PASSWORD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("paymentPassword", AESUtils.aesEncryptStr(mEditText.getText().toString(), UrlConfig.KEY));
        startRequest(Task.VERIFICATION_PASSWORD, params, BankOfDeposit.class);
    }

    @Override
    public void addNum(int n) {
        mEditText.addPassword(n);
        if (mEditText.getText().length() == 6) {
            mBtOk.setClickable(true);
            mBtOk.setBackgroundResource(R.drawable.bg_loan);
            mBtOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputPasswordDialog.dismiss();
                    verificationPassword();
                }
            });
        }
    }

    @Override
    public void deleteNum() {
        mBtOk.setClickable(false);
        mBtOk.setBackgroundResource(R.drawable.bg_button_disable);
        mEditText.deleteLastPassword();
    }
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.BIND_BANK_CARD_INFO:
                if (data.getRspCode().equals("200")) {
                    BankCardListResponse response = (BankCardListResponse) data.getBody();
                    List<BindBankCardInfoBean> list = response.getList();
                    if (list != null && list.size() > 0) {
                        mBankList.clear();
                        mBankList.addAll(list);
                        //初始选择银行（卡号后四位）数据
                        for (BindBankCardInfoBean card : mBankList) {
                            String cardNum = card.getBankCardNo();
                            mNameList.add(card.getOpenAccBank() + "(" + card.getBankCardNo().substring(cardNum.length() - 4, cardNum.length()) + ")");
                        }
                        mSelectedBank = mBankList.get(0);
                        mTvBankName.setText(mNameList.get(0));
                        if (isNeedShowDialog) {
                            isNeedShowDialog = false;
                            initDialog(mNameList);
                        }
                    }
                } else {
                    handlerRequestErr(data);
                }
                break;
            case Task.APPLY_FOR_DEPOSIT:
                if (handlerRequestErr(data)) {
                    mLlSuccess.setVisibility(View.VISIBLE);
                }else {
                    mLlFailed.setVisibility(View.VISIBLE);
                }
                break;
            case Task.VERIFICATION_PASSWORD:
                if (data.getRspCode().equals("200")){
                    String depositAmount = mEtDepositAmount.getText().toString().trim();
                    if (depositAmount.contains(",")){
                        depositAmount = depositAmount.replace(",","");
                    }
                    double amount;
                    try {
                        amount = Double.valueOf(depositAmount);
                    } catch (NumberFormatException e) {
                        UtilTools.toast(this, "金额格式错误");
                        return;
                    }
                    applyForDeposit(amount);
                } else {
                    showFailedDialog();
                }
                break;
        }
    }

    public String formatString(double data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

    private void showFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_no_bg);
        View myView = LayoutInflater.from(this).inflate(R.layout.dialog_withdraw_deposit_error, null);
        builder.setView(myView);

        myView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final Dialog dialog = builder.create();
        myView.findViewById(R.id.bt_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goPage(SetWithdrawPasswordActivity.class, null);
            }
        });

        myView.findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initInputPasswordDialog();
            }
        });
        dialog.show();
    }

    private void resetBtnState(boolean isEnable) {
        if (isEnable) {
            mBtWithDrawDeposit.setEnabled(true);
            mBtWithDrawDeposit.setBackground(getResources().getDrawable(R.drawable.bg_loan));
        } else {
            mBtWithDrawDeposit.setEnabled(false);
            mBtWithDrawDeposit.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
        }
    }
}
