package weike.entity.persistence;

/**
 * Created by muyi on 17-4-12.
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public AdminInfo() {
    }

}
