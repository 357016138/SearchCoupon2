package com.search.coupon.agent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.search.coupon.agent.ui.activity.MainActivity;
import com.search.coupon.agent.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 *  Created by song on 2018/2/1 0030.
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            if (bundle != null) {
                JSONObject push = null;
                try {
                    push = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String pushkey = GsonUtil.getStringFromJson("push", "type");
                if(pushkey.equals("minetask")){
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }else if(pushkey.equals("push_notice")||pushkey.equals("push_msg")){
//                    Intent i = new Intent(context, ChannelHomeActivity.class);
//                    i.putExtras(bundle);
//                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String KEY : bundle.keySet()) {
//            if (KEY.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nKEY:" + KEY + ", value:" + bundle.getInt(KEY));
//            } else if (KEY.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nKEY:" + KEY + ", value:" + bundle.getBoolean(KEY));
//            } else {
//                sb.append("\nKEY:" + KEY + ", value:" + bundle.getString(KEY));
//            }
//        }
//        return sb.toString();
//    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        JSONObject jo = JsonUtils.getJsonObject(extras);
//        if (jo != null) {
//            String pushkey = JsonUtils.getString(jo, "type");
//            if (pushkey.equals("deleteinto")) {
//                String id = JsonUtils.getString(jo, "id");
//                if (!TextUtils.isEmpty(id)) {
//                    deleteInto(context, id);
//                }
//            }
//        }
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}

    }

//    private void deleteInto(final Context context, final String id) {
//        new Thread() {
//            public void run() {
//                ProductDBOperation db = ProductDBOperation.getInstance(context);
//                if (db != null) {
//                    LocalIntoData data = db.getProductById(id);
//                    if (data != null && data.annexs != null && data.annexs.size() > 0) {
//                        for (int i = 0; i < data.annexs.size(); i++) {
//                            AnnexFolderData annexFolder = data.annexs.get(i);
//                            if (annexFolder.annexs != null) {
//                                for (int j = 0; j < annexFolder.annexs.size(); j++) {
//                                    AnnexData annex = annexFolder.annexs.get(j);
//                                    File file = new File(annex.url);
//                                    if (file.exists()) {
//                                        file.delete();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    db.deleteProductByIntoID(id);
//                }
//            }
//
//            ;
//        }.start();
//    }

}
