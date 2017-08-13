package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

import java.util.List;
@Data
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

    public ProApplyInfo() {
    }
}
