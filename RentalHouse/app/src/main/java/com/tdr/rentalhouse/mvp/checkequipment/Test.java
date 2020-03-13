package com.tdr.rentalhouse.mvp.checkequipment;

import com.tdr.rentalhouse.utils.BigDecimalUtils;

import java.math.BigInteger;

/**
 * Author：Libin on 2020-03-13 15:43
 * Email：1993911441@qq.com
 * Describe：
 */
public class Test {
    public static void main(String[] args) {
        String data = "AA0606060010CFF90A002099000000000000B91A";
        int equipmentHeight = Integer.parseInt(new BigInteger(data.substring(22, 24), 16).toString(10)) * 20;
       String height = BigDecimalUtils.getInstance().mul(equipmentHeight, 0.001, 3) + "米";
      System.out.println(data);
       System.out.println(height);
    }
}
