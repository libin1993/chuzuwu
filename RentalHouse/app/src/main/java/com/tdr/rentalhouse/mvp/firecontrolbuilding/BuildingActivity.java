package com.tdr.rentalhouse.mvp.firecontrolbuilding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.BuildingBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.mvp.devicelist.DeviceListActivity;
import com.tdr.rentalhouse.mvp.house.ManageHouseActivity;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Libin on 2020-03-17 14:37
 * Email：1993911441@qq.com
 * Describe：消防报装 楼栋信息
 */
public class BuildingActivity extends BaseMvpActivity<BuildingContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.rv_manager_address)
    RecyclerView rvUnit;
    @BindView(R.id.tv_to_community)
    TextView tvToCommunity;


    private HouseInfoBean houseInfoBean;
    private List<BuildingBean.DataBean.FloorListBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<BuildingBean.DataBean.FloorListBean, BaseViewHolder> adapter;
    private BuildingBean.DataBean dataBean;


    @Override
    protected BuildingContact.Presenter initPresenter() {
        return new BuildingPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_info);
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

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.getBuildingInfo(RequestCode.NetCode.BUILDING_INFO, houseInfoBean.getUnitId());
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("楼栋信息");
        tvToCommunity.setText("跳转楼房管理 >>");
        tvToCommunity.setVisibility(View.VISIBLE);

        rvUnit.setLayoutManager(new LinearLayoutManager(this));
        rvUnit.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12), 1));
        adapter = new BaseQuickAdapter<BuildingBean.DataBean.FloorListBean, BaseViewHolder>(R.layout.layout_rv_building_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, BuildingBean.DataBean.FloorListBean item) {
                helper.setText(R.id.tv_room_type, item.getHouseName());
                helper.setText(R.id.tv_equipment_count, item.getDeviceCount() + "");
                ImageView ivNext = helper.getView(R.id.iv_more);
                if (item.getDeviceCount() <= 0 && item.getBigRoomId() >= 0) {
                    ivNext.setVisibility(View.INVISIBLE);
                } else {
                    ivNext.setVisibility(View.VISIBLE);
                }
            }
        };
        rvUnit.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (dataList.get(position).getDeviceCount() > 0 || dataList.get(position).getBigRoomId() == -1
                        || dataList.get(position).getBigRoomId() == -2) {
                    Intent intent = new Intent(BuildingActivity.this, DeviceListActivity.class);
                    int roomId = dataList.get(position).getBigRoomId();
                    houseInfoBean.setHouseId(roomId);
                    if (roomId == -1) {
                        houseInfoBean.setInstallPosition(1);
                    } else if (roomId == -2) {
                        houseInfoBean.setInstallPosition(2);
                    }
                    houseInfoBean.setFloorName(dataList.get(position).getHouseName());
                    houseInfoBean.setHouseName(dataList.get(position).getHouseName());
                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                }
            }
        });
    }

    private void addHeader() {
        adapter.removeAllHeaderView();

        View view = LayoutInflater.from(this).inflate(R.layout.layout_manager_address_header, null);
        SimpleDraweeView iv = view.findViewById(R.id.iv_estate);
        TextView tvName = view.findViewById(R.id.tv_estate_name);
        TextView tvAddress = view.findViewById(R.id.tv_estate_address);
        tvAddress.setVisibility(View.GONE);
        iv.setImageURI(Api.IMG_HOST + dataBean.getOutLookOne());
        tvName.setText(dataBean.getAreaName());


        adapter.addHeaderView(view);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(int what, Object object) {
        dataList.clear();

        dataBean = (BuildingBean.DataBean) object;

        if (dataBean != null) {
            addHeader();
            if (ObjectUtils.getInstance().isNotNull(dataBean.getFloorList())) {
                dataList.addAll(dataBean.getFloorList());
            }
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFail(int what, String msg) {
        ToastUtils.getInstance().showToast(msg);

    }

    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }

    @OnClick({R.id.iv_title_back, R.id.tv_to_community})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_to_community:
                if (dataBean !=null){
                    Intent intent1 = new Intent(BuildingActivity.this, ManageHouseActivity.class);
                    HouseInfoBean houseInfo = new HouseInfoBean();
                    houseInfo.setBuildingType(houseInfoBean.getBuildingType());
                    houseInfo.setCommunityId(houseInfoBean.getCommunityId());
                    houseInfo.setCommunityName(houseInfoBean.getCommunityName());
                    houseInfo.setBuildingName(houseInfoBean.getBuildingName());
                    houseInfo.setUnitId(houseInfoBean.getUnitId());
                    houseInfo.setUnitName(houseInfoBean.getUnitName());
                    houseInfo.setImg(houseInfoBean.getImg());
                    houseInfo.setType(0);
                    houseInfo.setGuid(dataBean.getGuid());
                    intent1.putExtra("house", houseInfo);
                    startActivity(intent1);
                }

                break;
        }
    }
}
