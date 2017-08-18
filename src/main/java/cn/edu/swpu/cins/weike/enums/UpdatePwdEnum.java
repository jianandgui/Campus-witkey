package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-15.
 */
public enum UpdatePwdEnum {
    NO_USER("没有该用户的信息"),
    WRONG_EMALI("非本人邮箱"),
    UPDATE_PWD_WRONG("密码修改失败"),
    UPDATE_PWD_SUCCESS("密码修改成功");

    private String msg;

    UpdatePwdEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
