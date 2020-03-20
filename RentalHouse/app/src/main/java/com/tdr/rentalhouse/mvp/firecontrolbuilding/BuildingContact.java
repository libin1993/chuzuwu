package com.tdr.rentalhouse.mvp.firecontrolbuilding;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Libin on 2020-03-17 14:38
 * Email：1993911441@qq.com
 * Describe：
 */
public class BuildingContact {
    interface Presenter extends BasePresenter<BaseView> {
        void getBuildingInfo(int what, int id);
    }
}
