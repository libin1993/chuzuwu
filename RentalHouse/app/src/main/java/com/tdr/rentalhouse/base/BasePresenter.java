package com.tdr.rentalhouse.base;

/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：
 */
public interface BasePresenter<T extends BaseView> {
    void attach(T view);

    void detach();
}
