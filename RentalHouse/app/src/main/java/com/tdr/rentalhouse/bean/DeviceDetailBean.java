package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BaseView;

import java.util.List;

/**
 * Author：Libin on 2020-03-19 12:50
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceDetailBean extends BaseBean<DeviceDetailBean.DataBean> {

    public static class DataBean {
        /**
         * DeviceIcon : /upload/top-bg-one.982a0cc.png
         * DeviceName : AI烟感
         * DeviceCode : 4285555
         * DeviceType : AI烟感
         * DeviceTime : 2020-03-18
         */

        private List<String> DeviceIcon;
        private String DeviceName;
        private String DeviceCode;
        private String DeviceType;
        private String DeviceTime;


        public List<String> getDeviceIcon() {
            return DeviceIcon;
        }

        public void setDeviceIcon(List<String> deviceIcon) {
            DeviceIcon = deviceIcon;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getDeviceCode() {
            return DeviceCode;
        }

        public void setDeviceCode(String DeviceCode) {
            this.DeviceCode = DeviceCode;
        }

        public String getDeviceType() {
            return DeviceType;
        }

        public void setDeviceType(String DeviceType) {
            this.DeviceType = DeviceType;
        }

        public String getDeviceTime() {
            return DeviceTime;
        }

        public void setDeviceTime(String DeviceTime) {
            this.DeviceTime = DeviceTime;
        }
    }
}
