package com.tdr.rentalhouse.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.tdr.rentalhouse.application.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：Libin on 2019/6/6 15:49
 * Description：
 */
public class FormatUtils {
    private static FormatUtils mInstance;

    private FormatUtils() {
    }

    public static FormatUtils getInstance() {
        if (mInstance == null) {
            synchronized (FormatUtils.class) {
                if (mInstance == null) {
                    mInstance = new FormatUtils();
                }
            }
        }
        return mInstance;
    }

    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };


    /**
     * 将px转换为与之相等的dp
     */
    public int px2dp(float pxValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将dp转换为与之相等的px
     */
    public int dp2px(float dipValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * @param str
     * @return 是否为整数
     */
    public boolean isInteger(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * @param bytes
     * @return
     */
    public String byteToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();

        if (bytes != null && bytes.length > 0) {
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
        }

        return sb.toString();
    }

    /**
     * @param function
     * @param command
     * @param content
     * @return 蓝牙写入命令
     */
    public String getWriteData(String function, String command, String content) {
        int length = content.length();
        StringBuilder sb = new StringBuilder();
        sb.append(content);
        for (int i = 0; i < 32 - length; i++) {
            sb.append("0");
        }

        String body = sb.toString();
        String crc = makeCRC(hexStringToBytes(command + body));
        return function + command + body + crc;
    }


    /**
     * @param content
     * @param length
     * @return 10进制转16进制，前面补0
     */
    public String longToHex(long content, int length) {
        String s = Long.toHexString(content);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - s.length(); i++) {
            sb.append("0");
        }

        return sb.append(s).toString().toUpperCase();
    }


    /**
     * @param content
     * @param length
     * @return 16进制转10进制，前面补0
     */
    public String hexToLong(String content, int length) {
        long code = Long.parseLong(content, 16);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - String.valueOf(code).length(); i++) {
            sb.append("0");
        }

        return sb.append(code).toString().toUpperCase();
    }

    /**
     * 计算产生校验码
     *
     * @param
     * @return 校验码
     */
    public String makeCRC(byte[] buf) {

        int len = buf.length;

        int crc = 0xFFFF;//16位
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < 0) {

                crc ^= (int) buf[pos] + 256; // XOR byte into least sig. byte of
                // crc
            } else {
                crc ^= (int) buf[pos]; // XOR byte into least sig. byte of crc
            }

            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else
                    // Else LSB is not set
                    crc >>= 1; // Just shift right
            }
        }
        String c = Integer.toHexString(crc);

        if (c.length() == 4) {
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            c = "0" + c;
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 2) {
            c = "0" + c.substring(1, 2) + "0" + c.substring(0, 1);
        }
        return c.toUpperCase();
    }


    /**
     * 16进制字符串转字节数组
     */
    public byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    public byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 忽略大小写判断字符串中是否包含某个字符串
     *
     * @param a
     * @param b
     * @return
     */
    public boolean uplowContains(String a, String b) {
        String A = a.toUpperCase();
        String B = b.toUpperCase();
        if (A.contains(B)) {
            return true;
        }

        return false;
    }


    /**
     * byte数组 转换成 16进制小写字符串
     */
    public String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }

        return hex.toString().toUpperCase();
    }


    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public byte[] hex2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }


    /**
     * 验证是否是手机号
     *
     * @param str
     * @return
     */
    public boolean IsHandset(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String regex = "^[1]+[3-9]+\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证是否是身份证号（支持15位和18位身份验证）
     *
     * @param str
     * @return
     */
    public boolean IsIDCard(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String regex = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}

