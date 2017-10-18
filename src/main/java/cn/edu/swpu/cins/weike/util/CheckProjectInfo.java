package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.entity.persistence.ProjectInfo;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import org.springframework.stereotype.Service;

@Service
public class CheckProjectInfo {

    public void checkProjectInfo(ProjectInfo projectInfo) {
        long startDate = projectInfo.getProjectStart();
        long endDate = projectInfo.getProjectEnd();
        if (startDate > endDate) {
            throw new ProjectException(ExceptionEnum.PRO_DATE_ERROR.getMsg());
        }
    }
}
