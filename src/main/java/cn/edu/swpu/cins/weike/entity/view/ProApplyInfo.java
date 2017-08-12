package cn.edu.swpu.cins.weike.entity.view;

import java.util.List;

public class ProApplyInfo {

    //这个类用来查看项目申请详情
    private List<String> apllySuccess;
    private List<String> applyFailed;
    private List<String> applying;

    public ProApplyInfo(List<String> apllySuccess, List<String> applyFailed, List<String> applying) {
        this.apllySuccess = apllySuccess;
        this.applyFailed = applyFailed;
        this.applying = applying;
    }

    public List<String> getApllySuccess() {
        return apllySuccess;
    }

    public void setApllySuccess(List<String> apllySuccess) {
        this.apllySuccess = apllySuccess;
    }

    public List<String> getApplyFailed() {
        return applyFailed;
    }

    public void setApplyFailed(List<String> applyFailed) {
        this.applyFailed = applyFailed;
    }

    public List<String> getApplying() {
        return applying;
    }

    public void setApplying(List<String> applying) {
        this.applying = applying;
    }

    public ProApplyInfo() {
    }
}
