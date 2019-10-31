package com.tdr.rentalhouse.utils;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.search.SearchRequest;

/**
 * Author：Li Bin on 2019/7/9 17:09
 * Description：蓝牙
 */
public class ClientManager {
    private static BluetoothClient mClient;
    private static SearchRequest mRequest;
    private static BleConnectOptions mOptions;


    private ClientManager() {
    }

    public static BluetoothClient getClient(Context context) {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(context);
                }
            }
        }
        return mClient;
    }

    public static SearchRequest getRequest() {
        if (mRequest == null) {
            synchronized (ClientManager.class) {
                if (mRequest == null) {
                    mRequest = new SearchRequest.Builder()
                            .searchBluetoothLeDevice(3000, 1)
                            .build();
                }
            }
        }

        return mRequest;
    }

    public static BleConnectOptions getOption() {
        if (mOptions == null) {
            synchronized (ClientManager.class) {
                if (mOptions == null) {
                    mOptions = new BleConnectOptions.Builder()
                            .setConnectRetry(3)
                            .setConnectTimeout(30000)
                            .setServiceDiscoverRetry(3)
                            .setServiceDiscoverTimeout(20000)
                            .build();
                }
            }
        }

        return mOptions;
    }
}
