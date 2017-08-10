package cn.edu.swpu.cins.weike.entity.view;


import java.util.List;

public class JoinProject {

    private List<String> released;
    private List<String> joining;
    private List<String> joinFailed;
    private List<String> joinSuccess;

    public JoinProject(List<String> released, List<String> joining, List<String> joinFailed, List<String> joinSuccess) {
        this.released = released;
        this.joining = joining;
        this.joinFailed = joinFailed;
        this.joinSuccess = joinSuccess;
    }

    public JoinProject() {
    }

    public List<String> getReleased() {
        return released;
    }

    public void setReleased(List<String> released) {
        this.released = released;
    }

    public List<String> getJoining() {
        return joining;
    }

    public void setJoining(List<String> joining) {
        this.joining = joining;
    }

    public List<String> getJoinFailed() {
        return joinFailed;
    }

    public void setJoinFailed(List<String> joinFailed) {
        this.joinFailed = joinFailed;
    }

    public List<String> getJoinSuccess() {
        return joinSuccess;
    }

    public void setJoinSuccess(List<String> joinSuccess) {
        this.joinSuccess = joinSuccess;
    }
}
