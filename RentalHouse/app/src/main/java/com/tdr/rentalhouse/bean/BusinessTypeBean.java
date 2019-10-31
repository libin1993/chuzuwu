package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/9/30 14:17
 * Description：
 */
public class BusinessTypeBean extends BaseBean<List<BusinessTypeBean.DataBean>> {


    public static class DataBean {
        /**
         * ShowName : 出租屋
         * BusinessType : 1
         */

        private String ShowName;
        private int BusinessType;

        public String getShowName() {
            return ShowName;
        }

        public void setShowName(String ShowName) {
            this.ShowName = ShowName;
        }

        public int getBusinessType() {
            return BusinessType;
        }

        public void setBusinessType(int BusinessType) {
            this.BusinessType = BusinessType;
        }
    }
}
