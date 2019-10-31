package com.tdr.rentalhouse.utils;


import android.database.sqlite.SQLiteDatabase;

import com.tdr.rentalhouse.application.MyApplication;
import com.tdr.rentalhouse.bean.DaoMaster;
import com.tdr.rentalhouse.bean.DaoSession;
import com.tdr.rentalhouse.bean.HistoryAddress;
import com.tdr.rentalhouse.bean.HistoryAddressDao;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/18 17:22
 * Description：
 */
public class SQLiteUtils {
    private static SQLiteUtils instance;

    private HistoryAddressDao historyAddressDao;

    private SQLiteUtils() {
        historyAddressDao = MyApplication.getInstance().getDaoSession().getHistoryAddressDao();
    }

    public static SQLiteUtils getInstance() {
        if (instance == null) {
            synchronized (SQLiteUtils.class) {
                if (instance == null) {
                    instance = new SQLiteUtils();
                }
            }
        }
        return instance;
    }

    public void insert(HistoryAddress historyAddress) {
        historyAddressDao.insertOrReplace(historyAddress);
    }

    public void insert(List<HistoryAddress> historyAddress) {
        historyAddressDao.insertOrReplaceInTx(historyAddress);
    }


    public void update(HistoryAddress historyAddress) {
        historyAddressDao.update(historyAddress);
    }

    /**
     * 查询最新五条数据
     *
     * @return
     */
    public List<HistoryAddress> query() {
        return historyAddressDao.queryBuilder().where(HistoryAddressDao.Properties.User_id.eq(
                SPUtils.getInstance().getAccountId())).orderDesc(HistoryAddressDao.Properties.Id).limit(5).list();
    }

    public HistoryAddress queryAddress(int communityId) {
        return historyAddressDao.queryBuilder().where(HistoryAddressDao.Properties.Community_id.eq(communityId),
                HistoryAddressDao.Properties.User_id.eq(SPUtils.getInstance().getAccountId())).unique();
    }


    public void delete(HistoryAddress historyAddress) {
        historyAddressDao.delete(historyAddress);
    }

}
