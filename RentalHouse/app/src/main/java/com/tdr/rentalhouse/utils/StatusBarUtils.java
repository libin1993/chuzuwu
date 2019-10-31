package com.tdr.rentalhouse.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tdr.rentalhouse.application.MyApplication;

/**
 * Author：Libin on 2019/6/6 15:08
 * Description：
 */
public class StatusBarUtils {
    private static StatusBarUtils mInstance;

    private StatusBarUtils() {
    }

    public static StatusBarUtils getInstance() {
        if (mInstance == null) {
            synchronized (StatusBarUtils.class) {
                if (mInstance == null) {
                    mInstance = new StatusBarUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置状态栏栏高度
     *
     * @param view
     */
    public void setStatusBarHeight(View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        view.setLayoutParams(layoutParams);
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }


    /**
     * @return 屏幕宽度
     */
    public int getScreenWidth(Activity activity) {

        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return metric.widthPixels;
    }


    /**
     * @return 屏幕高度
     */
    public int getScreenHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return metric.heightPixels;
    }


    public void setNoStatusBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }


    /**
     * 设置屏幕的背景透明度
     *
     * @param
     */
    public void setBackgroundAlpha(Context mContext, boolean isShow) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        if (isShow) {
            lp.alpha = 0.4f;
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            lp.alpha = 1.0f;
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        ((Activity) mContext).getWindow().setAttributes(lp);

    }


}
