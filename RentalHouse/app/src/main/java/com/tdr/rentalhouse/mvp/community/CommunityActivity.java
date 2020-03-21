package com.tdr.rentalhouse.mvp.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.bean.UnitListBean;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.addaddress.AddAddressActivity;
import com.tdr.rentalhouse.mvp.addunit.AddUnitActivity;
import com.tdr.rentalhouse.mvp.communitydetail.CommunityDetailActivity;
import com.tdr.rentalhouse.mvp.editunit.EditUnitActivity;
import com.tdr.rentalhouse.mvp.firecontrolbuilding.BuildingActivity;
import com.tdr.rentalhouse.mvp.house.ManageHouseActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/12 16:49
 * Description：
 */
public class CommunityActivity extends BaseMvpActivity<CommunityContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_title_more)
    ImageView ivTitleMore;
    @BindView(R.id.rv_manager_address)
    RecyclerView rvAddress;
    @BindView(R.id.tv_to_community)
    TextView tvToCommunity;
    private List<UnitListBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<UnitListBean, BaseViewHolder> adapter;

    private int id;
    private int type;
    //报装类型  1：出租屋  2：消防
    private int installType = 1;

    private String communityName;
    private String areaNumber;
    private String img;
    private String guid;


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


    private void getData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        guid= intent.getStringExtra("guid");
        type = intent.getIntExtra("type", 0);
        installType = getIntent().getIntExtra("install_type", 1);
    }

    @Override
    protected CommunityContact.Presenter initPresenter() {
        return new CommunityPresenter();
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        if (installType == 1){
            mPresenter.getCommunityInfo(RequestCode.NetCode.COMMUNITY_INFO, id,guid);
        }else if (installType == 2){
            mPresenter.getFireControlCommunity(RequestCode.NetCode.SELF_BUILDING_DEVICE, id);
        }

    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        if (installType == 1){
            tvTitleName.setText("地址管理");
        }else if (installType == 2){
            tvTitleName.setText("单元信息");
        }

        if (type != 1) {
            ivTitleMore.setVisibility(View.VISIBLE);
            tvToCommunity.setVisibility(View.GONE);
        }else {
            tvToCommunity.setVisibility(View.VISIBLE);
        }


        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12), 1));
        adapter = new BaseQuickAdapter<UnitListBean, BaseViewHolder>(R.layout.layout_rv_community_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, UnitListBean item) {
                EasySwipeMenuLayout swipeMenuLayout = helper.getView(R.id.esm_community);
                if (type == 1) {
                    swipeMenuLayout.setCanLeftSwipe(false);
                    swipeMenuLayout.setCanRightSwipe(false);
                } else {
                    swipeMenuLayout.setCanLeftSwipe(true);
                    swipeMenuLayout.setCanRightSwipe(true);
                }
                String name = "";
                if (!TextUtils.isEmpty(item.getBuildingNumber())) {
                    name += item.getBuildingNumber() + "幢";
                }

                if (!TextUtils.isEmpty(item.getUnitNumber())) {
                    name += item.getUnitNumber() + "单元";
                }

                helper.setText(R.id.tv_building_unit, name);
                helper.addOnClickListener(R.id.tv_building_unit);
                helper.addOnClickListener(R.id.tv_edit_unit);
                helper.addOnClickListener(R.id.tv_delete_unit);
            }

        };

        rvAddress.setAdapter(adapter);


        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.tv_building_unit:
                        if (FastClickUtils.isSingleClick()) {
                            Intent intent = new Intent();
                            if (installType == 1){
                                intent.setClass(CommunityActivity.this,ManageHouseActivity.class);
                            }else if (installType == 2){
                                intent.setClass(CommunityActivity.this, BuildingActivity.class);
                            }
                            HouseInfoBean houseInfoBean = new HouseInfoBean();
                            houseInfoBean.setBuildingType(2);
                            houseInfoBean.setCommunityId(id);
                            houseInfoBean.setCommunityName(communityName);
                            houseInfoBean.setInstallType(installType);
                            houseInfoBean.setBuildingName(dataList.get(position).getBuildingNumber());
                            houseInfoBean.setUnitId(dataList.get(position).getId());
                            houseInfoBean.setUnitName(dataList.get(position).getUnitNumber());
                            houseInfoBean.setImg(img);
                            houseInfoBean.setType(type);
                            houseInfoBean.setAreaNumber(areaNumber);
                            intent.putExtra("house", houseInfoBean);
                            startActivity(intent);
                        }

                        break;
                    case R.id.tv_edit_unit:
                        if (FastClickUtils.isSingleClick()) {
                            Intent editIntent = new Intent(CommunityActivity.this, EditUnitActivity.class);
                            editIntent.putExtra("community_id", id);
                            editIntent.putExtra("id", dataList.get(position).getId());
                            editIntent.putExtra("house_no", dataList.get(position).getBuildingNumber());
                            editIntent.putExtra("unit_no", dataList.get(position).getUnitNumber());
                            startActivity(editIntent);
                        }

                        break;
                    case R.id.tv_delete_unit:
                        PopupWindowUtils.getInstance().showPopWindow(CommunityActivity.this,
                                "是否删除该单元", "确定", new PopupOnClickListener() {
                                    @Override
                                    public void onCancel() {
                                        PopupWindowUtils.getInstance().dismiss();
                                    }

                                    @Override
                                    public void onConfirm() {
                                        PopupWindowUtils.getInstance().dismiss();
                                        LoadingUtils.getInstance().showLoading(CommunityActivity.this, "加载中");
                                        mPresenter.deleteUnit(RequestCode.NetCode.DELETE_UNIT, dataList.get(position).getId());
                                    }
                                });
                        break;
                }
            }
        });

    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_more,R.id.tv_to_community})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    Intent intent = new Intent(CommunityActivity.this, AddUnitActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }

                break;
            case R.id.tv_to_community:
                if (FastClickUtils.isSingleClick()) {

                    if (installType == 1){
                        Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else if (installType == 2){
                        if (!TextUtils.isEmpty(guid)){
                            Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("guid", guid);
                            startActivity(intent);
                        }
                    }
                }
                    break;
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.COMMUNITY_INFO:
                CommunityBean.DataBean dataBean = (CommunityBean.DataBean) object;
                if (dataBean != null) {
                    id = dataBean.getId();
                    img = dataBean.getOutlookOne();
                    communityName = dataBean.getCommunityName();
                    addHeader(Api.IMG_HOST+dataBean.getOutlookOne(),dataBean.getCommunityName(),dataBean.getAddress());

                    List<CommunityBean.DataBean.ListBean> list = dataBean.getList();
                    dataList.clear();
                    if (ObjectUtils.getInstance().isNotNull(list)) {
                        for (CommunityBean.DataBean.ListBean listBean : list) {
                            UnitListBean unitListBean = new UnitListBean(listBean.getId(),listBean.getBuildingNumber(),listBean.getUnitNumber());
                            dataList.add(unitListBean);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case RequestCode.NetCode.DELETE_UNIT:
                initData();
                break;
            case RequestCode.NetCode.SELF_BUILDING_DEVICE:
                SelfBuildingDeviceBean.DataBean communityBean = ( SelfBuildingDeviceBean.DataBean) object;
                if (communityBean != null) {

                    img = communityBean.getOutlookOne();
                    communityName = communityBean.getCommunityName();
                    areaNumber = communityBean.getRDNumber();
                    guid = communityBean.getGuid();
                    addHeader(Api.IMG_HOST+communityBean.getOutlookOne(),communityBean.getCommunityName(),communityBean.getAddress());

                    List< SelfBuildingDeviceBean.DataBean.UnitListBean> list = communityBean.getUnitList();
                    dataList.clear();
                    if (ObjectUtils.getInstance().isNotNull(list)) {
                        for (SelfBuildingDeviceBean.DataBean.UnitListBean listBean : list) {
                            UnitListBean unitListBean = new UnitListBean(listBean.getId(),listBean.getBuildingNumber(),listBean.getUnitNumber());
                            dataList.add(unitListBean);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }

    private void addHeader(String img,String communityName,String address) {
        adapter.removeAllHeaderView();

        View view = LayoutInflater.from(this).inflate(R.layout.layout_manager_address_header, null);
        SimpleDraweeView iv = view.findViewById(R.id.iv_estate);
        TextView tvName = view.findViewById(R.id.tv_estate_name);
        TextView tvAddress = view.findViewById(R.id.tv_estate_address);
        iv.setImageURI(img);
        tvName.setText(communityName);
        tvAddress.setText(address);

        adapter.addHeaderView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != 1) {
                    Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });

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

}
