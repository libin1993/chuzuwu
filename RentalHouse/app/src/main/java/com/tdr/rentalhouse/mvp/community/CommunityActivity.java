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
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.addunit.AddUnitActivity;
import com.tdr.rentalhouse.mvp.communitydetail.CommunityDetailActivity;
import com.tdr.rentalhouse.mvp.house.ManageHouseActivity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.mvp.editunit.EditUnitActivity;
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
    private List<CommunityBean.DataBean.ListBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<CommunityBean.DataBean.ListBean, BaseViewHolder> adapter;

    private int id;
    private int type;
    private CommunityBean.DataBean dataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();
        initData();
    }

    private void getData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);
    }

    @Override
    protected CommunityContact.Presenter initPresenter() {
        return new CommunityPresenter();
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.getCommunityInfo(RequestCode.NetCode.COMMUNITY_INFO, id);

    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("地址管理");
        if (type != 1) {
            ivTitleMore.setVisibility(View.VISIBLE);
        }

        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12), 1));
        adapter = new BaseQuickAdapter<CommunityBean.DataBean.ListBean, BaseViewHolder>(R.layout.layout_rv_community_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, CommunityBean.DataBean.ListBean item) {
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
                            Intent intent = new Intent(CommunityActivity.this, ManageHouseActivity.class);
                            HouseInfoBean houseInfoBean = new HouseInfoBean();
                            houseInfoBean.setBuildingType(2);
                            houseInfoBean.setCommunityId(id);
                            houseInfoBean.setCommunityName(dataBean.getCommunityName());
                            houseInfoBean.setBuildingName(dataList.get(position).getBuildingNumber());
                            houseInfoBean.setUnitId(dataList.get(position).getId());
                            houseInfoBean.setUnitName(dataList.get(position).getUnitNumber());
                            houseInfoBean.setImg(dataBean.getOutlookOne());
                            houseInfoBean.setType(type);
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

    @OnClick({R.id.iv_title_back, R.id.iv_title_more})
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
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.COMMUNITY_INFO:
                dataBean = (CommunityBean.DataBean) object;
                if (dataBean != null) {
                    addHeader();

                    List<CommunityBean.DataBean.ListBean> list = dataBean.getList();
                    dataList.clear();
                    if (ObjectUtils.getInstance().isNotNull(list)) {
                        dataList.addAll(list);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case RequestCode.NetCode.DELETE_UNIT:
                initData();
                break;
        }

    }

    private void addHeader() {
        adapter.removeAllHeaderView();

        View view = LayoutInflater.from(this).inflate(R.layout.layout_manager_address_header, null);
        SimpleDraweeView iv = view.findViewById(R.id.iv_estate);
        TextView tvName = view.findViewById(R.id.tv_estate_name);
        TextView tvAddress = view.findViewById(R.id.tv_estate_address);
        iv.setImageURI(Api.IMG_HOST + dataBean.getOutlookOne());
        tvName.setText(dataBean.getCommunityName());
        tvAddress.setText(dataBean.getAddress());

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addUnit(String msg) {
        if (msg.equals("add_unit")) {
            initData();
        }

        if (msg.equals("edit_address")) {
            mPresenter.getCommunityInfo(RequestCode.NetCode.COMMUNITY_INFO, id);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
