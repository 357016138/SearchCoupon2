package com.search.coupon.agent.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.BankOfDeposit;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.response.FetchBankOfDepositResponse;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.IDCardTextWatcher;
import com.search.coupon.agent.utils.IDUtils;
import com.search.coupon.agent.utils.PhoneNumberTextWatcher;
import com.search.coupon.agent.utils.UtilTools;
import com.search.coupon.agent.view.wheelview.adapter.ArrayWheelAdapter;
import com.search.coupon.agent.view.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 添加银行卡
 */
public class BindBankCardActivity extends BaseActivity {

    private EditText mEtName;
    private EditText mEtIDCard;
    private EditText mEtBankCardNum;
    private RelativeLayout mRlSelectBank;
    private EditText mEtMobileNum;
    private EditText mEtVeriCode;
    private TextView mTvVerification;
    private LinearLayout mLlSuccess;
    private LinearLayout mLlFailed;
    private TextView mTvSelectBankName;

    private static final int UPDATE_TIME = 1;
    private boolean isCreditCard = false;           //绑定的是否是信用卡       true 是 false 否
    private boolean isValidCard = false;           //绑定的是否是公司支持的卡  true 是 false 否
    private List<BankOfDeposit> mBankList;
    private static final int SELECT_BANK_TAG = 0X0001;
    private BindCardHandler mHandle;
    private BankOfDeposit mCurrentBank;
    private List<String> mBankNameList;
    private TextView mBtOk;
    final List<String> idCardList = Arrays.asList(Constants.IDCARD);
    private InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = 0; i < source.toString().length(); i++) {
                if (!idCardList.contains(String.valueOf(source.charAt(i)))) {
                    return "";
                }
            }
            return null;
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_bind_bank_card);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        topBar.setTitle("添加银行卡");
        mEtName = findViewById(R.id.et_bindBankCard_uerName);
        mEtIDCard = findViewById(R.id.et_bind_card_identity_card_num);
        mEtBankCardNum = findViewById(R.id.et_bind_card_bank_card_num);
        mRlSelectBank = findViewById(R.id.rl_bank_card_select_bank);
        mEtMobileNum = findViewById(R.id.et_bindBankCard_phoneName);
        mEtVeriCode = findViewById(R.id.et_bind_bank_verification_code);
        mTvVerification = findViewById(R.id.tv_get_verification_code);
        mLlSuccess = findViewById(R.id.ll_bind_card_success);
        mLlFailed = findViewById(R.id.ll_bind_card_failed);
        mTvSelectBankName = findViewById(R.id.tv_bindBankCard_bank);
        mBtOk = findViewById(R.id.bindBankCard_nextStep);

        mEtBankCardNum.addTextChangedListener(new BankCardNumberTextWatcher(mEtBankCardNum));
        mEtMobileNum.addTextChangedListener(new PhoneNumberTextWatcher(mEtMobileNum));
        mEtIDCard.addTextChangedListener(new IDCardTextWatcher(mEtIDCard));
//        mEtIDCard.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18), inputFilter});

        mEtName.addTextChangedListener(watcher);
        mEtIDCard.addTextChangedListener(watcher);
        mEtBankCardNum.addTextChangedListener(watcher);
        mEtMobileNum.addTextChangedListener(watcher);
        mEtVeriCode.addTextChangedListener(watcher);

        mBtOk.setOnClickListener(this);
        mBtOk.setEnabled(false);
        mRlSelectBank.setOnClickListener(this);
        mTvVerification.setOnClickListener(null);
        findViewById(R.id.bt_done).setOnClickListener(this);
        findViewById(R.id.bt_withdraw_deposit).setOnClickListener(this);
        findViewById(R.id.bt_add_bank_card_retry).setOnClickListener(this);

        mEtName.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int dend) {
                        String regex = "^[\u4E00-\u9FA5·]+$";
                        boolean isChinese = Pattern.matches(regex, charSequence.toString());
                        if (!isChinese) {
                            return "";
                        }
                        return null;
                    }
                }, new InputFilter.LengthFilter(18)
        });

    }

    @Override
    public void dealLogicAfterInitView() {
        mHandle = new BindCardHandler(this, mTvVerification);
        mBankList = new ArrayList<>();
        mBankNameList = new ArrayList<>();
        getBankList();
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_bank_card_select_bank:
                if (mBankList.size() == 0) {
                    getBankList();
                    return;
                }
                initDialog(mBankNameList);
                break;

            case R.id.bindBankCard_nextStep:
                submitInfo();
                break;

            case R.id.tv_get_verification_code: // 获取验证码
                String mobile = mEtMobileNum.getText().toString().replace(" ", "");
                if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
                    UtilTools.toast(this, "请输入正确的手机号码");
                    return;
                }
                getVerificationCode(mobile);
                break;

            case R.id.bt_withdraw_deposit:
                EventBus.getDefault().post(new MessageEvent(Constants.FINISTH));
//                EventBus.getDefault().post(new MessageEvent(Constants.GO_WITHDRAW_DEPOSIT));
                finish();
                break;

            case R.id.bt_done:
                finish();
                break;

            case R.id.bt_add_bank_card_retry:
                mLlFailed.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    /**
     * 获取验证码
     */
    private void getVerificationCode(String mobile) {
        RequestParams params = new RequestParams(UrlConfig.URL_SIGN_IN_CODE);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("phone", mobile);
        params.add("codeType", "C");
        startRequest(Task.SIGN_UP_CODE, params, null);
    }

    /**
     * 获取公司支持的开户银行列表
     */
    private void getBankList() {
        RequestParams params = new RequestParams(UrlConfig.URL_QUERY_BANK_CARD_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.QUERY_BANK_INFO, params, FetchBankOfDepositResponse.class);
    }

    /**
     * 绑定银行卡
     */
    private void submitInfo() {
        submitBinkCardInformation();
    }

    /**
     * 自动适配银行卡类型
     */
    private void autoAdapterBank() {
        RequestParams params = new RequestParams(UrlConfig.URL_AUTO_ADAPTER_BANK);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("bankCardNo", mEtBankCardNum.getText().toString().replaceAll(" ", ""));
        startRequest(Task.AUTO_ADAPTER_BANK, params, BankOfDeposit.class, false);
    }

    /**
     * 请求数据的回调方法
     */
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.QUERY_BANK_INFO:
                if (handlerRequestErr(data)) {
                    FetchBankOfDepositResponse response = (FetchBankOfDepositResponse) data.getBody();
                    List<BankOfDeposit> bankList = response.getList();
                    if (bankList != null && bankList.size() > 0) {
                        mBankList = bankList;
                        for (int i = 0; i < mBankList.size(); i++) {
                            mBankNameList.add(mBankList.get(i).getBankName());
                        }
                    }
                }
                break;

            case Task.CHECK_BANK_CARD:
                if (handlerRequestErr(data)) {
                    mLlSuccess.setVisibility(View.VISIBLE);
                }
                break;

            case Task.SIGN_UP_CODE:
                if (handlerRequestErr(data)) {
                    toast(data.getRspMsg());
                    mHandle.resetTime();
                    mTvVerification.setClickable(false);
                    mHandle.sendEmptyMessage(UPDATE_TIME);
                }
                break;

            case Task.AUTO_ADAPTER_BANK:
                if (handlerRequestErr(data)) {
                    BankOfDeposit bank = (BankOfDeposit) data.getBody();
                    if ("201".equals(bank.getRetCode())) {
                        isValidCard = false;
                        toast(bank.getRetMsg());
                        break;
                    }
                    if (!TextUtils.isEmpty(bank.getBankName())) {
                        isValidCard = true;
                        mTvSelectBankName.setText(bank.getBankName());
                        for (BankOfDeposit sBank : mBankList) {
                            if (sBank.getBankName().equals(bank.getBankName())) {
                                mCurrentBank = sBank;
                            }
                        }
                        if ("1".equals(bank.getIsCreditCard())) {
                            isCreditCard = true;
                        } else {
                            isCreditCard = false;
                        }

                    }
                }
                break;

            case Task.WHETHER_SET_PAY_PASSWORD:
                goPage(MainActivity.class, null);
                break;

            default:
                break;

        }
    }

    private void submitBinkCardInformation() {
        String user = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            UtilTools.toast(this, "持卡人姓名不能为空");
            return;
        }
        String idCardNum = mEtIDCard.getText().toString().trim().replace(" ", "");

        if (TextUtils.isEmpty(idCardNum)) {
            UtilTools.toast(this, "身份证号码不能为空");
            return;
        }

        if (idCardNum.length() != 18 || !IDUtils.vId(idCardNum)) {
            toast("身份证号有误");
            return;
        }

        String bankCardNum = mEtBankCardNum.getText().toString().trim();
        bankCardNum = bankCardNum.replaceAll(" ", "");
        if (TextUtils.isEmpty(bankCardNum)) {
            UtilTools.toast(this, "银行卡号不能为空");
            return;
        }

        if (mCurrentBank == null) {
            UtilTools.toast(this, "请选择开户行");
            return;
        }
        if (!isValidCard) {
            toast("暂不支持此开户行。");
            return;
        }
        if (isCreditCard) {
            toast("不支持信用卡绑定，请绑定储蓄卡。");
            return;
        }

        String mobileNum = mEtMobileNum.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(mobileNum)) {
            UtilTools.toast(this, "手机号不能为空");
            return;
        }
        String verificationCode = mEtVeriCode.getText().toString().trim();
        if (TextUtils.isEmpty(verificationCode)) {
            UtilTools.toast(this, "验证码不能为空");
            return;
        }
        RequestParams params = new RequestParams(UrlConfig.URL_BIND_BANK_CARD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("name", user);
        params.add("idCard", idCardNum);
        params.add("bankCardNo", bankCardNum);
        params.add("openAccBank", mCurrentBank.getBankName());
        params.add("phone", mobileNum);
        params.add("smCode", verificationCode);
        params.add("codeType", "C");
        params.add("bankCode", mCurrentBank.getBankCode());
        startRequest(Task.CHECK_BANK_CARD, params, null);
    }

    private void initDialog(List<String> list) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.invitation_bottom_dialog_layout, null);
        final WheelView wv = view.findViewById(R.id.wv_stores);

        ArrayWheelAdapter adapter = new ArrayWheelAdapter(this);
        adapter.setData(list);
        wv.setWheelAdapter(adapter);
        if (list.size() >= 5) {
            wv.setWheelSize(5);
            wv.setSelection(3);
        } else {
            wv.setWheelSize(3);
            wv.setSelection(2);
        }

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
            mCurrentBank = mBankList.get(position);
            mTvSelectBankName.setText(mCurrentBank.getBankName());
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

    private boolean isTimeing = false;//是否是倒计时状态

    private class BindCardHandler extends Handler {
        private WeakReference<Activity> weakReference;
        private int time;
        private TextView tvCode;

        public BindCardHandler(Activity activity, TextView textView) {
            weakReference = new WeakReference<>(activity);
            tvCode = textView;
        }

        private void resetTime() {
            time = 60;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get().isFinishing()) {
                return;
            }
            switch (msg.what) {
                case UPDATE_TIME:
                    tvCode.setText(time + "s");
                    time--;
                    if (time < 0) {
//                        tvCode.setText(weakReference.get().getResources().getString(R.string.fetch_code));
                        isTimeing = false;
                        setObtainCodeColor();
                    } else {
                        isTimeing = true;
                        tvCode.setTextColor(getResources().getColor(R.color.color_A2A2A2));
                        tvCode.setOnClickListener(null);
                        this.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
                    }
                    break;
            }
        }
    }

    class BankCardNumberTextWatcher implements TextWatcher {

        EditText editText;
        int lastContentLength = 0;
        boolean isDelete = false;

        public BankCardNumberTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringBuffer sb = new StringBuffer(s);
            //是否为输入状态还是删除状态
            isDelete = s.length() > lastContentLength ? false : true;

            //输入状态  输入是第4，第9位，这时需要插入空格
            if (!isDelete && (s.length() == 5 || s.length() == 10 || s.length() == 15 || s.length() == 20)) {
                if (s.length() == 5) {
                    sb.insert(4, " ");
                } else if (s.length() == 10) {
                    sb.insert(9, " ");
                } else if (s.length() == 15) {
                    sb.insert(14, " ");
                } else if (s.length() == 20) {
                    sb.insert(19, " ");
                }
                setContent(sb);
            }

            if (!isDelete && s.length() == 12) {
                autoAdapterBank();
            }
            //删除状态  删除的位置到4，9时，剔除空格
            if (isDelete && (s.length() == 5 || s.length() == 10 || s.length() == 15 || s.length() == 20)) {
                sb.deleteCharAt(sb.length() - 1);
                setContent(sb);
            }
            //默认反显的情况
            if (s.length() == 20 && !sb.toString().contains(" ")) {
                sb.insert(4, " ");
                sb.insert(9, " ");
                sb.insert(14, " ");
                setContent(sb);
            }
            lastContentLength = sb.length();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        /**
         * 添加或删除空格EditText的设置
         *
         * @param sb
         */
        private void setContent(StringBuffer sb) {
            editText.setText(sb.toString());
            //移动光标到最后面
            editText.setSelection(sb.length());
        }
    }

    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean flag1 = mEtName.getText().toString().trim().length() > 0;
            boolean flag2 = mEtIDCard.getText().toString().trim().length() > 0;
            boolean flag3 = mEtBankCardNum.getText().toString().trim().length() > 0;
            boolean flag4 = mEtMobileNum.getText().toString().trim().length() > 0;
            boolean flag5 = mEtVeriCode.getText().toString().trim().length() > 0;

            /**
             * 根据手机号情况设置获取验证码按钮颜色
             * */
            if (flag4) {
                setObtainCodeColor();
            }

            if (flag1 && flag2 && flag3 && flag4 && flag5) {
                mBtOk.setEnabled(true);
                mBtOk.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            } else {
                mBtOk.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                mBtOk.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setObtainCodeColor() {

        int length = mEtMobileNum.getText().toString().replace(" ", "").length();
        if (length == 11 && !isTimeing) {
            mTvVerification.setTextColor(getResources().getColor(R.color.color_3889FF));
            mTvVerification.setOnClickListener(this);
        } else {
            mTvVerification.setTextColor(getResources().getColor(R.color.color_A2A2A2));
            mTvVerification.setOnClickListener(null);
        }
    }


}
