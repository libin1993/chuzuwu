package com.tdr.rentalhouse.mvp.installotherdevice;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;

/**
 * Author：Libin on 2020-03-19 18:15
 * Email：1993911441@qq.com
 * Describe：
 */
public class OtherDeviceContact {
    interface Presenter extends BasePresenter<BaseView> {
        void deviceName(int what,String code);
        void installOtherDevice(int what, Map<String, Object> map, ArrayList<String> list);

        void upload(int what, Map<String, RequestBody> map);
    }
}
