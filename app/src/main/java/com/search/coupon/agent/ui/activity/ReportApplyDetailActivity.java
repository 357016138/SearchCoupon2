package com.search.coupon.agent.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.donkingliang.imageselector.ImageSelectorActivity;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.PicMendRecyclerGridViewAdapter;
import com.search.coupon.agent.bean.PicDataBean;
import com.search.coupon.agent.bean.ProductCodeBean;
import com.search.coupon.agent.bean.SubmitApplyFormResult;
import com.search.coupon.agent.bean.UploadMultipleImgResult;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.network.HttpType;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.utils.DateUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.DiaLogUtils;
import com.search.coupon.agent.utils.FileUtils;
import com.search.coupon.agent.utils.IDCardTextWatcher;
import com.search.coupon.agent.utils.IDUtils;
import com.search.coupon.agent.utils.MoneyTextWatcher;
import com.search.coupon.agent.utils.PhoneNumberTextWatcher;
import com.search.coupon.agent.utils.RegexValidateUtil;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserManager;
import com.search.coupon.agent.utils.compress.Luban;
import com.search.coupon.agent.view.iosspinner.BaseSpinnerAdapter;
import com.search.coupon.agent.view.iosspinner.SpinnerPop;
import com.search.coupon.agent.zip.FileData;
import com.search.coupon.agent.zip.ZipUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * @author baipeng
 * @Title ReportApplyDetailActivity
 * @Date 2018/2/27 11:39
 * @Description ReportApplyDetailActivity.
 */
public class ReportApplyDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.recyclerview_img)
    RecyclerView mImgRecyclerview;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.rg_return_mode)
    RadioGroup rg_return_mode;
    @BindView(R.id.rg_settlement_type)
    RadioGroup rg_settlement_type;
    @BindView(R.id.rg_collect_type)
    RadioGroup rg_collect_type;
    @BindView(R.id.ll_return_info)
    View ll_return_info;
    @BindView(R.id.ll_return_ratio)
    View ll_return_ratio;
    @BindView(R.id.ll_return_amount)
    View ll_return_amount;
    @BindView(R.id.et_loan_name)
    EditText et_loan_name;
    @BindView(R.id.et_phone_num)
    EditText et_phone_num;
    @BindView(R.id.et_idcard_num)
    EditText et_idcard_num;
    @BindView(R.id.et_house_num)
    EditText et_house_num;
    @BindView(R.id.et_return_amount)
    EditText et_return_amount;
    @BindView(R.id.et_return_rate)
    EditText et_return_rate;
    private PicMendRecyclerGridViewAdapter mImgAdapter;
    private ArrayList<String> addImgList = new ArrayList<>();
    private SpinnerPop spinnerPop;//拍照相册菜单
    private String originalPicPath;
    private Uri originalPicUri;
    private String zipPath, inquiryCode;
    private String settlementType = "1";
    private String collectType = "1";
    private String returnType = "0";
    private ArrayList<String> productCodeList = new ArrayList<>();
    private final List<String> idCardList = Arrays.asList(Constants.IDCARD);
    private InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = 0; i < source.toString().length(); i++) {
                if (!idCardList.contains(String.valueOf(source.charAt(i)))) {
                    return "";
                }
                if (et_idcard_num.getText().toString().length() < 19) {
                    if ("x".equals(String.valueOf(source.charAt(i))) || "X".equals(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                }
            }
            return null;
        }
    };
    private InputFilter nameFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!source.toString().contains("·")
                    && !source.toString().contains("•")
                    && !source.toString().matches("^[\\u4e00-\\u9fa5]+$")) {
                return "";
            }
            return null;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_report_apply_detail);
    }

    @Override
    public void dealLogicBeforeInitView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = getIntentData();
        productCodeList = bundle.getStringArrayList("productCodeList");
        inquiryCode = bundle.getString("inquiryCode");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("填写报单信息");
        topBar.setLineVisible(true);

        setBtnState(false);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        mImgRecyclerview.setLayoutManager(glm);
//        int spacingInPixels = DensityUtil.dip2px(this, 20);
//        mImgRecyclerview.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));

        initAdapter();
        //弹出菜单数据初始化
        String[] menu = getResources().getStringArray(R.array.selectPicture);
        final ArrayList<String> array = new ArrayList<>();
        array.addAll(Arrays.asList(menu));
        BaseSpinnerAdapter baseSpinnerAdapter = new BaseSpinnerAdapter(this, array) {
            @Override
            public String getTitle(int position) {
                return array.get(position);
            }
        };
        spinnerPop = new SpinnerPop(this, baseSpinnerAdapter, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPop.hidePopupWindow();
                switch (i) {
                    case 0:
                        //相机
                        checkPermission(new BaseActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
                                startCamera();
                            }
                        }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                        break;
                    case 1:
                        //相册
                        FileUtils.deleteAllFiles(new File(FileUtils.CAMERA));
                        Intent intent = new Intent(ReportApplyDetailActivity.this, ImageSelectorActivity.class);
                        intent.putExtra(com.donkingliang.imageselector.constant.Constants.MAX_SELECT_COUNT, 4 - addImgList.size());
                        intent.putExtra(com.donkingliang.imageselector.constant.Constants.IS_SINGLE, false);
                        startActivityForResult(intent, Constants.MENU_ALBUM);
                        break;
                    case 2:
                        //取消
                        break;
                }
            }
        });
        initListener();
    }

    @Override
    public void dealLogicAfterInitView() {
        btn_submit.setOnClickListener(this);
    }

    @OnClick({R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if(checkSubmitData())
                    uploadImgZip();
                break;
            default:
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        FileUtils.deleteAllFiles(new File(FileUtils.TEMP));
        FileUtils.deleteAllFiles(new File(FileUtils.CAMERA));
    }

    private void deleteDialog(final String itemUrl) {
        DiaLogUtils diaLogUtils = DiaLogUtils.creatDiaLog(this);
        diaLogUtils.setContent("确定删除图片吗");
        diaLogUtils.setSureButton("确定", v -> {
            addImgList.remove(itemUrl);
            if(!addImgList.contains("TYPE_ADD"))
                addImgList.add("TYPE_ADD");
            mImgAdapter.notifyDataSetChanged();
            checkInputData();
            diaLogUtils.destroyDialog();
        });
        diaLogUtils.setCancelButton("取消", v -> {
            diaLogUtils.destroyDialog();
        });
        diaLogUtils.showDialog();
    }

    private void setBtnState(boolean isEnable) {
        if(isEnable) {
            btn_submit.setEnabled(true);
            btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_loan));
            ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
        } else {
            btn_submit.setEnabled(false);
            btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            ll_btn.setBackground(null);
        }
    }

    private void initAdapter() {
        addImgList.add("TYPE_ADD");
        mImgAdapter = new PicMendRecyclerGridViewAdapter(this, addImgList);
        mImgRecyclerview.setAdapter(mImgAdapter);
    }

    private void initListener() {
        mImgAdapter.setOnRecyclerViewItemListener(new PicMendRecyclerGridViewAdapter.OnRecyclerViewItemListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if("TYPE_ADD".equals(addImgList.get(position))) {
                    DeviceUtils.hideInputKeyBoad(ReportApplyDetailActivity.this);
                    spinnerPop.showPopupWindow();
                } else {
                    //跳转到查看大图页面
                    imageWall(position);
                }
            }

            @Override
            public void onItemDeleteClickListener(View view, String data) {
                deleteDialog(data);
            }
        });

        rg_return_mode.setOnCheckedChangeListener(this);
        rg_settlement_type.setOnCheckedChangeListener(this);
        rg_collect_type.setOnCheckedChangeListener(this);
        et_phone_num.addTextChangedListener(new PhoneNumberTextWatcher(et_phone_num));
        et_return_amount.addTextChangedListener(new MoneyTextWatcher(et_return_amount));
        et_return_rate.addTextChangedListener(new MoneyTextWatcher(et_return_rate));
        et_loan_name.addTextChangedListener(mWatcher);
        et_loan_name.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18), nameFilter});
        et_phone_num.addTextChangedListener(mWatcher);
        et_idcard_num.addTextChangedListener(new IDCardTextWatcher(et_idcard_num));
        et_idcard_num.addTextChangedListener(mWatcher);
        et_idcard_num.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20), inputFilter});
        et_house_num.addTextChangedListener(mWatcher);
        et_return_amount.addTextChangedListener(mWatcher);
        et_return_rate.addTextChangedListener(mWatcher);
    }

    /**
     * 启动相机
     */
    private void startCamera() {
        originalPicPath = null;
        originalPicUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            File cameraDir = new File(FileUtils.CAMERA);
            if(!cameraDir.exists()){
                cameraDir.mkdirs();
            }
            File cameraFile = new File(cameraDir,"IMAGE_" + new SimpleDateFormat("HHmmss").format(new Date()) + ".jpg");

            originalPicUri = Uri.fromFile(cameraFile);
            originalPicPath = cameraFile.getAbsolutePath();
        } else {
            File oriPhotoFile = null;
            try {
                oriPhotoFile = createOriImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (oriPhotoFile == null)
                return;
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            originalPicUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", oriPhotoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, originalPicUri);
        startActivityForResult(intent, Constants.MENU_CAMERA);
    }

    /**
     * 创建原图像保存的文件
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "IMAGE_" + new SimpleDateFormat("HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(imgNameOri, ".jpg", pictureDirOri);
        originalPicPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.MENU_CAMERA://拍照
                getCameraImage();
                break;
            case Constants.MENU_ALBUM://相册
                if (data != null) {
                    ArrayList<String> retImgPathList = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
                    if (retImgPathList != null && retImgPathList.size() > 0) {
                        compressWithRx(retImgPathList);
                    }
                }
                break;
        }
    }

    /**
     * 获取相机图片
     */
    private void getCameraImage() {
        File file = new File(originalPicPath);
        if (file.exists()) {
            //追加到系统相册
//            addPicToGallery(originalPicPath);
            if(file.length() > 0) {
                ArrayList<String> list = new ArrayList<>();
                list.add(originalPicPath);
                compressWithRx(list);
            }
        }
    }

    private void compressWithRx(final ArrayList<String> photos) {
        showDialog();
        Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(new Function<ArrayList<String>, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(@NonNull ArrayList<String> list) throws Exception {
                        return Luban.with(ReportApplyDetailActivity.this).load(list).setTargetDir(getPath()).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(@NonNull ArrayList<String> list) throws Exception {
                        dissDialog();
                        addLocalPicture(list);
                        mImgAdapter.notifyDataSetChanged();
                        checkInputData();
                        FileUtils.deleteAllFiles(new File(FileUtils.CAMERA));
                    }
                });
    }

    /**
     * 把图像添加进系统相册
     *
     * @param imgPath 图像路径
     */
    private void addPicToGallery(String imgPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imgPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    /**
     * 跳转查看大图页面
     *
     * @param position
     */
    private void imageWall(int position) {
        ArrayList<String> imageUrlList = new ArrayList<>();//传给查看大图页面的集合
        Intent intent = new Intent(this, ImageWallActivity.class);
        if (addImgList != null) {
            //获取URL传给查看大图页面
            for (String pathUrl : addImgList) {
                //如果是新增证明项就不做处理
                if ("TYPE_ADD".equals(pathUrl)) {
                    continue;
                }
                imageUrlList.add(pathUrl);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_WALL_INDEX, position);//当前所选下标
        bundle.putStringArrayList(Constants.IMAGE_WALL_DATA, imageUrlList);//查看大图集合
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 追加本地图片
     */
    private void addLocalPicture(ArrayList<String> imgPathList) {
        if (imgPathList != null && imgPathList.size() > 0) {
            for (String imageUrl : imgPathList) {
                addImgList.add(addImgList.size() - 1, imageUrl);
                if(addImgList.size() == 4) {
                    addImgList.remove(addImgList.size() - 1);
                    break;
                }
            }
        }
    }

    private String getPath() {
        String path = FileUtils.TEMP + File.separator + "image" + File.separator;
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_no_return:
                ll_return_info.setVisibility(View.GONE);
                returnType = "0";
                et_return_amount.setText("");
                et_return_rate.setText("");
                break;
            case R.id.rb_fixed_return:
                ll_return_info.setVisibility(View.VISIBLE);
                ll_return_amount.setVisibility(View.VISIBLE);
                ll_return_ratio.setVisibility(View.GONE);
                returnType = "1";
                et_return_rate.setText("");
                break;
            case R.id.rb_ratio_return:
                ll_return_info.setVisibility(View.VISIBLE);
                ll_return_amount.setVisibility(View.GONE);
                ll_return_ratio.setVisibility(View.VISIBLE);
                returnType = "2";
                et_return_amount.setText("");
                break;
            case R.id.rb_settlement_one:
                settlementType = "1";
                break;
            case R.id.rb_before_collect:
                collectType = "1";
                break;
            case R.id.rb_after_collect:
                collectType = "0";
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.UPLOAD_ZIP_FILE:
                if (handlerRequestErr(data)) {
                    UploadMultipleImgResult dataBean = (UploadMultipleImgResult) data.getBody();
                    if (dataBean != null) {
                        if(!StringUtils.isEmpty(zipPath)) {
                            FileUtils.deleteAllFiles(new File(zipPath));
                            zipPath = null;
                        }
                        submitApplyInfo(dataBean.getFileList());
                    } else {
                        dissDialog();
                    }
                } else {
                    dissDialog();
                }
                break;
            case Task.SUBMIT_APPLY_FORM:
                dissDialog();
                if (handlerRequestErr(data)) {
                    SubmitApplyFormResult dataBean = (SubmitApplyFormResult) data.getBody();
                    if (dataBean != null) {
                        EventBus.getDefault().post(new MessageEvent(Constants.GET_REFRESH_ORDER_LIST));
                        Bundle bd = new Bundle();
                        bd.putString("orderNum", dataBean.getOrderNum());
                        goPage(ReportApplySuccessActivity.class, bd);
                        finish();
                    }
                }
                break;
        }
    }

    /**
     * 图片上传
     *
     */
    private void uploadImgZip() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                try {
                    if (addImgList.size() == 0) {
                        toast("亲，请添加房本照片");
                        return;
                    }
                    ArrayList<FileData> fileList = getZipFileData(addImgList, "H");
                    zipPath = FileUtils.TEMP + UserManager.getUserId()
                            + DateUtils.format(System.currentTimeMillis(), "yyyyMMddkkmmssSSS") + ".zip";
                    final File zipFile = new File(zipPath);
                    if (!zipFile.exists()) {
                        File fileParentDir = zipFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        zipFile.createNewFile();
                    }
                    ZipUtils.zipFiles(fileList, zipFile);
                    uploadZipFile(zipPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void uploadZipFile(String zipPath) {
        showDialog("正在提交报单，请稍后。。。");
        RequestParams params = new RequestParams(UrlConfig.URL_UPLOAD_ZIP_FILE);
        params.setHttpType(HttpType.UPLOAD);
        params.setContentType("application/octet-stream");
        params.setFormName("zip");
        params.add("zip", zipPath);
        startRequest(Task.UPLOAD_ZIP_FILE, params, UploadMultipleImgResult.class, false);
    }

    private void submitApplyInfo(List<UploadMultipleImgResult.FileListBean> dataFileList) {
        List<PicDataBean> pathList = new ArrayList<>();
        for(UploadMultipleImgResult.FileListBean fileBean : dataFileList) {
            if(!StringUtils.isEmpty(fileBean.getFilePath())) {
                PicDataBean picBean = new PicDataBean();
                picBean.setUrl(fileBean.getFilePath());
                pathList.add(picBean);
            }
        }
        List<ProductCodeBean> productList = new ArrayList<>();
        for(String code : productCodeList) {
            if(!StringUtils.isEmpty(code)) {
                ProductCodeBean productBean = new ProductCodeBean();
                productBean.setProductCode(code);
                productList.add(productBean);
            }
        }
        RequestParams params = new RequestParams(UrlConfig.URL_SUBMIT_APPLY_FORM);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", UserManager.getUserId());
        params.add("borrowerName", et_loan_name.getText().toString().trim());
        params.add("borrowerId", et_idcard_num.getText().toString().trim().replace(" ","").toUpperCase());
        params.add("borrowerPhone", et_phone_num.getText().toString().trim().replace(" ",""));
        params.add("propertyCertNo", et_house_num.getText().toString().trim());
        params.add("commissionType", returnType);
        params.add("commissionAmount", et_return_amount.getText().toString().trim());
        params.add("commissionRate", et_return_rate.getText().toString().trim());
        params.add("commissionSettle", settlementType);
        params.add("collectCommission", collectType);
        params.add("productCodeList", productList);
        params.add("inquiryCode", inquiryCode);
        params.add("propertyCertUrl", pathList);
        startRequest(Task.SUBMIT_APPLY_FORM, params, SubmitApplyFormResult.class, false);
    }

    private ArrayList<FileData> getZipFileData(List<String> dataList, String filaNameStart) {
        ArrayList<FileData> fileList = new ArrayList<>();
        String fileName, filePath;
        for (int pos = 0; pos < dataList.size(); pos++) {
            filePath = dataList.get(pos);
            if("TYPE_ADD".equals(filePath))
                continue;
            if (pos < 9)
                fileName = filaNameStart + "_0" + (pos + 1) + ".jpg";
            else
                fileName = filaNameStart + "_" + (pos + 1) + ".jpg";
            FileData fileData = new FileData(new File(filePath), fileName);
            fileList.add(fileData);
        }
        return fileList;
    }

    private void checkInputData() {
        if(et_loan_name.getText().toString().trim().length() > 0
                && et_phone_num.getText().toString().trim().length() > 0
                && et_idcard_num.getText().toString().trim().length() > 0
                && et_house_num.getText().toString().trim().length() > 0
                && addImgList.size() > 1) {
            if("1".equals(returnType)) {
                if(et_return_amount.getText().toString().trim().length() > 0) {
                    setBtnState(true);
                } else {
                    setBtnState(false);
                }
            } else if("2".equals(returnType)) {
                if(et_return_rate.getText().toString().trim().length() > 0) {
                    setBtnState(true);
                } else {
                    setBtnState(false);
                }
            } else {
                setBtnState(true);
            }
        } else {
            setBtnState(false);
        }
    }

    private boolean checkSubmitData() {
        if(!RegexValidateUtil.isLegalName(et_loan_name.getText().toString().trim())) {
            toast("借款人有误");
            return false;
        }
        if(!et_phone_num.getText().toString().trim().startsWith("1")) {
            toast("手机号有误");
            return false;
        }
        if(et_idcard_num.getText().toString().trim().replace(" ","").toUpperCase().length() != 18 || !IDUtils.vId(et_idcard_num.getText().toString().trim().replace(" ","").toUpperCase())) {
            toast("身份证号有误");
            return false;
        }
        return true;
    }

    private TextWatcher mWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputData();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
