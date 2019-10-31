package com.tdr.rentalhouse.mvp.addaddress;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author：Li Bin on 2019/7/19 13:22
 * Description：
 */
public class AddAddressContact {

    interface Presenter extends BasePresenter<BaseView> {
        void upload(int what, Map<String, RequestBody> map);

        void addAddress(int what, Map<String, Object> map);


        void editAddress(int what, Map<String, Object> map);

        void getHouseInfo(int what, int id);
    }
}
