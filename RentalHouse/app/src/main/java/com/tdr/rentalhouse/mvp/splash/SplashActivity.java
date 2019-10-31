package com.tdr.rentalhouse.mvp.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.UserBean;
import com.tdr.rentalhouse.mvp.login.LoginActivity;
import com.tdr.rentalhouse.ui.MainActivity;
import com.tdr.rentalhouse.utils.PermissionUtils;
import com.tdr.rentalhouse.utils.SPUtils;
import com.tdr.rentalhouse.utils.ToastUtils;


/**
 * Author：Libin on 2019/7/6 14:47
 * Description：启动页
 */
public class SplashActivity extends BaseMvpActivity<SplashContact.Presenter> implements BaseView {
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION};
    private String[][] permissionArray = {{Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写"}, {Manifest.permission.ACCESS_COARSE_LOCATION, "定位"}};

    @Override
    protected SplashContact.Presenter initPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getPermissions();
    }

    private void getPermissions() {
        if (PermissionUtils.getInstance().hasPermission(this, permissions)) {
            if (TextUtils.isEmpty(SPUtils.getInstance().getToken())) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } else {
                mPresenter.getInfo(RequestCode.NetCode.GET_INFO, SPUtils.getInstance().getAccountId());
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, RequestCode.Permission.NECESSARY_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.Permission.NECESSARY_PERMISSION) {
            for (String[] strings : permissionArray) {
                if (!PermissionUtils.getInstance().hasPermission(this, strings[0])) {
                    PermissionUtils.getInstance().showPermissionDialog(SplashActivity.this,
                            strings[0], strings[1], new PermissionUtils.OnPermissionListener() {
                                @Override
                                public void onCancel() {
                                    finish();
                                }

                                @Override
                                public void onReQuest() {
                                    getPermissions();
                                }
                            });
                    return;
                }
            }
            if (TextUtils.isEmpty(SPUtils.getInstance().getToken())) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } else {
                mPresenter.getInfo(RequestCode.NetCode.GET_INFO, SPUtils.getInstance().getAccountId());
            }
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        UserBean.DataBean userInfo = (UserBean.DataBean) object;
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.USERNAME, userInfo.getUserName());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.POLICE_STATION_CODE, userInfo.getOfficeCode());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.POLICE_STATION_NAME, userInfo.getOfficeName());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.COMMUNITY_NAME, userInfo.getAreaName());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onFail(int what, String msg) {
        ToastUtils.getInstance().showToast(msg);
    }

    @Override
    public void hideLoading() {
    }
}


