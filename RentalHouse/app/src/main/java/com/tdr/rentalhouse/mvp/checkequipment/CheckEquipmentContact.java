package com.tdr.rentalhouse.mvp.checkequipment;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Query;

/**
 * Author：Li Bin on 2019/7/30 08:51
 * Description：
 */
public class CheckEquipmentContact {
    interface Presenter extends BasePresenter<BaseView> {
        void upload(int what, Map<String, RequestBody> map);

        void installEquipment(int what, Map<String, Object> map);

        void isEquipmentBind(int what, Long equipmentNumber, Long equipmentType,int businessTye,int floorId, int roomId);

        void deviceType(int what, String equipmentNumber,String equipmentType,int unitId);

        void installDevice(int what, Map<String, Object> map);


    }
}


