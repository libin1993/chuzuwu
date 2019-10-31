package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/18 16:54
 * Description：
 */
public class AddressBean extends BaseBean<List<AddressBean.DataBean>> {

    public static class DataBean {
        private String Name;
        private String Pid;
        private String UnionCode;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPid() {
            return Pid;
        }

        public void setPid(String pid) {
            Pid = pid;
        }

        public String getUnionCode() {
            return UnionCode;
        }

        public void setUnionCode(String unionCode) {
            UnionCode = unionCode;
        }
    }
}
