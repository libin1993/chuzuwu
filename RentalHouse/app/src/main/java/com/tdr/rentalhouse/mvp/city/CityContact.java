package com.tdr.rentalhouse.mvp.city;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author：Li Bin on 2019/8/9 13:19
 * Description：
 */
public class CityContact {

    public interface Presenter extends BasePresenter<BaseView> {
        void getCityList(int what, String code);

    }

}
