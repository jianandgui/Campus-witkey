package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-16.
 */
public enum ExceptionEnum {
    NO_SUITBLE_PRO("很抱歉没有合适的项目推荐给您！"),
    ILLEAGEAL_OPERATION("异常操作"),
    INNER_ERROR("服务器内部异常");
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ExceptionEnum(String msg) {

        this.msg = msg;
    }
}
