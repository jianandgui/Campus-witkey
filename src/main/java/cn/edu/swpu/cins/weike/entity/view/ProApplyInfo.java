package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

import java.util.List;
@Data
public class ProApplyInfo {

    //这个类用来查看项目申请详情
    private List<String> applySuccess;
    private List<String> applyFailed;
    private List<String> applying;

    public ProApplyInfo(List<String> applySuccess, List<String> applyFailed, List<String> applying) {
        this.applySuccess = applySuccess;
        this.applyFailed = applyFailed;
        this.applying = applying;
    }

    public ProApplyInfo() {
    }
}
