package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import lombok.Data;

@Data
public class RegisterTeacherVO {

    private TeacherInfo teacherInfo;
    private String verifyCode;

    public RegisterTeacherVO(TeacherInfo teacherInfo, String verifyCode) {
        this.teacherInfo = teacherInfo;
        this.verifyCode = verifyCode;
    }

    public RegisterTeacherVO() {
    }

}
