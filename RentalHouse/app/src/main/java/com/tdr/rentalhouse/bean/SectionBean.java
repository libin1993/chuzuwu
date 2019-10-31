package com.tdr.rentalhouse.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

/**
 * Author：Li Bin on 2019/7/8 17:10
 * Description：
 */
public class SectionBean extends SectionEntity<SectionItem>{
    public SectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionBean(SectionItem itemBean) {
        super(itemBean);
    }
}
