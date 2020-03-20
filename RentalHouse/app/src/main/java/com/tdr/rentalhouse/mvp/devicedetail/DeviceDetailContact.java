package com.tdr.rentalhouse.mvp.devicedetail;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import io.reactivex.Observable;
import retrofit2.http.Field;

/**
 * Author：Libin on 2020-03-19 17:36
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceDetailContact {
    interface Presenter extends BasePresenter<BaseView> {
        void deviceDetail(int what,int bindId);
        void editDeviceName(int what,int bindId, String deviceName);
    }
}
