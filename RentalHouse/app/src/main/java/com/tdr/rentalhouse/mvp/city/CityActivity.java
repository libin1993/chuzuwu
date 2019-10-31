package com.tdr.rentalhouse.mvp.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.CityBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/8/9 13:23
 * Description：
 */
public class CityActivity extends BaseMvpActivity<CityContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.rv_city)
    RecyclerView rvCity;
    private List<CityBean.DataBean.ListBean> cityList = new ArrayList<>();
    private BaseQuickAdapter<CityBean.DataBean.ListBean, BaseViewHolder> adapter;
    private int index = -1;
    private HouseInfoBean houseInfoBean;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();

        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        LogUtils.log(type + "");
        if (type != 0) {
            houseInfoBean = (HouseInfoBean) intent.getSerializableExtra("house");
            cityList.clear();
            cityList.addAll((Collection<? extends CityBean.DataBean.ListBean>) intent.getSerializableExtra("data"));
        } else {
            initData();
        }

    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        switch (type) {
            case 0:
                tvTitleName.setText("选择市");
                break;
            case 1:
                tvTitleName.setText("选择区");
                break;
            case 2:
                tvTitleName.setText("选择街道");
                break;
            case 3:
                tvTitleName.setText("选择社区");
                break;
        }


        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.addItemDecoration(new RVDividerItemDecoration(this));
        adapter = new BaseQuickAdapter<CityBean.DataBean.ListBean, BaseViewHolder>(R.layout.layout_rv_city_item, cityList) {
            @Override
            protected void convert(BaseViewHolder helper, CityBean.DataBean.ListBean item) {
                helper.setText(R.id.tv_city_name, item.getName());
                int position = helper.getLayoutPosition();
                helper.setVisible(R.id.iv_select_city, index == position);
            }
        };
        rvCity.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                index = position;
                adapter.notifyDataSetChanged();

                CityBean.DataBean.ListBean listBean = cityList.get(position);
                if (houseInfoBean == null) {
                    houseInfoBean = new HouseInfoBean();
                }

                switch (type) {
                    case 0:
                        houseInfoBean.setCityName(listBean.getName());
                        houseInfoBean.setCityCode(listBean.getUnionCode());
                        LoadingUtils.getInstance().showLoading(CityActivity.this, "加载中");
                        mPresenter.getCityList(RequestCode.NetCode.AREA_LIST, listBean.getUnionCode());
                        break;
                    case 1:
                        houseInfoBean.setAreaName(listBean.getName());
                        houseInfoBean.setAreaCode(listBean.getUnionCode());
                        LoadingUtils.getInstance().showLoading(CityActivity.this, "加载中");
                        mPresenter.getCityList(RequestCode.NetCode.AREA_LIST, listBean.getUnionCode());
                        break;
                    case 2:
                        houseInfoBean.setStreetName(listBean.getName());
                        houseInfoBean.setStreetCode(listBean.getUnionCode());
                        LoadingUtils.getInstance().showLoading(CityActivity.this, "加载中");
                        mPresenter.getCityList(RequestCode.NetCode.AREA_LIST, listBean.getUnionCode());
                        break;
                    case 3:
                        houseInfoBean.setResidentialName(listBean.getName());
                        houseInfoBean.setResidentialCode(listBean.getUnionCode());
                        EventBus.getDefault().post(houseInfoBean);
                        finish();
                        break;
                }

            }
        });
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.getCityList(RequestCode.NetCode.CITY_LIST, null);
    }

    @Override
    protected CityContact.Presenter initPresenter() {
        return new CityPresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        List<CityBean.DataBean.ListBean> dataList = (List<CityBean.DataBean.ListBean>) object;
        switch (what) {
            case RequestCode.NetCode.CITY_LIST:
                cityList.clear();
                if (ObjectUtils.getInstance().isNotNull(dataList)) {
                    cityList.addAll(dataList);
                }
                adapter.notifyDataSetChanged();
                break;
            case RequestCode.NetCode.AREA_LIST:
                if (ObjectUtils.getInstance().isNotNull(dataList)) {
                    Intent intent = new Intent(CityActivity.this, CityActivity.class);
                    intent.putExtra("type", type + 1);
                    intent.putExtra("house", houseInfoBean);
                    intent.putExtra("data", (Serializable) dataList);
                    startActivity(intent);
                } else {
                    EventBus.getDefault().post(houseInfoBean);
                    finish();
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

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddress(HouseInfoBean houseInfoBean) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
