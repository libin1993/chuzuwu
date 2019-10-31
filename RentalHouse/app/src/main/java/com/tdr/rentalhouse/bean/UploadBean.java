package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/22 08:48
 * Description：
 */
public class UploadBean extends BaseBean<List<String>> {

    /**
     * Data : []
     * Status : 2
     * Message : 成功
     * ExtendInfo : null
     */

    private List<String> Data;

    @Override
    public List<String> getData() {
        return Data;
    }

    @Override
    public void setData(List<String> data) {
        Data = data;
    }
}
