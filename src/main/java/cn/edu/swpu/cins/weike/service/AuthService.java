package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationRequest;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationResponse;
import cn.edu.swpu.cins.weike.exception.AuthException;

/**
 * Created by muyi on 17-4-18.
 */
public interface AuthService {

    //学生注册
    int studentRegister(StudentInfo studentinfo) throws AuthException;

    //学生登陆
    JwtAuthenticationResponse studentLogin(JwtAuthenticationRequest request,String captchaCode) throws AuthException;

    //老师注册
    int teacherRegister(TeacherInfo teacherinfo) throws AuthException;

    //老师登陆
    JwtAuthenticationResponse teacherLogin(JwtAuthenticationRequest jwtAuthenticationRequest,String captchaCode) throws AuthException;

    //管理员登录
    String adminLogin(String username, String password) throws AuthException;

    //学生找回密码
    int studentUpdatePassword(String username, String password) throws AuthException;

    //老师召回密码
    int teacherUpdatePassword(String username, String password) throws AuthException;

    //    //管理员注册
    AdminInfo adminRegister(AdminInfo adminInfo) throws AuthException;

  //  String getVerifyCodeForLogin() throws AuthException;

    void studentGetVerifyCodeForRegister(String username,String email) throws AuthException;

    void teacherGetVerifyCodeForRegister(String username,String email) throws AuthException;

}
