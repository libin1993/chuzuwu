package com.tdr.rentalhouse.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tdr.rentalhouse.bean.DaoMaster;
import com.tdr.rentalhouse.bean.DaoSession;
import com.tdr.rentalhouse.utils.MyOpenHelper;
import com.tdr.rentalhouse.utils.SQLiteUtils;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Author：Libin on 2019/7/2 15:52
 * Description：
 */
public class MyApplication extends Application {
    private static MyApplication mContext;

    private DaoSession mDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(this);


        CrashReport.initCrashReport(this);

        //调用Application里面的上下文   参数二为数据库名字
        MyOpenHelper helper = new MyOpenHelper(MyApplication.getInstance(), "address.db", null);

        SQLiteDatabase database = helper.getWritableDatabase();

        mDaoSession = new DaoMaster(database).newSession();
    }


    public static MyApplication getInstance() {
        return mContext;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}

