package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-15.
 */
public enum ProjectEnum {
    NO_PROJECTS("很遗憾，没有为您找到合适的项目"),
    PUBLISH_PROJECT_FAILD("发布项目失败"),
    REPEATE_PROJECT("请不要重复发布项目"),
    ADD_PERSONNAL("个人信息未填完整");
    private String msg;

    ProjectEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
