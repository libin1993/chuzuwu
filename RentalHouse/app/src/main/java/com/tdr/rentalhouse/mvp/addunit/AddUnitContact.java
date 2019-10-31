package com.tdr.rentalhouse.mvp.addunit;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

/**
 * Author：Li Bin on 2019/7/22 15:35
 * Description：
 */
public class AddUnitContact {

    interface Presenter extends BasePresenter<BaseView> {
        void addUnit(int what, Map<String, Object> map);
    }
}
