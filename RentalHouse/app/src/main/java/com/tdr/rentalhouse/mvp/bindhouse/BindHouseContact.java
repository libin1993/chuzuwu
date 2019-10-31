package com.tdr.rentalhouse.mvp.bindhouse;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import retrofit2.http.Field;

/**
 * Author：Li Bin on 2019/7/23 14:28
 * Description：
 */
public class BindHouseContact {
    interface Presenter extends BasePresenter<BaseView> {
        void bindHouse(int what, int floorId, String landlordName, String idNo, String phone, int businessTye,String code,
                       String areaCode, String qrCode, String lng, String lat, String list);

        void  getBusinessType(int what);

    }
}
