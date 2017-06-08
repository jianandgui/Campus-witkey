package weike.entity.view;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private final String username;
    private final String role;
    private final String image;

    public String getImage() {
        return image;
    }

    public JwtAuthenticationResponse(String token, String username, String role, String image) {

        this.token = token;
        this.username = username;
        this.role = role;
        this.image = image;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }


}
