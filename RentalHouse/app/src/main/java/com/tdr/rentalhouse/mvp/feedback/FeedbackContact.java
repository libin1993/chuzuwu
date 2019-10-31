package com.tdr.rentalhouse.mvp.feedback;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/23 17:15
 * Description：
 */
public class FeedbackContact {
    interface Presenter extends BasePresenter<BaseView> {
        void feedback(int what, String content);
    }

}
