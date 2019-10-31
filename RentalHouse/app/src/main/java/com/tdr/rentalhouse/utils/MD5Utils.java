package com.tdr.rentalhouse.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author：Libin on 2018/5/29 10:42
 * Email：1993911441@qq.com
 * Describe：MD5加密
 */
public class MD5Utils {
    private static MD5Utils mInstance;

    private MD5Utils() {
    }

    public static MD5Utils getInstance() {
        if (mInstance ==null){
            synchronized (MD5Utils.class){
                if (mInstance == null){
                    mInstance = new MD5Utils();
                }
            }
        }
        return mInstance;
    }


    public  String md5Encode(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
