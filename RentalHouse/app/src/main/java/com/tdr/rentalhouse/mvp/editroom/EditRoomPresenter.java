package com.tdr.rentalhouse.mvp.editroom;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/23 13:26
 * Description：
 */
public class EditRoomPresenter extends BasePresenterImpl<BaseView> implements EditRoomContact.Presenter {
    @Override
    public void editRoom(final int what, int id, int communityId, int unitId, String floorNo, String roomNo) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .editRoom(id, communityId, unitId, floorNo, roomNo)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void deleteHouse(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deleteHouse(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}
