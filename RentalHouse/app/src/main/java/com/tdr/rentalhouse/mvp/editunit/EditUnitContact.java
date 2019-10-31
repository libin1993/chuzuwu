package com.tdr.rentalhouse.mvp.editunit;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;

/**
 * Author：Li Bin on 2019/7/22 16:15
 * Description：
 */
public class EditUnitContact {

    interface Presenter extends BasePresenter<BaseView> {
        void editUnit(int what, Map<String, Object> map);
    }
}
