package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatePro {

    private ProjectInfo projectInfo;
    private String projectName;
}
