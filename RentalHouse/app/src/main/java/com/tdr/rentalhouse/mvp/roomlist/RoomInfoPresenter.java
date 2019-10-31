package com.tdr.rentalhouse.mvp.roomlist;

import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.RoomListBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/24 09:22
 * Description：
 */
public class RoomInfoPresenter extends BasePresenterImpl<BaseView> implements RoomInfoContact.Presenter{
    @Override
    public void roomList(final int what, int id) {
        if (!isViewAttached()) {
            return;
        }

        RetrofitUtils.getInstance()
                .getService()
                .getRoomList(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<RoomListBean>() {
                    @Override
                    public void onSuccess(RoomListBean roomListBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, roomListBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}
