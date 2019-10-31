package com.tdr.rentalhouse.mvp.addroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.RoomBean;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.GsonUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/15 10:41
 * Description：新增房屋
 */
public class AddRoomActivity extends BaseMvpActivity<AddRoomContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_unit_name)
    TextView tvUnitName;
    @BindView(R.id.rv_unit)
    RecyclerView rvRoom;
    private EditText etFloor;
    private EditText etRoom;

    private List<RoomBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<RoomBean, BaseViewHolder> adapter;
    private HouseInfoBean houseInfoBean;


    @Override
    protected AddRoomContact.Presenter initPresenter() {
        return new AddRoomPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_house);
        ButterKnife.bind(this);
        getData();
        initView();
    }


    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("新增房屋");
        tvTitleMore.setText("完成");

        String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢";
        if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
            address += "/" + houseInfoBean.getUnitName() + "单元";
        }
        tvUnitName.setText(address);

        rvRoom.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<RoomBean, BaseViewHolder>(R.layout.layout_room_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, RoomBean item) {
                helper.setText(R.id.tv_floor_name, item.getFloorNo());
                helper.setText(R.id.tv_room_number, item.getRoomNo());
                helper.addOnClickListener(R.id.ll_delete_room);
            }

        };

        View view = LayoutInflater.from(this).inflate(R.layout.layout_room_footer, null);
        LinearLayout llAddRoom = view.findViewById(R.id.ll_add_room);
        etFloor = view.findViewById(R.id.et_floor_name);
        etRoom = view.findViewById(R.id.et_room_no);
        adapter.addFooterView(view);
        rvRoom.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                dataList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, dataList.size() - position);

                if (dataList.size() ==0){
                    tvTitleMore.setVisibility(View.GONE);
                }

            }
        });

        llAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String floorNo = etFloor.getText().toString().trim();
                String roomNo = etRoom.getText().toString().trim();

                if (TextUtils.isEmpty(floorNo)) {
                    ToastUtils.getInstance().showToast("请输入楼层号");
                    return;
                }

                if (TextUtils.isEmpty(roomNo)) {
                    ToastUtils.getInstance().showToast("请输入房间号");
                    return;
                }

                if (FormatUtils.getInstance().isInteger(floorNo) && Integer.parseInt(floorNo) == 0){
                    ToastUtils.getInstance().showToast("请输入正整数、大写字母楼层号");
                    return;
                }


                if (FormatUtils.getInstance().isInteger(roomNo) && Integer.parseInt(roomNo) == 0){
                    ToastUtils.getInstance().showToast("请输入正整数、大写字母房间号");
                    return;
                }


                boolean flag = true;
                for (RoomBean roomBean : dataList) {
                    if (floorNo.equals(roomBean.getFloorNo()) && roomNo.equals(roomBean.getRoomNo())) {
                        ToastUtils.getInstance().showToast("该房屋已存在");
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    RoomBean roomBean = new RoomBean();
                    roomBean.setRoomNo(roomNo);
                    roomBean.setFloorNo(floorNo);

                    dataList.add(roomBean);
                    etFloor.setText(null);
                    etRoom.setText(null);

                    adapter.notifyDataSetChanged();

                    tvTitleMore.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    addRoom();
                }
                break;
        }
    }

    /**
     * 增加房间
     */
    private void addRoom() {

        if (dataList.size() == 0) {
            ToastUtils.getInstance().showToast("请添加楼层房屋");
            return;
        }
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.addRoom(RequestCode.NetCode.ADD_ROOM, houseInfoBean.getCommunityId(), houseInfoBean.getUnitId(), GsonUtils.listToString(dataList));
    }

    @Override
    public void onSuccess(int what, Object object) {
        finish();
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
