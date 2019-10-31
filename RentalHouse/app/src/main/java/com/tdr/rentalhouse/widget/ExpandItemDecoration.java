package com.tdr.rentalhouse.widget;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tdr.rentalhouse.adapter.ExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Li Bin on 2019/7/9 13:24
 * Description：
 */
public class ExpandItemDecoration extends RecyclerView.ItemDecoration {


    private class Section {
        public int startPos = 0;
        public int endPos = 0;

        public int getCount() {
            return endPos - startPos + 1;
        }

        public boolean contains(int pos) {
            return pos >= startPos && pos <= endPos;
        }

        @Override
        public String toString() {
            return "Section{" +
                    "startPos=" + startPos +
                    ", endPos=" + endPos +
                    '}';
        }
    }

    private float gapHorizontalDp;
    private float gapVerticalDp;
    private float sectionEdgeHPaddingDp;
    private float sectionEdgeVPaddingDp;
    private int gapHSizePx = -1;
    private int gapVSizePx = -1;
    private int sectionEdgeHPaddingPx;
    private int sectionEdgeVPaddingPx;
    private List<Section> mSectionList = new ArrayList<>();
    private ExpandableItemAdapter mAdapter;
    private int eachItemHPaddingPx;
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            markSections();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            markSections();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            markSections();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            markSections();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            markSections();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            markSections();
        }
    };


    /**
     * @param gapHorizontalDp       item之间的水平间距
     * @param gapVerticalDp         item之间的垂直间距
     * @param sectionEdgeHPaddingDp section左右两端的padding大小
     * @param sectionEdgeVPaddingDp section上下两端的padding大小
     */
    public ExpandItemDecoration(float gapHorizontalDp, float gapVerticalDp, float sectionEdgeHPaddingDp, float sectionEdgeVPaddingDp) {
        this.gapHorizontalDp = gapHorizontalDp;
        this.gapVerticalDp = gapVerticalDp;
        this.sectionEdgeHPaddingDp = sectionEdgeHPaddingDp;
        this.sectionEdgeVPaddingDp = sectionEdgeVPaddingDp;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager && parent.getAdapter() instanceof ExpandableItemAdapter) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            ExpandableItemAdapter adapter = (ExpandableItemAdapter) parent.getAdapter();
            if (mAdapter != adapter) {
                setUpWithAdapter(adapter);
            }
            int spanCount = layoutManager.getSpanCount();
            int position = parent.getChildAdapterPosition(view);
            MultiItemEntity entity = adapter.getItem(position);

            if (entity != null && entity.getItemType() == ExpandableItemAdapter.TYPE_TITLE) {
                //不处理header
                outRect.set(0, 0, 0, 0);
//                Log.w("GridAverageGapItem", "pos=" + position + "," + outRect.toShortString());
                return;
            }

            Section section = findSectionLastItemPos(position);

            if (gapHSizePx < 0 || gapVSizePx < 0) {
                transformGapDefinition(parent, spanCount);
            }
            outRect.top = gapVSizePx;
            outRect.bottom = 0;

            //下面的visualPos为单个Section内的视觉Pos
            int visualPos = position + 1 - section.startPos;

            if (visualPos % spanCount == 1) {
                //第一列
                outRect.left = sectionEdgeHPaddingPx;
                outRect.right = eachItemHPaddingPx - sectionEdgeHPaddingPx;
            } else if (visualPos % spanCount == 0) {
                //最后一列
                outRect.left = eachItemHPaddingPx - sectionEdgeHPaddingPx;
                outRect.right = sectionEdgeHPaddingPx;
            } else {
                outRect.left = (visualPos % spanCount - 1) * (eachItemHPaddingPx - sectionEdgeHPaddingPx * 2) / (spanCount - 1) + sectionEdgeHPaddingPx;
                outRect.right = eachItemHPaddingPx - outRect.left;
            }

            if (visualPos - spanCount <= 0) {
                //第一行
                outRect.top = sectionEdgeVPaddingPx;
            }

            if (isLastRow(visualPos, spanCount, section.getCount())) {
                //最后一行
                outRect.bottom = sectionEdgeVPaddingPx;
//                Log.w("GridAverageGapItem", "last row pos=" + position);
            }

            if (visualPos - spanCount <= 0) {
                //第一行
                outRect.top = sectionEdgeVPaddingPx;
            }

            if (isLastRow(visualPos, spanCount, section.getCount())) {
                //最后一行
                outRect.bottom = sectionEdgeVPaddingPx;
//                Log.w("GridAverageGapItem", "last row pos=" + position);
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    private void setUpWithAdapter(ExpandableItemAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
        markSections();
    }

    private void markSections() {
        if (mAdapter != null) {
            ExpandableItemAdapter adapter = mAdapter;
            mSectionList.clear();
            MultiItemEntity sectionEntity = null;
            Section section = new Section();
            for (int i = 0, size = adapter.getItemCount(); i < size; i++) {
                sectionEntity = adapter.getItem(i);
                if (sectionEntity != null && sectionEntity.getItemType() == ExpandableItemAdapter.TYPE_TITLE) {
                    //找到新Section起点
                    if (i != 0) {
                        //已经有待添加的section
                        section.endPos = i - 1;
                        mSectionList.add(section);
                    }
                    section = new Section();
                    section.startPos = i + 1;
                } else {
                    section.endPos = i;
                }
            }
            //处理末尾情况
            if (!mSectionList.contains(section)) {
                mSectionList.add(section);
            }

//            Log.w("GridAverageGapItem", "section list=" + mSectionList);
        }
    }

    private void transformGapDefinition(RecyclerView parent, int spanCount) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        parent.getDisplay().getMetrics(displayMetrics);
        gapHSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gapHorizontalDp, displayMetrics);
        gapVSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gapVerticalDp, displayMetrics);
        sectionEdgeHPaddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sectionEdgeHPaddingDp, displayMetrics);
        sectionEdgeVPaddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sectionEdgeVPaddingDp, displayMetrics);
        eachItemHPaddingPx = (gapHSizePx * (spanCount - 1) + sectionEdgeHPaddingPx * 2) / spanCount;
    }

    private Section findSectionLastItemPos(int curPos) {
        for (Section section : mSectionList) {
            if (section.contains(curPos)) {
                return section;
            }
        }
        return null;
    }

    private boolean isLastRow(int visualPos, int spanCount, int sectionItemCount) {
        int lastRowCount = sectionItemCount % spanCount;
        lastRowCount = lastRowCount == 0 ? spanCount : lastRowCount;
        return visualPos > sectionItemCount - lastRowCount;
    }
}
