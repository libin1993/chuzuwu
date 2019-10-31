package com.tdr.rentalhouse.base;

import java.io.Serializable;

/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：json封装
 */
public class BaseBean<T> implements Serializable {
    private int Status;
    private T Data;
    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Status=" + Status +
                ", Data=" + Data +
                ", Message='" + Message + '\'' +
                '}';
    }
}


