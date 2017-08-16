package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.exception.AuthException;

/**
 * Created by muyi on 17-4-18.
 */
public interface AuthService {

    //学生注册
    int studentRegister(StudentInfo studentinfo) throws AuthException;

    //学生登陆
    String studentLogin(String userName, String password) throws AuthException;

    //老师注册
    int teacherRegister(TeacherInfo teacherinfo) throws AuthException;

    //老师登陆
    String teacherLogin(String username, String password) throws AuthException;

    //管理员登录
    String adminLogin(String username, String password) throws AuthException;

    //学生找回密码
    int studentUpdatePassword(String username, String password) throws AuthException;

    //老师召回密码
    int teacherUpdatePassword(String username, String password) throws AuthException;

    //    //管理员注册
    AdminInfo adminRegister(AdminInfo adminInfo) throws AuthException;

  //  String getVerifyCodeForLogin() throws AuthException;

}
