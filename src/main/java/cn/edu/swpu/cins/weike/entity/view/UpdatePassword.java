package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-4-25.
 */
@Data
public class UpdatePassword {

    private String username;
    private String password;
    private String verifyCode;

    public UpdatePassword(String username, String password, String verifyCode) {
        this.username = username;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public UpdatePassword() {
    }
}
