package com.tdr.rentalhouse.utils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.tdr.rentalhouse.application.MyApplication;
import com.tdr.rentalhouse.bean.BluetoothBean;

/**
 * Author：Li Bin on 2019/7/9 17:09
 * Description：蓝牙
 */
public class BluetoothUtils {

    private static BluetoothUtils mInstance;
    private static BluetoothClient mBluetoothClient;
    private BluetoothBean bluetoothBean;


    private BluetoothUtils() {
        mBluetoothClient = new BluetoothClient(MyApplication.getInstance());
    }

    public static BluetoothUtils getInstance() {
        if (mInstance == null) {
            synchronized (BluetoothUtils.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothUtils();
                }
            }
        }
        return mInstance;
    }

    public  BluetoothClient getClient() {
        return mBluetoothClient;
    }

    public BluetoothBean getBluetoothBean() {
        return bluetoothBean;
    }

    public void setBluetoothBean(BluetoothBean bluetoothBean) {
        this.bluetoothBean = bluetoothBean;
    }

    /**
     * @return 扫描配置
     */
    public SearchRequest getRequest() {
        return new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 2)
                .build();
    }

    /**
     * @return 连接配置
     */
    public BleConnectOptions getOption() {

        return new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(30000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(20000)
                .build();
    }
}
