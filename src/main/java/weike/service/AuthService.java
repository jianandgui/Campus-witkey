package weike.service;

import weike.entity.persistence.AdminInfo;
import weike.entity.persistence.StudentInfo;
import weike.entity.persistence.TeacherInfo;

/**
 * Created by muyi on 17-4-18.
 */
public interface AuthService {

    //学生注册
    public int studentRegister(StudentInfo studentinfo);

    //学生登陆
    public String studentLogin(String userName,String password);

    //老师注册
    public int teacherRegister(TeacherInfo teacherinfo);

    //老师登陆
    public String teacherLogin(String username,String password);

    //管理员登录
    public String adminLogin(String username,String password);

    //学生找回密码
    public int studentUpdatePassword(String username,String password);

    //老师召回密码
    public int teacherUpdatepassword(String username,String password);

//    //管理员注册
    public AdminInfo adminRegister(AdminInfo adminInfo);

    public String getVerifyCodeForLogin();

}
