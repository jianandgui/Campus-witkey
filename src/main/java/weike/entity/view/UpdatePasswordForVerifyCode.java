package weike.entity.view;

/**
 * Created by muyi on 17-4-25.
 */
public class UpdatePasswordForVerifyCode {

    private String email;

    private String username;

    public UpdatePasswordForVerifyCode() {
    }

    public UpdatePasswordForVerifyCode(String email, String username) {

        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
