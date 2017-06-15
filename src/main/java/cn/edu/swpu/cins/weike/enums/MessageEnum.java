package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-15.
 */
public enum MessageEnum {
    SEND_MESSAGE_SUCCESS("消息发送成功"),
    SEND_MESSAGE_FAILD("消息发送失败"),
    NO_MESSAGE("当前没有信息");

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    MessageEnum(String msg) {

        this.msg = msg;
    }
}
