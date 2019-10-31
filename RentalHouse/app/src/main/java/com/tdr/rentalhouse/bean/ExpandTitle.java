package com.tdr.rentalhouse.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.tdr.rentalhouse.adapter.ExpandableItemAdapter.TYPE_TITLE;

/**
 * Author：Li Bin on 2019/7/9 09:48
 * Description：
 */
public class ExpandTitle extends AbstractExpandableItem<ExpandItem> implements MultiItemEntity {
    public String title;

    public ExpandTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TYPE_TITLE;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
