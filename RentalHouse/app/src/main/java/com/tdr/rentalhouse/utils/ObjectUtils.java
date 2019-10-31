package com.tdr.rentalhouse.utils;

import java.util.List;

/**
 * Author：Libin on 2019/5/31 17:03
 * Email：1993911441@qq.com
 * Describe：list判空
 */
public class ObjectUtils {
    private static ObjectUtils mInstance;

    private ObjectUtils() {

    }

    public static ObjectUtils getInstance() {
        if (mInstance == null) {
            synchronized (ObjectUtils.class) {
                if (mInstance == null) {
                    mInstance = new ObjectUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * list不为空
     *
     * @param list
     * @param <T>
     * @return
     */
    public <T> boolean isNotNull(List<T> list) {
        return list != null && list.size() > 0;
    }

}
