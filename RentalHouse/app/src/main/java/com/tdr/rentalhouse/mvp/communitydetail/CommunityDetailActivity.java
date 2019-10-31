package com.tdr.rentalhouse.mvp.communitydetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.CommunityDetailBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.mvp.addaddress.AddAddressActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/12 16:51
 * Description：
 */
public class CommunityDetailActivity extends BaseMvpActivity<CommunityDetailContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_area_name)
    TextView tvAreaName;
    @BindView(R.id.tv_area_address)
    TextView tvAreaAddress;
    @BindView(R.id.tv_building_type)
    TextView tvBuildingType;
    @BindView(R.id.tv_plate_no)
    TextView tvPlateNo;
    @BindView(R.id.tv_police_station_name)
    TextView tvPoliceStationName;
    @BindView(R.id.rl_community)
    RelativeLayout rlCommunity;
    @BindView(R.id.view_community)
    View viewCommunity;
    @BindView(R.id.banner_community)
    Banner banner;

    private int id;
    private CommunityDetailBean.DataBean communityBean;

    @Override
    protected CommunityDetailContact.Presenter initPresenter() {
        return new CommunityDetailPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();

        initData();
    }

    private void getData() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("详情");
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("编辑");

        id = getIntent().getIntExtra("id", 0);
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.communityDetail(RequestCode.NetCode.COMMUNITY_DETAIL, id);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    if (communityBean != null) {
                        Intent intent = new Intent(CommunityDetailActivity.this, AddAddressActivity.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("community", communityBean);
                        startActivity(intent);

                    }
                }

                break;
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        communityBean = (CommunityDetailBean.DataBean) object;
        if (communityBean != null) {
            HouseInfoBean houseInfoBean = new HouseInfoBean();
            houseInfoBean.setAreaNumber(communityBean.getRDNumber());
            houseInfoBean.setCityName(communityBean.getCityName());
            houseInfoBean.setAreaName(communityBean.getAreaName());
            houseInfoBean.setStreetName(communityBean.getSteetName());
            houseInfoBean.setResidentialName(communityBean.getComminityName());
            houseInfoBean.setImg(String.valueOf(communityBean.getOutlookOne()));
            EventBus.getDefault().post(houseInfoBean);

            initView();
        }
    }

    private void initView() {
        List<String> imgList = new ArrayList<>();
        if (!TextUtils.isEmpty(communityBean.getOutlookOne())) {
            imgList.add(Api.IMG_HOST + communityBean.getOutlookOne());
        }

        if (!TextUtils.isEmpty(communityBean.getOutlookTwo())) {
            imgList.add(Api.IMG_HOST + communityBean.getOutlookTwo());
        }

        if (!TextUtils.isEmpty(communityBean.getOutlookThree())) {
            imgList.add(Api.IMG_HOST + communityBean.getOutlookThree());
        }

        banner.setImages(imgList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .isAutoPlay(false)
                .start();


        if (communityBean.getArchitecturalTypes() == 1 || communityBean.getArchitecturalTypes() == 4) {
            rlCommunity.setVisibility(View.GONE);
            viewCommunity.setVisibility(View.GONE);
        } else {
            rlCommunity.setVisibility(View.VISIBLE);
            viewCommunity.setVisibility(View.VISIBLE);
            tvAreaName.setText(communityBean.getCommunityName());
        }


        String address = communityBean.getCityName();
        if (!TextUtils.isEmpty(communityBean.getAreaName())) {
            address += communityBean.getAreaName();
        }

        if (!TextUtils.isEmpty(communityBean.getSteetName())) {
            address += communityBean.getSteetName();
        }

        if (!TextUtils.isEmpty(communityBean.getComminityName())) {
            address += communityBean.getComminityName();
        }

        tvAreaAddress.setText(address);
        switch (communityBean.getArchitecturalTypes()) {
            case 1:
                tvBuildingType.setText("自建房");
                break;
            case 2:
                tvBuildingType.setText("商品房");
                break;
            case 3:
                tvBuildingType.setText("企业单位");
                break;
            case 4:
                tvBuildingType.setText("旅店");
                break;
        }

        tvPlateNo.setText(communityBean.getRDNumber());
        tvPoliceStationName.setText(communityBean.getPoliceStationsName());
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
    public void editAddress(String msg) {
        if (msg.equals("edit_address")) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
