package cn.edu.swpu.cins.weike.entity.persistence;



import lombok.Data;

import java.sql.Date;


/**
 * Created by muyi on 17-4-6.
 */
@Data
public class StudentInfo {

    //用户id
    private int Id;
    //用户名
    private String username;
    //密码
    private String password;

    private String email;

    private long lastPasswordResetDate;

    private String role;

    public StudentInfo() {

    }

    public StudentInfo(int id, String username, String password, String email, long lastPasswordResetDate, String role) {
        Id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.role = role;
    }

}
