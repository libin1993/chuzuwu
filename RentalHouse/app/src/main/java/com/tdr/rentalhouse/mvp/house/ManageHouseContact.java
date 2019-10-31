package com.tdr.rentalhouse.mvp.house;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/23 11:11
 * Description：
 */
public class ManageHouseContact {

    interface Presenter extends BasePresenter<BaseView> {
        void getFloor(int what, int id);
        void getHouseInfo(int what, int id);
    }
}
