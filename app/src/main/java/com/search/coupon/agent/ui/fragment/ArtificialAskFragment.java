package com.search.coupon.agent.ui.fragment;

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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.imageselector.ImageSelectorActivity;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.search.coupon.agent.R;
import com.search.coupon.agent.adapter.PicMendRecyclerGridViewAdapter;
import com.search.coupon.agent.bean.ArtificialAskPriceResultBean;
import com.search.coupon.agent.bean.PicDataBean;
import com.search.coupon.agent.bean.UploadMultipleImgResult;
import com.search.coupon.agent.common.BaseFragment;
import com.search.coupon.agent.common.Constants;
import com.search.coupon.agent.network.HttpType;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.service.MessageEvent;
import com.search.coupon.agent.ui.activity.AskPriceResultActivity;
import com.search.coupon.agent.ui.activity.ImageWallActivity;
import com.search.coupon.agent.utils.DateUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.utils.DiaLogUtils;
import com.search.coupon.agent.utils.FileUtils;
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
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * @author baipeng
 * @Title ArtificialAskFragment
 * @Date 2018/2/9 16:47
 * @Description ArtificialAskFragment.
 */
public class ArtificialAskFragment extends BaseFragment {
    @BindView(R.id.recyclerview_img)
    RecyclerView mImgRecyclerview;
    @BindView(R.id.et_house_name)
    EditText etHouseName;
    @BindView(R.id.btn_submit_ask_price)
    TextView btnAskPrice;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    Unbinder unbinder;
    private PicMendRecyclerGridViewAdapter mImgAdapter;
    private ArrayList<String> addImgList = new ArrayList<>();
    private SpinnerPop spinnerPop;//拍照相册菜单
    private String originalPicPath;
    private Uri originalPicUri;
    private String zipPath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_ask_price, container, false);
        bindView(view);
        initView();
        return view;
    }

    @OnClick({R.id.btn_submit_ask_price})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_ask_price:
                if(StringUtils.isEmpty(etHouseName.getText().toString().trim())) {
                    toast("请输入小区名称");
                    return;
                }
                if(addImgList.size() <= 1) {
                    toast("请上传房本照片");
                    return;
                }
                uploadImgZip();
                break;
            default:
                break;

        }
    }

    /**
     * 初始化控件 用ButterKnife 简约
     */
    private void bindView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this, view);

    }

    private void initView() {
        setBtnState(false);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        mImgRecyclerview.setLayoutManager(glm);
//        int spacingInPixels = DensityUtil.dip2px(getActivity(), 10);
//        mImgRecyclerview.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));

        initAdapter();

        //弹出菜单数据初始化
        String[] menu = getResources().getStringArray(R.array.selectPicture);
        final ArrayList<String> array = new ArrayList<>();
        array.addAll(Arrays.asList(menu));
        BaseSpinnerAdapter baseSpinnerAdapter = new BaseSpinnerAdapter(getActivity(), array) {
            @Override
            public String getTitle(int position) {
                return array.get(position);
            }
        };
        spinnerPop = new SpinnerPop(getActivity(), baseSpinnerAdapter, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPop.hidePopupWindow();
                switch (i) {
                    case 0:
                        //相机
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                startCamera();
                            }
                        }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                        break;
                    case 1:
                        //相册
                        FileUtils.deleteAllFiles(new File(FileUtils.CAMERA));
                        Intent intent = new Intent(getActivity(), ImageSelectorActivity.class);
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

    private void initAdapter() {
        addImgList.add("TYPE_ADD");
        mImgAdapter = new PicMendRecyclerGridViewAdapter(getActivity(), addImgList);
        mImgRecyclerview.setAdapter(mImgAdapter);
    }

    private void initListener() {
        etHouseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mImgAdapter.setOnRecyclerViewItemListener(new PicMendRecyclerGridViewAdapter.OnRecyclerViewItemListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if("TYPE_ADD".equals(addImgList.get(position))) {
                    DeviceUtils.hideInputKeyBoad(getActivity());
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
            originalPicUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", oriPhotoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, originalPicUri);
        startActivityForResult(intent, Constants.MENU_CAMERA);
    }

    /**
     * 创建原图像保存的文件
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "IMAGE_" + new SimpleDateFormat("HHmmss").format(new Date());
        File pictureDirOri = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
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
                        return Luban.with(getActivity()).load(list).setTargetDir(getPath()).get();
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
        getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * 跳转查看大图页面
     *
     * @param position
     */
    private void imageWall(int position) {
        ArrayList<String> imageUrlList = new ArrayList<>();//传给查看大图页面的集合
        Intent intent = new Intent(getActivity(), ImageWallActivity.class);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        FileUtils.deleteAllFiles(new File(FileUtils.TEMP));
        FileUtils.deleteAllFiles(new File(FileUtils.CAMERA));
    }

    private void deleteDialog(final String itemUrl) {
        DiaLogUtils diaLogUtils = DiaLogUtils.creatDiaLog(getActivity());
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
            btnAskPrice.setEnabled(true);
            btnAskPrice.setBackground(getResources().getDrawable(R.drawable.bg_loan));
            ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
        } else {
            btnAskPrice.setEnabled(false);
            btnAskPrice.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            ll_btn.setBackground(null);
        }
    }

    private void checkInputData() {
        if(etHouseName.getText().toString().trim().length() > 0 && addImgList.size() > 1) {
            setBtnState(true);
        } else {
            setBtnState(false);
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
                        artificialAskPrice(dataBean.getFileList());
                    } else {
                        dissDialog();
                    }
                } else {
                    dissDialog();
                }
                break;
            case Task.ARTIFICIAL_ASK_PRICE:
                dissDialog();
                if (handlerRequestErr(data)) {
                    ArtificialAskPriceResultBean dataBean = (ArtificialAskPriceResultBean) data.getBody();
                    if (dataBean != null) {
                        EventBus.getDefault().post(new MessageEvent(Constants.GET_REFRESH_ORDER_LIST));
                        Bundle bd = new Bundle();
                        bd.putString("estate_name", dataBean.getEstateName());
                        bd.putString("inquiryCode", dataBean.getInquiryCode());
                        goPage(AskPriceResultActivity.class, bd);
                        getActivity().finish();
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
        checkPermission(new BaseFragment.CheckPermListener() {
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
        showDialog("正在提交询价，请稍后");
        RequestParams params = new RequestParams(UrlConfig.URL_UPLOAD_ZIP_FILE);
        params.setHttpType(HttpType.UPLOAD);
        params.setContentType("application/octet-stream");
        params.setFormName("zip");
        params.add("zip", zipPath);
        startRequest(Task.UPLOAD_ZIP_FILE, params, UploadMultipleImgResult.class, false);
    }

    private void artificialAskPrice(List<UploadMultipleImgResult.FileListBean> dataFileList) {
        List<PicDataBean> pathList = new ArrayList<>();
        for(UploadMultipleImgResult.FileListBean fileBean : dataFileList) {
            if(!StringUtils.isEmpty(fileBean.getFilePath())) {
                PicDataBean picBean = new PicDataBean();
                picBean.setUrl(fileBean.getFilePath());
                pathList.add(picBean);
            }
        }
        RequestParams params = new RequestParams(UrlConfig.URL_ARTIFICIAL_ASK_PRICE);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", UserManager.getUserId());
        params.add("estateName", etHouseName.getText().toString());
        params.add("propertyCertUrl", pathList);
        startRequest(Task.ARTIFICIAL_ASK_PRICE, params, ArtificialAskPriceResultBean.class, false);
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
}
