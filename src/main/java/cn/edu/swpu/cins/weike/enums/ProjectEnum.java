package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-6-15.
 */
public enum ProjectEnum {
    NO_PROJECTS("很遗憾，没有为您找到合适的项目"),
    NO_SUITBLE_PERSON("很遗憾，没有为您的项目匹配到合适的人"),
    PUBLISH_PROJECT_FAILD("发布项目失败"),
    REPEATE_PROJECT("请不要重复发布项目"),
    FOLLOW_PRO_FAILD("取消关注项目成功"),
    FOLLOW_PRO_SUCCESS("关注项目成功");
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
