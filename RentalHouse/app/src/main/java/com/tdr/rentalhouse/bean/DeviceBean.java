package com.tdr.rentalhouse.bean;

import java.util.List;

/**
 * Author：Libin on 2020-03-19 15:24
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceBean {
    private Object DevIcon;
    private String DeviceName;
    private String DeviceNumber;
    private String DeviceType;
    private String DeviceTypeName;
    private List<String> DevicePicture;

    public List<String> getDevicePicture() {
        return DevicePicture;
    }

    public void setDevicePicture(List<String> devicePicture) {
        DevicePicture = devicePicture;
    }

    public String getDeviceTypeName() {
        return DeviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        DeviceTypeName = deviceTypeName;
    }

    private int DeviceBindId;

    public Object getDevIcon() {
        return DevIcon;
    }

    public void setDevIcon(Object DevIcon) {
        this.DevIcon = DevIcon;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String DeviceNumber) {
        this.DeviceNumber = DeviceNumber;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public int getDeviceBindId() {
        return DeviceBindId;
    }

    public void setDeviceBindId(int DeviceBindId) {
        this.DeviceBindId = DeviceBindId;
    }
}
