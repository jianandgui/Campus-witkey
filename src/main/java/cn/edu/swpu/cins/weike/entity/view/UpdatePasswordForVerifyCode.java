package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-4-25.
 */
@Data
public class UpdatePasswordForVerifyCode {
    private String email;
    private String username;

    public UpdatePasswordForVerifyCode() {
    }

    public UpdatePasswordForVerifyCode(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
