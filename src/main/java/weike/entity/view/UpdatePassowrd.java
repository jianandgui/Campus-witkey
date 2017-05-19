package weike.entity.view;

/**
 * Created by muyi on 17-4-25.
 */
public class UpdatePassowrd {

    private String username;
    private String password;

    public UpdatePassowrd(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UpdatePassowrd() {
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
}
