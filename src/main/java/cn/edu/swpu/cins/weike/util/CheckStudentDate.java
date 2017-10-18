package cn.edu.swpu.cins.weike.util;

import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.StudentException;
import org.springframework.stereotype.Service;

@Service
public class CheckStudentDate {
    public void checkStudentInfo(StudentDetail studentDetail) {
        long entryDate = studentDetail.getEntryUniversity();
        long leaveDate = studentDetail.getLeaveUniversity();
        if (entryDate > leaveDate) {
            throw new StudentException(ExceptionEnum.DATE_ERROR.getMsg());
        }
    }
}
