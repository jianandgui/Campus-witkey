package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;

public class RegisterTeacherVO {

    private TeacherInfo teacherInfo;
    private String verifyCode;

    public RegisterTeacherVO(TeacherInfo teacherInfo, String verifyCode) {
        this.teacherInfo = teacherInfo;
        this.verifyCode = verifyCode;
    }

    public RegisterTeacherVO() {
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
