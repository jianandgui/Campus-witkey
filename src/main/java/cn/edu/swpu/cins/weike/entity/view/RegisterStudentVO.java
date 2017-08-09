package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;

public class RegisterStudentVO {

    private StudentInfo studentInfo;
    private String verifyCode;

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public RegisterStudentVO(StudentInfo studentInfo, String verifyCode) {
        this.studentInfo = studentInfo;
        this.verifyCode = verifyCode;
    }

    public RegisterStudentVO() {
    }
}
