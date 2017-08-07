package cn.edu.swpu.cins.weike.enums;


/**
 * Created by muyi on 17-4-6.
 */

public enum RegisterEnum {

    REPEATE_EMAIL("邮箱已经被注册"),
    REPETE_USERNAME("用户名已经被注册"),
    SUCCESS_SAVE("添加用户成功"),
    FAIL_SAVE("用户添加失败,请勿重复注册");


    private String message;

    RegisterEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
