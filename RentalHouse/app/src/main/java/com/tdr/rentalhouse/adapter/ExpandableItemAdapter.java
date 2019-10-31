package com.tdr.rentalhouse.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.bean.ExpandTitle;
import com.tdr.rentalhouse.bean.ExpandItem;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/9 09:59
 * Description：
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;
    private Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(Context context,List<MultiItemEntity> data) {
        super(data);
        this.mContext = context;
        addItemType(TYPE_TITLE, R.layout.layout_expand_title);
        addItemType(TYPE_ITEM, R.layout.layout_expand_item);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_TITLE:
                final ExpandTitle lv0 = (ExpandTitle) item;
                holder.setText(R.id.tv_expand_title, lv0.title+"层")
                        .setImageResource(R.id.iv_expand_title, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });

                break;
            case TYPE_ITEM:
                final ExpandItem person = (ExpandItem) item;
                LinearLayout llSection = holder.getView(R.id.ll_expand_item);
                ViewGroup.LayoutParams layoutParams = llSection.getLayoutParams();
                layoutParams.height = (StatusBarUtils.getInstance().getScreenWidth((Activity) mContext) - FormatUtils.getInstance().dp2px(12) * 5) / 4;
                llSection.setLayoutParams(layoutParams);
                holder.setText(R.id.tv_expand_item, person.name+"室");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        ToastUtils.getInstance().showToast(person.name + " parent pos: " + pos);

                    }
                });
                break;
        }
    }
}
