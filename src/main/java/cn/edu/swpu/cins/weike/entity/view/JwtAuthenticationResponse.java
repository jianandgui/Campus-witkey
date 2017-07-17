package cn.edu.swpu.cins.weike.entity.view;

import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private final String username;
    private final String role;
    private final String image;
    private final boolean isCompleted;

    public String getImage() {
        return image;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public JwtAuthenticationResponse(String token, String username, String role, String image, boolean isCompleted) {

        this.token = token;
        this.username = username;
        this.role = role;
        this.image = image;
        this.isCompleted = isCompleted;
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

    @Override
    public String toString() {
        return "JwtAuthenticationResponse{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", image='" + image + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
