package com.tdr.rentalhouse.bean;

/**
 * Author：Li Bin on 2019/7/26 15:17
 * Description：
 */
public class ActionBean {
    private String time;
    private int orientation;
    private int height;

    public ActionBean(String time, int orientation, int height) {
        this.time = time;
        this.orientation = orientation;
        this.height = height;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ActionBean() {
    }
}
