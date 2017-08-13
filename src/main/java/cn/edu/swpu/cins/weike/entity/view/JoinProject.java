package cn.edu.swpu.cins.weike.entity.view;


import lombok.Data;

import java.util.List;
@Data
public class JoinProject {

    private List<String> released;
    private List<String> joining;
    private List<String> joinFailed;
    private List<String> joinSuccess;
    private List<String> followPro;

    public JoinProject(List<String> released, List<String> joining, List<String> joinFailed, List<String> joinSuccess, List<String> followPro) {
        this.released = released;
        this.joining = joining;
        this.joinFailed = joinFailed;
        this.joinSuccess = joinSuccess;
        this.followPro = followPro;
    }

    public JoinProject() {
    }

}
