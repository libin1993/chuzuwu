package com.tdr.rentalhouse.mvp.scancode;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/30 16:36
 * Description：
 */
public class ScanCodeContact {
    interface Presenter extends BasePresenter<BaseView> {
        void getHouseInfo(int what, int id);

        void scanCode(int what, String code,String areaCode);
    }
}
