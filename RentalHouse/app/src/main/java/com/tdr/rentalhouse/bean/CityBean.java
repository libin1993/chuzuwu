package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Li Bin on 2019/8/9 13:08
 * Description：
 */
public class CityBean extends BaseBean<CityBean.DataBean> {


    public static class DataBean implements Serializable {
        private java.util.List<ListBean> List;

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean implements Serializable {
            /**
             * UnionCode : 370801000000
             * Name : 市辖区
             */

            private String UnionCode;
            private String Name;

            public String getUnionCode() {
                return UnionCode;
            }

            public void setUnionCode(String UnionCode) {
                this.UnionCode = UnionCode;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }
    }
}
