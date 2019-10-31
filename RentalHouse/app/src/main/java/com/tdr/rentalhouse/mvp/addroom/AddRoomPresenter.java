package com.tdr.rentalhouse.mvp.addroom;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/23 13:02
 * Description：
 */
public class AddRoomPresenter extends BasePresenterImpl<BaseView> implements AddRoomContact.Presenter {
    @Override
    public void addRoom(final int what, long id, long unitId, String list) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .addRoom(id,unitId,list)
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
