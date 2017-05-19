package weike.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by muyi on 17-4-6.
 */

public enum RegisterEnum {

    NULL_USERNAME("用户名不能为空"),
    NULL_PASSWORD("密码不能为空"),
    REPEATE_EMAIL("邮箱已经被注册"),
    REPETE_USERNAME("用户名已经被注册"),
    SUCCESS_SAVE("添加用户成功"),
    FAIL_SAVE("用户添加失败");



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
