package com.tdr.rentalhouse.mvp.editroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.SectionItem;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/23 13:27
 * Description：
 */
public class EditRoomActivity extends BaseMvpActivity<EditRoomContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.rv_manager_address)
    RecyclerView rvRoom;


    private List<SectionItem> roomList;
    private BaseQuickAdapter<SectionItem, BaseViewHolder> adapter;
    private int communityId;
    private int unitId;

    private String floor;
    private String room;
    private int index;


    @Override
    protected EditRoomContact.Presenter initPresenter() {
        return new EditRoomPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_info);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("编辑房屋");

        rvRoom.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<SectionItem, BaseViewHolder>(R.layout.layout_rv_edit_room_item, roomList) {
            @Override
            protected void convert(BaseViewHolder helper, SectionItem item) {
                helper.setText(R.id.et_floor_no, item.header);
                helper.setText(R.id.et_room_number, item.name);

                helper.addOnClickListener(R.id.ll_edit_room);
                helper.addOnClickListener(R.id.ll_delete_house);


            }
        };

        rvRoom.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final SectionItem sectionItem = roomList.get(position);
                index = position;

                switch (view.getId()) {
                    case R.id.ll_edit_room:
                        EditText etFloorNo = (EditText) adapter.getViewByPosition(rvRoom, position, R.id.et_floor_no);
                        EditText etRoomNo = (EditText) adapter.getViewByPosition(rvRoom, position, R.id.et_room_number);

                        String floorNo = etFloorNo.getText().toString().trim();
                        String houseNo = etRoomNo.getText().toString().trim();

                        if (TextUtils.isEmpty(floorNo)) {
                            ToastUtils.getInstance().showToast("请输入楼层号");
                            return;
                        }

                        if (TextUtils.isEmpty(houseNo)) {
                            ToastUtils.getInstance().showToast("请输入房间号");
                            return;
                        }


                        if (FormatUtils.getInstance().isInteger(floorNo) && Integer.parseInt(floorNo) == 0) {
                            ToastUtils.getInstance().showToast("请输入正整数、大写字母楼层号");
                            return;
                        }


                        if (FormatUtils.getInstance().isInteger(houseNo) && Integer.parseInt(houseNo) == 0) {
                            ToastUtils.getInstance().showToast("请输入正整数、大写字母房间号");
                            return;
                        }


                        floor = floorNo;
                        room = houseNo;

                        LoadingUtils.getInstance().showLoading(EditRoomActivity.this, "加载中");
                        mPresenter.editRoom(RequestCode.NetCode.EDIT_ROOM, sectionItem.id,
                                communityId, unitId, floorNo, houseNo);
                        break;
                    case R.id.ll_delete_house:
                        PopupWindowUtils.getInstance().showPopWindow(EditRoomActivity.this,
                                "是否删除该房屋？", "确定", new PopupOnClickListener() {
                                    @Override
                                    public void onCancel() {
                                        PopupWindowUtils.getInstance().dismiss();
                                    }

                                    @Override
                                    public void onConfirm() {
                                        PopupWindowUtils.getInstance().dismiss();
                                        LoadingUtils.getInstance().showLoading(EditRoomActivity.this, "加载中");
                                        mPresenter.deleteHouse(RequestCode.NetCode.DELETE_HOUSE, sectionItem.id);
                                    }
                                });
                        break;
                }
            }
        });

    }


    private void getData() {
        Intent intent = getIntent();
        roomList = (List<SectionItem>) intent.getSerializableExtra("house");
        communityId = intent.getIntExtra("community_id", 0);
        unitId = intent.getIntExtra("unit_id", 0);


    }


    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.EDIT_ROOM:
                roomList.get(index).header = floor;
                roomList.get(index).name = room;
                adapter.notifyDataSetChanged();
                ToastUtils.getInstance().showToast("修改成功");
                break;
            case RequestCode.NetCode.DELETE_HOUSE:
                roomList.remove(index);
                adapter.notifyItemRemoved(index);
                adapter.notifyItemRangeChanged(index, roomList.size() - index);
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


    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
