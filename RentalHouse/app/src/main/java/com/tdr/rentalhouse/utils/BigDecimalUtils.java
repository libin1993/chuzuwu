package com.tdr.rentalhouse.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Author：Libin on 2018/8/8 09:00
 * Email：1993911441@qq.com
 * Describe：
 */
public class BigDecimalUtils {
    private static BigDecimalUtils mInstance;

    private BigDecimalUtils() {
    }

    public static BigDecimalUtils getInstance() {
        if (mInstance == null) {
            synchronized (BigDecimalUtils.class) {
                if (mInstance == null) {
                    mInstance = new BigDecimalUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param value1
     * @param value2
     * @return 加
     */
    public double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * @param value1
     * @param value2
     * @return 加
     */
    public double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return 减
     */
    public double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return 减
     */
    public double sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }




    /**
     * @param v1
     * @param v2
     * @return 乘
     */
    public double mul(double v1, double v2, int size) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(size, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * @param v1
     * @param v2
     * @return 除
     */
    public double div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

}
