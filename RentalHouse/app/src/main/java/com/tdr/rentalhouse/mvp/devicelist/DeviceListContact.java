package com.tdr.rentalhouse.mvp.devicelist;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Libin on 2020-03-19 10:46
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceListContact {
    interface Presenter extends BasePresenter<BaseView> {
        void getSelfBuildingDevice(int what, int id,int currentPage);

        void getBuildingDevice(int what, int id,int unitId,int currentPage);

        void deleteDevice(int what,int bindId);
    }
}
