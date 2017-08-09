package cn.edu.swpu.cins.weike.entity.view;

/**
 * Created by muyi on 17-4-25.
 */
public class UpdatePassword {

    private String username;
    private String password;
    private String verifyCode;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public UpdatePassword(String username, String password, String verifyCode) {
        this.username = username;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public UpdatePassword() {
    }
}
