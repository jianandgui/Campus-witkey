package cn.edu.swpu.cins.weike.entity.view;



import java.io.Serializable;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private  String token;
    private  String username;
    private  String role;
    private  String image;
    private  boolean isCompleted;

    private JoinProject joinProject;


    public JwtAuthenticationResponse(String token, String username, String role, String image, boolean isCompleted, JoinProject joinProject) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.image = image;
        this.isCompleted = isCompleted;
        this.joinProject = joinProject;
    }

    public JwtAuthenticationResponse() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getImage() {
        return image;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public JoinProject getJoinProject() {
        return joinProject;
    }

    public void setJoinProject(JoinProject joinProject) {
        this.joinProject = joinProject;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
