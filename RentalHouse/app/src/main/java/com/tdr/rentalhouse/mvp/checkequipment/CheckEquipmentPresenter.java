package com.tdr.rentalhouse.mvp.checkequipment;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.EquipmentBean;
import com.tdr.rentalhouse.bean.ScanCodeBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author：Li Bin on 2019/7/30 08:51
 * Description：
 */
public class CheckEquipmentPresenter extends BasePresenterImpl<BaseView> implements CheckEquipmentContact.Presenter {
    @Override
    public void upload(final int what, Map<String, RequestBody> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .upload(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().onSuccess(what, baseBean);
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void installEquipment(final int what, Map<String, Object> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .installEquipment(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void isEquipmentBind(final int what,Long equipmentNumber, Long equipmentType,int businessTye,int floorId,int roomId) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .isEquipmentBind(equipmentNumber,equipmentType, businessTye,floorId,roomId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<EquipmentBean>() {

                    @Override
                    public void onSuccess(EquipmentBean equipmentBean) {
                        if (!isViewAttached())
                            return;
                        getView().onSuccess(what, equipmentBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void deviceType(final int what, String equipmentNumber, String equipmentType, int unitId) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deviceType(equipmentNumber,equipmentType,unitId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<EquipmentBean>() {
                    @Override
                    public void onSuccess(EquipmentBean equipmentBean) {
                        if (!isViewAttached())
                            return;
                        getView().onSuccess(what, equipmentBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void installDevice(final int what, Map<String, Object> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .installDevice(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }


}
