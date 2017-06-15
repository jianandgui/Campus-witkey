package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-4-21.
 */
public enum LoginEnum {

    NO_USER("没有该用户信息，请确认信息后登录");

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    LoginEnum(String message) {
        this.message = message;
    }

}
