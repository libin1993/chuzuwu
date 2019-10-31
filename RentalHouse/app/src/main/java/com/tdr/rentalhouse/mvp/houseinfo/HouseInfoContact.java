package com.tdr.rentalhouse.mvp.houseinfo;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/24 16:14
 * Description：
 */
public class HouseInfoContact {
    interface Presenter extends BasePresenter<BaseView> {
        void getHouseInfo(int what, int id);
    }
}
