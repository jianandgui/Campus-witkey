package cn.edu.swpu.cins.weike.enums;

/**
 * Created by muyi on 17-4-21.
 */
public enum LoginEnum {

    NO_VERRIFY("邮箱未经过验证，请及时验证");

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
