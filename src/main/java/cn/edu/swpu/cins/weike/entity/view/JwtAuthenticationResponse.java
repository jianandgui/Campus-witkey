package cn.edu.swpu.cins.weike.entity.view;



import lombok.Data;

import java.io.Serializable;
@Data
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


}
