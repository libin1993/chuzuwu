package com.tdr.rentalhouse.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.tdr.rentalhouse.adapter.ExpandableItemAdapter.TYPE_ITEM;

/**
 * Author：Li Bin on 2019/7/9 09:46
 * Description：
 */
public class ExpandItem implements MultiItemEntity {
    public ExpandItem(String name) {
        this.name = name;
    }

    public String name;

    @Override
    public int getItemType() {
        return TYPE_ITEM;
    }
}
