package com.tdr.rentalhouse.bean;

import java.io.Serializable;
import java.util.UUID;

/**
 * Author：Li Bin on 2019/7/18 10:07
 * Description：
 */
public class BluetoothBean implements Serializable {
    private String name;
    private String address;
    private UUID serviceUUID;
    private UUID writeCUUID;
    private UUID notifyCUUID;
    private UUID readCUUID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "BluetoothBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", serviceUUID=" + serviceUUID +
                ", writeCUUID=" + writeCUUID +
                ", notifyCUUID=" + notifyCUUID +
                ", readCUUID=" + readCUUID +
                '}';
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(UUID serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public UUID getWriteCUUID() {
        return writeCUUID;
    }

    public void setWriteCUUID(UUID writeCUUID) {
        this.writeCUUID = writeCUUID;
    }

    public UUID getNotifyCUUID() {
        return notifyCUUID;
    }

    public void setNotifyCUUID(UUID notifyCUUID) {
        this.notifyCUUID = notifyCUUID;
    }

    public UUID getReadCUUID() {
        return readCUUID;
    }

    public void setReadCUUID(UUID readCUUID) {
        this.readCUUID = readCUUID;
    }
}
