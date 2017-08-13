package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import lombok.Data;

@Data
public class RegisterStudentVO {

    private StudentInfo studentInfo;
    private String verifyCode;

    public RegisterStudentVO(StudentInfo studentInfo, String verifyCode) {
        this.studentInfo = studentInfo;
        this.verifyCode = verifyCode;
    }

    public RegisterStudentVO() {
    }
}
