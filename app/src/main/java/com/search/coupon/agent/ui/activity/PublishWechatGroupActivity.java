package com.search.coupon.agent.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.CategoryBean;
import com.search.coupon.agent.bean.ProductDetailBean;
import com.search.coupon.agent.bean.ProvinceBean;
import com.search.coupon.agent.bean.DataBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.common.ShareData;
import com.search.coupon.agent.network.HttpType;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DateUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.FileUtils;
import com.search.coupon.agent.utils.StringUtils;
import com.search.coupon.agent.utils.UserManager;
import com.search.coupon.agent.view.wheelview.adapter.ArrayWheelAdapter;
import com.search.coupon.agent.view.wheelview.widget.WheelView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PublishWechatGroupActivity extends BaseActivity {

    @BindView(R.id.et_title)
    EditText et_title;
//    @BindView(R.id.et_wechat_num)
//    EditText et_wechat_num;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_tag)
    TextView tv_tag;
    @BindView(R.id.et_des)
    EditText et_des;
    @BindView(R.id.tv_remark_num)
    TextView tv_remark_num;
    @BindView(R.id.iv_group_qcode)
    ImageView iv_group_qcode;
    @BindView(R.id.iv_delete_qcode)
    ImageView iv_delete_qcode;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.iv_delete_cover)
    ImageView iv_delete_cover;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.ll_publish_address)
    LinearLayout ll_publish_address;
    @BindView(R.id.ll_publish_category)
    LinearLayout ll_publish_category;
    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.iv_add_tag)
    ImageView iv_add_tag;

    @BindView(R.id.btn_submit)
    TextView btn_submit;
    private static final int PHOTO_REQUEST_GROUP = 1;// 图片选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;
    private String type = "1";
    private String groupImage ;    //微信群二维码图片地址
    private String coverImage ;     //封面图片地址
    private String tempGroupImage ;    //临时微信群二维码图片地址
    private String tempCoverImage ;     //临时封面图片地址

    private List<ProvinceBean> mProvinceList;        //省份列表
    private List<ProvinceBean.CityBean> mCityList;  //市或区列表
    private List<String> mProvinceNameList = new ArrayList<>();
    private HashMap<String, List<String>> mProvinceMap = new HashMap<String, List<String>>();
    private String mCurrentProvinceId;   //当前省份的id
    private String mCurrentCityId;      //当前市区的id
    private List<CategoryBean> mCategoryList;
    private List<CategoryBean.TwoLevelBean> mTwoLevelList;
    private List<String> mCategoryNameList = new ArrayList<>();
    private HashMap<String, List<String>> mCategoryMap = new HashMap<String, List<String>>();

    private String mCurrentCategoryId;      //当前分类的id
    private String mCurrentTwoLevelId;      //当前子分类的id
    private String orderId;
    private String path;
    private ProductDetailBean dataBean;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_publish_wechat_group);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        orderId = bundle.getString("orderId");
        path = bundle.getString("path");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("发布");
        topBar.setLineVisible(true);
        et_des.addTextChangedListener(watcher1); //监听输入数字变化
    }

    @Override
    public void dealLogicAfterInitView() {
        //获取城市列表
        getAddressList("city.json",this);
        //获取分类类目
        getCategoryList();
        //修改订单的情况,获取订单信息展示
        if (!StringUtils.isEmpty(orderId)){
            topBar.setTitle("修改");
            getProductDetail(orderId);
        }

    }



    @OnClick({R.id.rl_group_qcode, R.id.rl_cover, R.id.btn_submit, R.id.ll_publish_address,R.id.ll_publish_category,R.id.ll_tag,R.id.iv_delete})
    @Override
    public void onClickEvent(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.ll_publish_address:                 //选择城市
                if (mProvinceNameList.size() == 0) {
                    getAddressList("city.json",this);
                    return;
                }
                initProvinceDialog(mProvinceNameList);
                break;
            case R.id.ll_publish_category:                 //选择分类
                if (mCategoryNameList.size() == 0) {
                    getCategoryList();
                    return;
                }
                initCategoryDialog(mCategoryNameList);
                break;
            case R.id.ll_tag:                            //自定义Tag
                showEditTextDialog();
                break;
            case R.id.iv_delete:                         //删除Tag
                tv_tag.setText("");
                iv_add_tag.setVisibility(View.VISIBLE);
                iv_delete.setVisibility(View.GONE);
                ll_tag.setOnClickListener(this);
                break;
            case R.id.rl_group_qcode:             //微信群二维码图片地址
                checkPermission(new CheckPermListener(){
                    @Override
                    public void superPermission() {
                        // 激活系统图库，选择一张图片
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        type = "1";    //设置图片上传类型  1.二维码 2.封面
                        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GROUP
                        startActivityForResult(intent, PHOTO_REQUEST_GROUP);

                    }
                }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;

            case R.id.rl_cover:                 //封面图片地址
                checkPermission(new CheckPermListener(){
                    @Override
                    public void superPermission() {
                        // 激活系统图库，选择一张图片
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        type = "2";    //设置图片上传类型  1.二维码 2.封面
                        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GROUP
                        startActivityForResult(intent, PHOTO_REQUEST_GROUP);

                    }
                }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.btn_submit:                 //提交
                if (!isLogin()) return;
                //新发布的和修改的两种情况
                if (!StringUtils.isEmpty(orderId)){        // 修改
                    if (groupImage.equals(tempGroupImage) && coverImage.equals(tempCoverImage)){
                        toast("请修改二维码图片或封面图片");
                        return;
                     }
                     updataGroupInfo(groupImage,coverImage);
                }else {                                  // 新发布
                    submitInfo();
                }
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

    private void submitInfo() {

        String title = et_title.getText().toString().trim();
//        String wechat_num = et_wechat_num.getText().toString().trim();
        String tag = tv_tag.getText().toString().trim();
        String des = et_des.getText().toString().trim();

        //判断是否为空
        if (StringUtils.isEmpty(title)){
            toast("标题不能为空");
            return;
        }
//        if (StringUtils.isEmpty(wechat_num)){
//            toast("微信号不能为空");
//            return;
//        }
        if (StringUtils.isEmpty(mCurrentCategoryId)||StringUtils.isEmpty(mCurrentTwoLevelId)){
            toast("请选择分类");
            return;
        }
        if (StringUtils.isEmpty(mCurrentProvinceId)||StringUtils.isEmpty(mCurrentCityId)){
            toast("请选择地区");
            return;
        }
        if (StringUtils.isEmpty(tag)){
            toast("标签不能为空");
            return;
        }
        if (StringUtils.isEmpty(des)){
            toast("详情不能为空");
            return;
        }
        if (StringUtils.isEmpty(groupImage)){
            toast("请上传群二维码");
            return;
        }
        if (StringUtils.isEmpty(coverImage)){
            toast("请上传封面");
            return;
        }

        RequestParams params = new RequestParams(UrlConfig.URL_PUBLISH_WECHAT_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("groupName", title);
        params.add("groupImage", groupImage);
        params.add("coverImage", coverImage);
//        params.add("userWechat", wechat_num);
        params.add("tags", tag);
        params.add("description", des);
        params.add("provinceId", mCurrentProvinceId);
        params.add("cityId",mCurrentCityId);
        params.add("parentCategory", mCurrentCategoryId);
        params.add("category",mCurrentTwoLevelId);
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, DataBean.class);

    }

    /**
     * 修改订单
     * */
    public void updataGroupInfo(String groupImage,String coverImage) {
        RequestParams params = new RequestParams(UrlConfig.URL_UPDATE_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("uniqueId", dataBean.getUniqueId());
        params.add("groupImage", groupImage);
        params.add("coverImage", coverImage);
        startRequest(Task.UPDATE_GROUP, params, DataBean.class);
    }

    /**
     * 上传头像
     */
    public void uploadImg(String file) {
        RequestParams params = new RequestParams(UrlConfig.URL_UPLOAD_IMAGE);
        params.setHttpType(HttpType.UPLOAD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.setContentType("multipart/form-data");
        params.add("file", file);
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("type", "1");
        startRequest(Task.UPLOAD_IMAGE, params, DataBean.class);
    }

    /**
     * 获取分类类目
     */
    private void getCategoryList() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_CATEGORY);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        startRequest(Task.GET_CATEGORY, params, new TypeToken<List<CategoryBean>>() {}.getType());
    }

    /**
     * 获得商品详情
     * */
    public void getProductDetail(String uniqueId) {
        RequestParams params = new RequestParams(UrlConfig.URL_PRODUCT_DETAIL);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("uniqueId", uniqueId);
        startRequest(Task.PRODUCT_DETAIL, params, ProductDetailBean.class);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PUBLISH_WECHAT_GROUP:
                if (handlerRequestErr(data)) {       //发布成功
                    DataBean dataBean = (DataBean) data.getBody();
                    String orderId = dataBean.getData();
                    Bundle bd = new Bundle();
                    bd.putString("orderId", orderId);
                    goPage(PayActivity.class, bd);
                    finish();
                }
                break;
            case Task.UPDATE_GROUP:
                if (handlerRequestErr(data)) {       //修改成功
                    DataBean dataBean = (DataBean) data.getBody();
                    toast("修改成功");
                    finish();
                }
                break;

            case Task.GET_CATEGORY:            //获取分类类目
                if (handlerRequestErr(data)) {
                    mCategoryList = (List<CategoryBean>) data.getBody();
                    if (mCategoryList != null && mCategoryList.size() > 0) {

                        for (int i = 0; i < mCategoryList.size(); i++) {
                            mCategoryNameList.add(mCategoryList.get(i).getCategoryName());
                            List<CategoryBean.TwoLevelBean> twoLevelList = mCategoryList.get(i).getTwoLevel();
                            List<String> mTwoLeveNameList = new ArrayList<>();
                            for (int j = 0; j < twoLevelList.size(); j++){
                                mTwoLeveNameList.add(twoLevelList.get(j).getCategoryName());
                            }
                            mCategoryMap.put(mCategoryList.get(i).getCategoryName(),mTwoLeveNameList);

                        }
                    }
                }
                break;
            case Task.UPLOAD_IMAGE:          //上传图片
                if (handlerRequestErr(data)) {
                    DataBean dataBean = (DataBean) data.getBody();
                    String url = dataBean.getData();

                    if ("1".equals(type)){
                        groupImage = url;
                        iv_delete_qcode.setVisibility(View.VISIBLE);
                    }else if ("2".equals(type)){
                        coverImage = url;
                        iv_delete_cover.setVisibility(View.VISIBLE);
                    }else {
                        return;
                    }
                }
                break;
            case Task.PRODUCT_DETAIL:
                if (handlerRequestErr(data)) {
                    dataBean = (ProductDetailBean) data.getBody();
                    if (dataBean != null) {
                        updateDetailInfo(dataBean);
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     * 赋值
     * */
    private void updateDetailInfo(ProductDetailBean dataBean) {
        et_title.setText(dataBean.getGroupName());
//        et_wechat_num.setText(dataBean.getUserWechat());
        tv_category .setText(dataBean.getParentCategory()+" "+dataBean.getCategory());
        tv_address.setText(dataBean.getProvince()+" "+dataBean.getCity());
        tv_tag.setText(dataBean.getTags());
        et_des.setText(dataBean.getDescription());
        tv_remark_num.setText(et_des.getText().length()+"/100");
        Glide.with(this).load(dataBean.getGroupImage()).into(iv_group_qcode);
        Glide.with(this).load(dataBean.getCoverImage()).into(iv_cover);

        iv_delete_cover.setVisibility(View.VISIBLE);
        iv_delete_qcode.setVisibility(View.VISIBLE);
        //临时赋值
        tempGroupImage = dataBean.getGroupImage();
        tempCoverImage = dataBean.getCoverImage();
        //赋值
        groupImage = dataBean.getGroupImage();
        coverImage = dataBean.getCoverImage();

        et_title.setFocusable(false);
//        et_wechat_num.setFocusable(false);
        ll_publish_category.setOnClickListener(null);
        ll_publish_address.setOnClickListener(null);
        ll_tag.setOnClickListener(null);
        iv_add_tag.setVisibility(View.GONE);
        et_des.setFocusable(false);

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GROUP) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                //剪切图片
                Intent intent = new Intent(this, CropPicActivity.class);
                intent.setData(uri);
                startActivityForResult(intent, PHOTO_REQUEST_CUT);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {

                Bitmap bitmap = data.getParcelableExtra("data");

                if ("1".equals(type)){
                    iv_group_qcode.setImageBitmap(bitmap);
                }else if ("2".equals(type)){
                    iv_cover.setImageBitmap(bitmap);
                }
                //压缩并上传图片
                compressImage(bitmap);
                //保存到SharedPreferences
//                saveBitmapToSharedPreferences(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public void compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        try {
            String filePath = FileUtils.TEMP + UserManager.getUserId()
                    + DateUtils.format(System.currentTimeMillis(), "yyyyMMddkkmmssSSS") + ".png";
            File file = new File(filePath);

            if (!file.exists()) {
                File fileParentDir = file.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadImg(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
//         recycleBitmap(bitmap);

    }

    /**
     * 销毁图片
     */
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    // 可输入文本的提示框
    private void showEditTextDialog(){
        final EditText edt = new EditText(this);
        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
//        edt.setEms(4);
//        edt.setMaxEms(4);
        new AlertDialog.Builder(this)
                .setTitle("请输入少于4个字")
                .setView(edt)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
//                        text_01.setText(edt.getText().toString());
                        if (StringUtils.isEmpty(tv_tag.getText().toString().trim())){
                            //第一次输入
                            tv_tag.setText(edt.getText().toString());
                            iv_delete.setVisibility(View.VISIBLE);
                        }else {
                            //第二次输入
                            tv_tag.setText(tv_tag.getText().toString().trim()+" "+edt.getText().toString());
                            iv_add_tag.setVisibility(View.GONE);
                            ll_tag.setOnClickListener(null);
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 获得城市列表
     */
    public  void  getAddressList(String fileName,Context context) {
            //将json数据变成字符串
            StringBuilder stringBuilder = new StringBuilder();
            try {
                //获取assets资源管理器
                AssetManager assetManager = context.getAssets();
                //通过管理器打开文件并读取
                BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<List<ProvinceBean>>() {}.getType();
            //这里的json是字符串类型 = jsonArray.toString();
            mProvinceList = new Gson().fromJson(stringBuilder.toString(), listType );


        if (mProvinceList != null && mProvinceList.size() > 0) {

            for (int i = 0; i < mProvinceList.size(); i++) {
                mProvinceNameList.add(mProvinceList.get(i).getAreaName());
                List<ProvinceBean.CityBean> cityList = mProvinceList.get(i).getCity();
                List<String> mCityNameList = new ArrayList<>();
                for (int j = 0; j < cityList.size(); j++){
                    mCityNameList.add(cityList.get(j).getAreaName());
                }
                mProvinceMap.put(mProvinceList.get(i).getAreaName(),mCityNameList);

            }
        }

    }

    /**
     * 展示城市选择框
     * */

    private void initProvinceDialog(List<String> mProvinceNameList) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_regist_choose, null);
        WheelView wv_province = view.findViewById(R.id.wv_province);

        wv_province.setWheelAdapter(new ArrayWheelAdapter(this));  //设置滚轮数据适配器s
        wv_province.setSkin(WheelView.Skin.Holo);  //设置背景颜色
        wv_province.setWheelData(mProvinceNameList); //设置滚轮数据
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle(); //设置选中与未选中字体的样式
        style.selectedTextSize = 20;
        style.textSize = 16;
        wv_province.setStyle(style);

        WheelView wv_city = view.findViewById(R.id.wv_city);
        wv_city.setSkin(WheelView.Skin.Holo);
        wv_city.setWheelAdapter(new ArrayWheelAdapter(this));
        wv_city.setWheelData(mProvinceMap.get(mProvinceNameList.get(wv_province.getSelection())));
        wv_city.setStyle(style);
        wv_province.join(wv_city);            //连接副WheelView  市
        wv_province.joinDatas(mProvinceMap); //副WheelView 市数据


        wv_province.setWheelSize(5);
        wv_province.setSelection(2);
        wv_city.setWheelSize(5);
        wv_city.setSelection(2);

        selectStoreDialog.setContentView(view);
        view.findViewById(R.id.bt_cancel).setOnClickListener(v -> selectStoreDialog.dismiss());

        view.findViewById(R.id.bt_conform).setOnClickListener(v -> {
            //获得当前选择的省市位置
            int provincePosition = wv_province.getCurrentPosition();
            int cityPosition = wv_city.getCurrentPosition();
            //根据位置取出实体类
            ProvinceBean mCurrentProvinceBean = mProvinceList.get(provincePosition);
            ProvinceBean.CityBean mCurrentCityBean = mCurrentProvinceBean.getCity().get(cityPosition);

            mCurrentProvinceId = mCurrentCityBean.getParentId();
            mCurrentCityId= mCurrentCityBean.getId();
            tv_address.setText(mCurrentProvinceBean.getAreaName()+" "+mCurrentCityBean.getAreaName());

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

    /**
     * 展示分类选择框
     * */

    private void initCategoryDialog(List<String> mCategoryNameList) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_regist_choose, null);
        WheelView wv_province = view.findViewById(R.id.wv_province);

        wv_province.setWheelAdapter(new ArrayWheelAdapter(this));  //设置滚轮数据适配器s
        wv_province.setSkin(WheelView.Skin.Holo);  //设置背景颜色
        wv_province.setWheelData(mCategoryNameList); //设置滚轮数据
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle(); //设置选中与未选中字体的样式
        style.selectedTextSize = 20;
        style.textSize = 16;
        wv_province.setStyle(style);

        WheelView wv_city = view.findViewById(R.id.wv_city);
        wv_city.setSkin(WheelView.Skin.Holo);
        wv_city.setWheelAdapter(new ArrayWheelAdapter(this));
        wv_city.setWheelData(mCategoryMap.get(mCategoryNameList.get(wv_province.getSelection())));
        wv_city.setStyle(style);
        wv_province.join(wv_city);            //连接副WheelView  市
        wv_province.joinDatas(mCategoryMap); //副WheelView 市数据

        wv_province.setWheelSize(5);
        wv_province.setSelection(2);
        wv_city.setWheelSize(5);
        wv_city.setSelection(2);

        selectStoreDialog.setContentView(view);
        view.findViewById(R.id.bt_cancel).setOnClickListener(v -> selectStoreDialog.dismiss());

        view.findViewById(R.id.bt_conform).setOnClickListener(v -> {
            //获得当前选择的省市位置
            int provincePosition = wv_province.getCurrentPosition();
            int cityPosition = wv_city.getCurrentPosition();
            //根据位置取出实体类
            CategoryBean mCurrentProvinceBean = mCategoryList.get(provincePosition);
            CategoryBean.TwoLevelBean mCurrentCityBean = mCurrentProvinceBean.getTwoLevel().get(cityPosition);

            mCurrentCategoryId = mCurrentCityBean.getParentId();
            mCurrentTwoLevelId= mCurrentCityBean.getId();
            tv_category.setText(mCurrentProvinceBean.getCategoryName()+" "+mCurrentCityBean.getCategoryName());

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

    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     * */
    private TextWatcher watcher1 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tv_remark_num.setText(s.length()+"/100");
        }
    };

}
