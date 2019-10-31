package com.tdr.rentalhouse.bean;

/**
 * Author：Libin on 2019/7/5 15:59
 * Description：
 */
public class LocationBean {
    private double lat;
    private double lng;
    private String name;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
