package cn.edu.swpu.cins.weike.enums;

public enum JoinProEnum {

    REFUCE_SUCCESS("拒绝申请成功"),
    ACCEPT_SUCCESS("已接受申请");

    private String msg;

    JoinProEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
