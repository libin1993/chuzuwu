package com.tdr.rentalhouse.bean;

import java.io.Serializable;

/**
 * Author：Li Bin on 2019/7/8 16:57
 * Description：
 */
public class SectionItem implements Serializable {
    public int id;
    public String name;
    public String header;

    public SectionItem(int id, String name, String header) {
        this.id = id;
        this.name = name;
        this.header = header;
    }

}

