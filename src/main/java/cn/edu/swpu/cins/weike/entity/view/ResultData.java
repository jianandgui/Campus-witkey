package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

@Data
public class ResultData<T> {

    /*请求数据*/
    private T data;
    /*返回结果*/
    private boolean ifSuccess;
    private String msg;

    public ResultData(boolean ifSuccess, T data) {
        this.ifSuccess = ifSuccess;
        this.data = data;
    }

    public ResultData(boolean ifSuccess, String msg) {
        this.ifSuccess = ifSuccess;
        this.msg = msg;
    }

    public ResultData(T data) {
        this.data = data;
        this.ifSuccess = true;
    }
}
