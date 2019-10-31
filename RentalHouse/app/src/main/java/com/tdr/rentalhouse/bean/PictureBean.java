package com.tdr.rentalhouse.bean;

/**
 * Author：Li Bin on 2019/7/23 08:54
 * Description：
 */
public class PictureBean {
    private String img;
    private boolean isUrl;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isUrl() {
        return isUrl;
    }

    public PictureBean(String img, boolean isUrl) {
        this.img = img;
        this.isUrl = isUrl;
    }

    public void setUrl(boolean url) {
        isUrl = url;
    }
}
