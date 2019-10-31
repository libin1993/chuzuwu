package com.tdr.rentalhouse.base;

/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：
 */
public interface BaseView {

    void onSuccess(int what, Object object);

    void onFail(int what, String msg);

    void hideLoading();

}
