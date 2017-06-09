package cn.edu.swpu.cins.weike.entity.persistence;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


/**
 * Created by muyi on 17-4-6.
 */
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public long getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(long lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
