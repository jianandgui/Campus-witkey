package cn.edu.swpu.cins.weike.entity.persistence;


import lombok.Data;

/**
 * Created by muyi on 17-4-12.
 */
@Data
public class AdminInfo {

    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private long lastPasswordResetDate;

    public AdminInfo(int id, String username, String password, String role, String email, long lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
    public AdminInfo() {
    }


}
