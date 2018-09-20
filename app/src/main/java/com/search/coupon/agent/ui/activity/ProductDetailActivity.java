package com.search.coupon.agent.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.search.coupon.agent.R;
import com.search.coupon.agent.bean.ProductDetailBean;
import com.search.coupon.agent.common.BaseActivity;
import com.search.coupon.agent.network.RequestParams;
import com.search.coupon.agent.network.ResultData;
import com.search.coupon.agent.network.Task;
import com.search.coupon.agent.network.UrlConfig;
import com.search.coupon.agent.utils.DateUtils;
import com.search.coupon.agent.utils.DeviceUtils;
import com.search.coupon.agent.view.DownloadDialog;
import com.search.coupon.agent.view.OneButtonDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 微信群详情页
 *
 * */
public class ProductDetailActivity extends BaseActivity {

    private String uniqueId;
    @BindView(R.id.iv_pic1)
    ImageView iv_pic1;
    @BindView(R.id.tv_detail_des)
    TextView tv_detail_des;
    @BindView(R.id.tv_detail_look)
    TextView tv_detail_look;
    @BindView(R.id.tv_detail_time)
    TextView tv_detail_time;
    @BindView(R.id.tv_detail_category)
    TextView tv_detail_category;
    @BindView(R.id.tv_detail_address)
    TextView tv_detail_address;
    @BindView(R.id.tv_detail_tag)
    TextView tv_detail_tag;
    private String iamgeUrl;

    private static final int SAVE_SUCCESS = 0;           //保存图片成功
    private static final int SAVE_FAILURE = 1;          //保存图片失败
    private static final int SAVE_BEGIN = 2;           //开始保存图片
    private static final int NETWORK_SAVE_FAILURE = 3;// 网络原因 保存图片失败

    private ProductDetailBean dataBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    toast("开始保存图片...");
                    break;
                case SAVE_SUCCESS:
                    dissDialog();
                    toWeChatScanDirect();
                    toast("点击右上角--从相册选取二维码扫描");
                    break;
                case SAVE_FAILURE:
                    dissDialog();
                    toast("图片保存失败,请稍后再试...");
                    break;
                case NETWORK_SAVE_FAILURE:
                    dissDialog();
                    toast("图片保存失败,请检查网络...");
                    break;
            }
        }
    };


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_product_detail);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        uniqueId = bundle.getString("uniqueId");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setLineVisible(true);
        topBar.setRightText("分享");
        topBar.setRightVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
        getProductDetail(uniqueId);
        addLookCount(uniqueId);
    }

    @OnClick({R.id.tv_add})
    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.tv_add:                //跳转到微信
                showTipDialog();
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
        if (dataBean != null) {
            //展示邀请弹出框
            showShareDialog("http://www.vxsousuo.com/page/detail?uniqueId="+dataBean.getUniqueId(), "微信搜索", "下载APP，查找优质微信群,公众号,小程序");
        }
    }
    /**
     * 增加浏览量
     * */
    public void addLookCount(String uniqueId) {
        RequestParams params = new RequestParams(UrlConfig.URL_ADD_LOOK_COUNT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("uniqueId", uniqueId);
        params.add("type", "1");
        startRequest(Task.ADD_LOOK_COUNT, params, null);
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
     *  "userId": 1,
     "groupInfoId": 3,
     "uniqueId": "fb47093524a24a76bc237a679cf456bf",
     "groupName": "发布微信群测试搜索功能",
     "groupImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "coverImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "userWechat": "tjggtlx",
     "tags": "测试|搜索|微信群",
     "description": "这是一个微信群搜索测试发布,为了试验搜索功能是否可用,各位请勿加入.",
     "codeType": 2,
     "updateDate": 1522997899000,
     "province": "北京市",
     "city": "丰台区",
     "parentCategory": "兴趣",
     "category": "文学",
     "lookCount": 15
     * */
    private void updateDetailInfo(ProductDetailBean dataBean) {
        topBar.setTitle(dataBean.getGroupName());
        tv_detail_des.setText(dataBean.getDescription());
        tv_detail_look.setText(dataBean.getLookCount()+" 个关注");
        tv_detail_time.setText( DateUtils.formatDate(dataBean.getUpdateDate()));
        tv_detail_category.setText(dataBean.getParentCategory()+" "+dataBean.getCategory());
        tv_detail_address.setText(dataBean.getProvince()+" "+dataBean.getCity());
        tv_detail_tag.setText(dataBean.getTags());

        iamgeUrl = dataBean.getGroupImage();
        Glide.with(this).load(iamgeUrl).into(iv_pic1);
    }

    /**
     * 跳转到微信
     */
    protected void showTipDialog() {
        final OneButtonDialog dialog = new OneButtonDialog(this);
        dialog.setContent("二维码已保存到相册\n  \n打开微信扫一扫 点击右上角\n  \n从相册选取二维码扫描" );
        dialog.setOkText("知道了");
        dialog.setOnDownLoadClickListener(new DownloadDialog.OnDownLoadClickListener() {
            @Override
            public void onLeftClick() {

                dialog.dismiss();
            }

            @Override
            public void onRightClick() {
                dialog.dismiss();
                showDialog();
                //监测权限
                checkPermission(new CheckPermListener(){
                    @Override
                    public void superPermission() {
                        //保存图片必须在子线程中操作，是耗时操作
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bp = returnBitMap(iamgeUrl);
                                if(bp != null){
                                    saveImageToPhotos(ProductDetailActivity.this, bp);
                                }else{
                                    mHandler.obtainMessage(NETWORK_SAVE_FAILURE).sendToTarget();
                                }

                            }
                        }).start();
                }
            }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        });
        dialog.show();
    }

    /**
     * 保存二维码到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {

                // 首先保存图片
                File appDir = new File(Environment.getExternalStorageDirectory(), "wechatsearch");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    dissDialog();
                    mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
                    return;
                }
                // 最后通知图库更新
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();

    }

    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 跳转到微信
     */
    public void toWeChatScanDirect() {

        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 分享弹出框
     */
    protected void showShareDialog(String url,String title,String content) {
        //分享链接设置
        UMWeb web = new UMWeb(url);
        web.setTitle(title);                                                             //标题
        UMImage thumb = new UMImage(this, R.mipmap.ic_launcher);               //资源图片
        web.setThumb(thumb);                                                           //缩略图
        web.setDescription(content);                                                  //描述
        //设置分享方式，分享平台，分享回调
        new ShareAction(this) .withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(shareListener).open();
    }

    /**
     * 分享回调接口
     */
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            toast("分享成功");
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform == SHARE_MEDIA.WEIXIN ||platform == SHARE_MEDIA.WEIXIN_CIRCLE ){
                if (t.getMessage().contains("2008")){
                    toast("请先安装微信");
                }else {
                    toast("分享失败"+t.getMessage());
                }

            }else {
                toast("分享失败"+t.getMessage());
            }

        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            toast("分享已取消");
        }
    };

}
