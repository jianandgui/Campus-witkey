package cn.edu.swpu.cins.weike.entity.view;

/**
 * Created by muyi on 17-4-25.
 */
public class UpdatePassword {

    private String username;
    private String password;

    public UpdatePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UpdatePassword() {
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

    @Override
    public String toString() {
        return "UpdatePassword{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
