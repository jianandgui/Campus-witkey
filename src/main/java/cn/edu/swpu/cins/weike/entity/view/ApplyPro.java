package cn.edu.swpu.cins.weike.entity.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyPro {

    //显示项目申请详情
    private List<String> applySuccess;
    private List<String> applyFaild;
    private List<String> appling;
}
