package com.tdr.rentalhouse.base;

import android.os.Environment;

/**
 * Author：Li Bin on 2019/7/19 17:06
 * Description：
 */
public class Field {

    //APP文件路径
    public static final String tdrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RentalHouse";
    //APP图片缓存路径
    public static final String imagePath = tdrPath + "/imgCache";
    //音频路径
    public static final String audioPath = tdrPath + "/audio";
    //视频路径
    public static final String videoPath = tdrPath + "/video";
    //下载路径
    public static final String downloadPath = tdrPath + "/download";
    //tbs临时文件夹
    public static final String tempPath = tdrPath + "/tbsTemp";
}
