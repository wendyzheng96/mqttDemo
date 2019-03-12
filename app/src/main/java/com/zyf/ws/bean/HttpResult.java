package com.zyf.ws.bean;

/**
 * Created by zyf on 2019/1/31.
 */
public class HttpResult<T> {

    private int code;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
