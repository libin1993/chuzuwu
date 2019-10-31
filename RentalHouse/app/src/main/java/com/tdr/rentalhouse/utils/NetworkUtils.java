package com.tdr.rentalhouse.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tdr.rentalhouse.application.MyApplication;


/**
 * Author：Libin on 2019/5/31 16:50
 * Email：1993911441@qq.com
 * Describe：网络状况
 */
public class NetworkUtils {
    private static NetworkUtils mInstance;

    private NetworkUtils() {

    }

    public static NetworkUtils getInstance() {
        if (mInstance == null) {
            synchronized (NetworkUtils.class) {
                if (mInstance == null) {
                    mInstance = new NetworkUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        return networkinfo != null && networkinfo.isAvailable();
    }
}
