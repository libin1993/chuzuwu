package com.tdr.rentalhouse.mvp.selectaddress;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import retrofit2.http.Query;

/**
 * Author：Li Bin on 2019/7/22 13:12
 * Description：
 */
public class SelectAddressContact {

    interface Presenter extends BasePresenter<BaseView> {
        void findAddress(int what, Map<String, Object> map);


        void getHouseInfo(int what, int id);

        void getCommunityInfo(int what, int id);
    }
}
