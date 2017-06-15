package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-15.
 */
public enum UserEnum {
    ADD_PERSONAL_SUCCESS("信息添加成功"),
    ADD_PERSONAL_FAILD("信息添加失败"),
    REPEATE_ADD("请勿重复添加"),
    UPDATE_SUCCESS("信息修改成功"),
    UPDATE_FAILD("修改信息失败"),
    NO_PROJECTS("您还没有发布过任何项目"),
    ADD_PERSONNAL("个人信息未填完整");
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    UserEnum(String msg) {

        this.msg = msg;
    }
}
