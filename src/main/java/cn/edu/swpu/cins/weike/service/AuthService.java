package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.exception.AuthException;

import javax.management.OperationsException;

/**
 * Created by muyi on 17-4-18.
 */
public interface AuthService {

    //学生注册
    int studentRegister(RegisterStudentVO registerStudentVO) throws AuthException;

    //学生登陆
    JwtAuthenticationResponse studentLogin(JwtAuthenticationRequest request,String captchaCode) throws AuthException;

    //使用token登录
//    JwtAuthenticationResponse loginByToken(String token);

    //老师注册
    int teacherRegister(RegisterTeacherVO registerTeacherVO) throws AuthException;

    //老师登陆
    JwtAuthenticationResponse teacherLogin(JwtAuthenticationRequest jwtAuthenticationRequest,String captchaCode) throws AuthException;

    //管理员登录
    String adminLogin(String username, String password) throws AuthException;

    //学生找回密码
    int studentUpdatePassword(UpdatePassword updatePassword) throws AuthException;

    //老师召回密码
    int teacherUpdatePassword(UpdatePassword updatePassword) throws AuthException;

    //    //管理员注册
    AdminInfo adminRegister(AdminInfo adminInfo) throws AuthException;


    void studentGetVerifyCodeForRegister(String username,String email) throws AuthException, OperationsException;

    void teacherGetVerifyCodeForRegister(String username,String email) throws AuthException, OperationsException;

    void studentGetVerifyCodeForFindPassword(String username,String email) throws AuthException;

    void teacherGetVerifyCodeForFindPassword(String username, String email) throws AuthException;

}
