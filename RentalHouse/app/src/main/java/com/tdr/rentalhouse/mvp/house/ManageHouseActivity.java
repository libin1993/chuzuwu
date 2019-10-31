package com.tdr.rentalhouse.mvp.house;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.mvp.houseinfo.HouseInfoActivity;
import com.tdr.rentalhouse.mvp.roomlist.RoomInfoActivity;
import com.tdr.rentalhouse.mvp.addroom.AddRoomActivity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.FloorBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.SectionBean;
import com.tdr.rentalhouse.bean.SectionItem;
import com.tdr.rentalhouse.mvp.bindhouse.BindHouseActivity;
import com.tdr.rentalhouse.mvp.editroom.EditRoomActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.SectionItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/12 17:55
 * Description：
 */
public class ManageHouseActivity extends BaseMvpActivity<ManageHouseContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_title_more)
    ImageView ivTitleMore;
    @BindView(R.id.tv_unit_name)
    TextView tvUnitName;
    @BindView(R.id.rv_unit)
    RecyclerView rvUnit;

    private HouseInfoBean houseInfoBean;

    private BaseSectionQuickAdapter<SectionBean, BaseViewHolder> adapter;
    private List<SectionBean> dataList = new ArrayList<>();

    @Override
    protected ManageHouseContact.Presenter initPresenter() {
        return new ManageHousePresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_house);
        ButterKnife.bind(this);
        getData();
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.getFloor(RequestCode.NetCode.GET_FLOOR, houseInfoBean.getUnitId());
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("楼房管理");
        if (houseInfoBean.getType() != 1) {
            ivTitleMore.setVisibility(View.VISIBLE);
        }


        String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢";
        if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
            address += "/" + houseInfoBean.getUnitName() + "单元";
        }
        tvUnitName.setText(address);


        rvUnit.setLayoutManager(new GridLayoutManager(this, 4));
        rvUnit.addItemDecoration(new SectionItemDecoration(12, 12, 12, 12));
        adapter = new BaseSectionQuickAdapter<SectionBean, BaseViewHolder>(R.layout.layout_manager_house_item,
                R.layout.layout_rv_floor_item, dataList) {

            @Override
            protected void convert(BaseViewHolder helper, SectionBean item) {
                SectionItem t = item.t;
                helper.setText(R.id.tv_room_no, t.name + "室");
                helper.addOnClickListener(R.id.tv_room_no);
            }

            @Override
            protected void convertHead(BaseViewHolder helper, SectionBean item) {
                EasySwipeMenuLayout swipeMenuLayout = helper.getView(R.id.esm_layout);
                if (houseInfoBean.getType() == 1) {
                    swipeMenuLayout.setCanLeftSwipe(false);
                    swipeMenuLayout.setCanRightSwipe(false);
                }


                helper.setVisible(R.id.view_item_line, helper.getAdapterPosition() > 0);
                helper.setText(R.id.tv_left, item.header + "层");
                helper.addOnClickListener(R.id.tv_edit_house);

            }
        };
        rvUnit.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (FastClickUtils.isSingleClick()) {
                    switch (view.getId()) {
                        case R.id.tv_edit_house:
                            Intent editIntent = new Intent(ManageHouseActivity.this, EditRoomActivity.class);
                            List<SectionItem> itemList = new ArrayList<>();
                            for (int i = position + 1; i < dataList.size(); i++) {
                                if (dataList.get(i).isHeader) {
                                    break;
                                }
                                SectionItem sectionItem = dataList.get(i).t;
                                itemList.add(new SectionItem(sectionItem.id, sectionItem.name, sectionItem.header));
                            }
                            editIntent.putExtra("house", (Serializable) itemList);
                            editIntent.putExtra("community_id",houseInfoBean.getCommunityId());
                            editIntent.putExtra("unit_id",houseInfoBean.getUnitId());
                            startActivity(editIntent);
                            break;
                        case R.id.tv_room_no:
                            if (houseInfoBean.getType() == 1) {
                                Intent intent = new Intent(ManageHouseActivity.this, RoomInfoActivity.class);
                                houseInfoBean.setHouseId(dataList.get(position).t.id);
                                houseInfoBean.setFloorName(dataList.get(position).t.header);
                                houseInfoBean.setHouseName(dataList.get(position).t.name);
                                intent.putExtra("house", houseInfoBean);
                                startActivity(intent);
                            } else {
                                houseInfoBean.setFloorName(dataList.get(position).t.header);
                                houseInfoBean.setHouseName(dataList.get(position).t.name);
                                LoadingUtils.getInstance().showLoading(ManageHouseActivity.this, "加载中");
                                mPresenter.getHouseInfo(RequestCode.NetCode.HOUSE_INFO, dataList.get(position).t.id);
                            }

                            break;
                    }
                }
            }
        });

    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_more})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()) {
            switch (view.getId()) {
                case R.id.iv_title_back:
                    finish();
                    break;
                case R.id.iv_title_more:
                    Intent intent = new Intent(ManageHouseActivity.this, AddRoomActivity.class);
                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);

                    break;
            }
        }

    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.GET_FLOOR:
                dataList.clear();
                List<FloorBean.DataBean.FloorListBean> floorList = (List<FloorBean.DataBean.FloorListBean>) object;
                if (ObjectUtils.getInstance().isNotNull(floorList)) {
                    for (FloorBean.DataBean.FloorListBean floorListBean : floorList) {
                        dataList.add(new SectionBean(true, floorListBean.getFloorName()));
                        if (ObjectUtils.getInstance().isNotNull(floorListBean.getHouseList())) {
                            for (FloorBean.DataBean.FloorListBean.HouseListBean houseListBean : floorListBean.getHouseList()) {
                                SectionItem sectionItem = new SectionItem(houseListBean.getId(), houseListBean.getHouseName(),
                                        floorListBean.getFloorName());
                                dataList.add(new SectionBean(sectionItem));
                            }
                        }

                    }

                    adapter.notifyDataSetChanged();
                }
                break;
            case RequestCode.NetCode.HOUSE_INFO:
                HouseBean.DataBean houseBean = (HouseBean.DataBean) object;
                if (houseBean != null) {
                    Intent intent;
                    if (TextUtils.isEmpty(houseBean.getLandlordName())) {
                        intent = new Intent(ManageHouseActivity.this, BindHouseActivity.class);
                    } else {
                        intent = new Intent(ManageHouseActivity.this, HouseInfoActivity.class);
                    }

                    houseInfoBean.setHouseId(houseBean.getId());

                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void onFail(int what, String msg) {
        ToastUtils.getInstance().showToast(msg);
    }

    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }


}
