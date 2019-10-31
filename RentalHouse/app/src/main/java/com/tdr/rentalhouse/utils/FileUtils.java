package com.tdr.rentalhouse.utils;

import android.os.Environment;

/**
 * Author：Libin on 2019/7/4 17:36
 * Description：
 */
public class FileUtils {
    public static final String appPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RentalHouse";
    //APP图片缓存路径
    public static final String imagePath = appPath + "/image";
    //选择图片压缩罗静
    public static final String photoPath = "RentalHouse/image";
    //音频路径
    public static final String audioPath = appPath + "/audio";
    //视频路径
    public static final String videoPath = appPath + "/video";
    //下载路径
    public static final String downloadPath = appPath + "/download";
}
