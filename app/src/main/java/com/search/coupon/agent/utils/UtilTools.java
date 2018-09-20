package com.search.coupon.agent.utils;

import android.content.Context;
import android.widget.Toast;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilTools {
    public static Toast toast;

    public static void toast(Context context, String str) {
        try {
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            toast = Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    //sp转px
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /***
     * 登录异常提示
     * @param context
     */
//    public static void showLoginErrDialog(final Context context, String error) {
//        DiaLogUtils dialog = DiaLogUtils.creatDiaLog(context);
//        dialog.setContent(error).setCancelButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserUtils.loginOut();
//                BaseApplication.getApplication().removeAllActivity();
//                Bundle bd = new Bundle();
//                bd.putBoolean(Ikeys.KEY_LOGIN_CAN_BACK, false);
//                goPage(context, Login_Activity.class, bd);
//                dialog.destroyDialog();
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.showDialog();
//    }

    /***
     * 跳转到指定页面
     *
     * @param clas        指定页面
     * @param data        传入数据
     */
//    public static void goPage(Context context, Class<? extends BaseActivity> clas, Bundle data) {
//        if (clas == null) {
//            return;
//        }
//        Intent intent = new Intent(context, clas);
//        if (data != null) {
//            intent.putExtra(Ikeys.KEY_DATA, data);
//        }
//        context.startActivity(intent);
//    }

    /**
     * 密码由数字和字母组成
     *
     * @param password 登录输入的密码
     * @return 密码是否符合规则
     */
    public static boolean checkPassWordInput(String password) {
        if (password != null && password.trim().length() > 0) {
            Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{6,12}$");
            Matcher matcher = p.matcher(password);
            if (!matcher.matches()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

}
