package com.tdr.rentalhouse.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseFragment;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.mvp.selectaddress.SelectAddressActivity;
import com.tdr.rentalhouse.ui.InstallDocActivity;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.PermissionUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author：Li Bin on 2019/7/6 17:44
 * Description：
 */
public class HomepageFragment extends BaseFragment {

    @BindView(R.id.iv_homepage)
    ImageView ivHomepage;
    @BindView(R.id.rl_add_address)
    RelativeLayout rlAddAddress;
    @BindView(R.id.rl_install_equipment)
    RelativeLayout rlInstallEquipment;
    @BindView(R.id.rl_install_handbook)
    RelativeLayout rlInstallHandbook;
    @BindView(R.id.rl_register_house)
    RelativeLayout rlRegisterHouse;
    @BindView(R.id.rl_register_human)
    RelativeLayout rlRegisterHuman;
    @BindView(R.id.rl_data_statistics)
    RelativeLayout rlDataStatistics;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homapage, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public static HomepageFragment newInstance() {

        Bundle args = new Bundle();
        HomepageFragment fragment = new HomepageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_homepage, R.id.rl_add_address, R.id.rl_install_equipment, R.id.rl_install_handbook,
            R.id.rl_register_house, R.id.rl_register_human, R.id.rl_data_statistics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_homepage:
                break;
            case R.id.rl_add_address:
                if (FastClickUtils.isSingleClick()){
                    startActivity(new Intent(getActivity(), SelectAddressActivity.class));
                }
                break;
            case R.id.rl_install_equipment:
                if (FastClickUtils.isSingleClick()){
                    getPermission();
                }
                break;
            case R.id.rl_install_handbook:
                if (FastClickUtils.isSingleClick()){
                    startActivity(new Intent(getActivity(),InstallDocActivity.class));
                }
                break;
            case R.id.rl_register_house:
                moduleDeveloping();
                break;
            case R.id.rl_register_human:
                moduleDeveloping();
                break;
            case R.id.rl_data_statistics:
                moduleDeveloping();
                break;
        }
    }

    /**
     * 相机权限
     */
    private void getPermission() {
        if (PermissionUtils.getInstance().hasPermission(getActivity(), Manifest.permission.CAMERA)) {
            Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestCode.Permission.CAMERA_PERMISSION);
        }
    }


    /**
     * 模块开发中
     */
    private void moduleDeveloping() {
        if (FastClickUtils.isSingleClick()){
            ToastUtils.getInstance().showToast("模块正在开发中");
        }

    }

}



