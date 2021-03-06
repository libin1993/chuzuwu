package com.tdr.rentalhouse.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.inuker.bluetooth.library.Constants;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.fragment.HomepageFragment;
import com.tdr.rentalhouse.mvp.checkequipment.CheckEquipmentActivity;
import com.tdr.rentalhouse.mvp.mine.MineFragment;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.fragment.WorkWarnFragment;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.mvp.roomlist.RoomInfoActivity;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.utils.BluetoothUtils;
import com.tdr.rentalhouse.utils.FragmentTabUtils;
import com.tdr.rentalhouse.utils.PermissionUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author：Libin on 2019/7/3 13:21
 * Description：主页
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rgs_main)
    RadioGroup rgsMain;

    //退出应用
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomepageFragment.newInstance());
        fragmentList.add(WorkWarnFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());
        new FragmentTabUtils(getSupportFragmentManager(), fragmentList, R.id.fl_main, rgsMain);

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.getInstance().showToast("再按一次退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }



    @Override
    protected void onDestroy() {
        if (BluetoothUtils.getInstance().getBluetoothBean() !=null &&
                BluetoothUtils.getInstance().getClient().getConnectStatus(BluetoothUtils
                        .getInstance().getBluetoothBean().getAddress()) == Constants.STATUS_DEVICE_CONNECTED){
            BluetoothUtils.getInstance().getClient().disconnect(BluetoothUtils.getInstance().getBluetoothBean().getAddress());
        }

        super.onDestroy();
    }
}
