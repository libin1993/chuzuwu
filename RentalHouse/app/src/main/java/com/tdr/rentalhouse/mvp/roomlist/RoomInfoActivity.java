package com.tdr.rentalhouse.mvp.roomlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.inuker.bluetooth.library.Constants;
import com.tdr.rentalhouse.mvp.checkequipment.CheckEquipmentActivity;
import com.tdr.rentalhouse.ui.ConnectBluetoothActivity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.RoomListBean;
import com.tdr.rentalhouse.utils.BluetoothUtils;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/17 17:22
 * Description：
 */
public class RoomInfoActivity extends BaseMvpActivity<RoomInfoContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_unit_name)
    TextView tvUnitName;
    @BindView(R.id.rv_unit)
    RecyclerView rvUnit;

    private HouseInfoBean houseInfoBean;
    private List<RoomListBean.DataBean.RoomEntityBean> roomList = new ArrayList<>();
    private BaseQuickAdapter<RoomListBean.DataBean.RoomEntityBean, BaseViewHolder> adapter;
    private RoomListBean.DataBean dataBean;

    @Override
    protected RoomInfoContact.Presenter initPresenter() {
        return new RoomInfoPresenter();
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

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.roomList(RequestCode.NetCode.ROOM_LIST, houseInfoBean.getHouseId());
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("房屋信息");
        if (houseInfoBean.getBuildingType() == 2 || houseInfoBean.getBuildingType() == 3) {
            String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += houseInfoBean.getUnitName() + "单元/";
            }
            address += houseInfoBean.getFloorName() + "层/" + houseInfoBean.getHouseName() + "室";
            tvUnitName.setText(address);
        } else {
            tvUnitName.setText(houseInfoBean.getAreaNumber());
        }

        rvUnit.setLayoutManager(new LinearLayoutManager(this));
        rvUnit.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12)));
        adapter = new BaseQuickAdapter<RoomListBean.DataBean.RoomEntityBean, BaseViewHolder>(R.layout.layout_room_item_status, roomList) {
            @Override
            protected void convert(BaseViewHolder helper, RoomListBean.DataBean.RoomEntityBean item) {
                helper.setText(R.id.tv_no_room, item.getRoomNumber());
                TextView tvNoEquipment = helper.getView(R.id.tv_no_equipment);

                TextView tvCode = helper.getView(R.id.tv_code_room);

                if (item.getEquipmentNumber() == null) {
                    tvNoEquipment.setVisibility(View.VISIBLE);
                    tvCode.setVisibility(View.GONE);
                } else {
                    tvNoEquipment.setVisibility(View.GONE);
                    tvCode.setVisibility(View.VISIBLE);
                    tvCode.setText(item.getEquipmentNumber());

                }
            }
        };

        rvUnit.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (FastClickUtils.isSingleClick()) {

                    Intent intent = new Intent();
                    if (BluetoothUtils.getInstance().getBluetoothBean() !=null &&
                            BluetoothUtils.getInstance().getClient().getConnectStatus(BluetoothUtils
                                    .getInstance().getBluetoothBean().getAddress()) == Constants.STATUS_DEVICE_CONNECTED){
                        intent.setClass(RoomInfoActivity.this, CheckEquipmentActivity.class);
                    }else {
                        intent.setClass(RoomInfoActivity.this, ConnectBluetoothActivity.class);
                    }

                    RoomListBean.DataBean.RoomEntityBean roomBean = roomList.get(position);
                    houseInfoBean.setRoomName(roomBean.getRoomNumber());
                    houseInfoBean.setManageId(roomBean.getManageId());
                    houseInfoBean.setBusinessType(dataBean.getBussinessType());
                    if (roomBean.getRoomId() !=null){
                        houseInfoBean.setRoomId(roomBean.getRoomId());
                    }
                    if (roomBean.getEquipRoomBindId() != null) {
                        houseInfoBean.setEquipRoomBindId(roomBean.getEquipRoomBindId());
                    }else {
                        houseInfoBean.setEquipRoomBindId(0);
                    }
                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSuccess(int what, Object object) {
        dataBean = (RoomListBean.DataBean) object;
        roomList.clear();
        if (ObjectUtils.getInstance().isNotNull(dataBean.getRoomEntity())) {
            roomList.addAll(dataBean.getRoomEntity());
        }
//        else {
//            RoomListBean.DataBean.RoomEntityBean roomEntityBean = new RoomListBean.DataBean.RoomEntityBean();
//            roomEntityBean.setRoomNumber("房间");
//            roomEntityBean.setManageId(dataBean.getFloorInfos().getManageId());
//            roomEntityBean.setEquipmentNumber(dataBean.getFloorInfos().getEquipmentNumber());
//            roomEntityBean.setEquipRoomBindId(dataBean.getFloorInfos().getEquipRoomBindId());
//            roomEntityBean.setDeviceStatus(dataBean.getFloorInfos().getDeviceStatus());
//            roomEntityBean.setRoomId(dataBean.getFloorInfos().getRoomId());
//
//            roomList.add(roomEntityBean);
//
//        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
