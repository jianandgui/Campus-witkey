package cn.edu.swpu.cins.weike.enums;

public enum VerifyCodeEnum {
    GET_CODE_AGAIN("请重新获取验证码"),
    CODE_ERROR("验证码错误");

    private String msg;

    VerifyCodeEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
