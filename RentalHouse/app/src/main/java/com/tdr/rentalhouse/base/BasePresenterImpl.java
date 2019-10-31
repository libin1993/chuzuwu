package com.tdr.rentalhouse.base;


/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：Presenter生命周期包装、View的绑定和解除，P层实现的基类
 */
public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    private V mView;

    protected V getView() {
        return mView;
    }


    protected boolean isViewAttached() {
        return mView != null ;
    }

    @Override
    public void attach(V view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
