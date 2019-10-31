package com.tdr.rentalhouse.mvp.editunit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/22 16:06
 * Description：
 */
public class EditUnitActivity extends BaseMvpActivity<EditUnitContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.et_no_house)
    EditText etHouse;
    @BindView(R.id.et_no_unit)
    EditText etUnit;

    private int id;
    private int communityId;
    private String houseNo;
    private String unitNo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_unit);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("编辑楼房");
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("完成");
        etHouse.setText(houseNo);
        etUnit.setText(unitNo);
    }


    private void getData() {
        Intent intent = getIntent();
        communityId = intent.getIntExtra("community_id", 0);
        id = intent.getIntExtra("id", 0);
        houseNo = intent.getStringExtra("house_no");
        unitNo = intent.getStringExtra("unit_no");
    }

    @Override
    protected EditUnitContact.Presenter initPresenter() {
        return new EditUnitPresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        EventBus.getDefault().post("add_unit");
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

    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    editUnit();
                }

                break;
        }
    }

    private void editUnit() {
        String houseNo = etHouse.getText().toString().trim();
        String unitNo = etUnit.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();

        if (TextUtils.isEmpty(houseNo)) {
            ToastUtils.getInstance().showToast("请输入楼幢号");
            return;
        }

        if (FormatUtils.getInstance().isInteger(houseNo) && Integer.parseInt(houseNo) == 0) {
            ToastUtils.getInstance().showToast("请输入正整数楼幢号");
            return;
        }

        if (!TextUtils.isEmpty(unitNo)) {
            if (FormatUtils.getInstance().isInteger(unitNo) && Integer.parseInt(unitNo) == 0) {
                ToastUtils.getInstance().showToast("请输入正整数楼幢号");
                return;
            }

            map.put("UnitNumber", unitNo);
        }
        map.put("ResidentialId",communityId);
        map.put("BuildingNumber", houseNo);
        map.put("Id", id);

        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.editUnit(RequestCode.NetCode.EDIT_UNIT, map);
    }
}
