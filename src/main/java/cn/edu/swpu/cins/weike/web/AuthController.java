package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.WeikeApplication;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationRequest;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationResponse;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.entity.view.UpdatePassword;
import cn.edu.swpu.cins.weike.enums.LoginEnum;
import cn.edu.swpu.cins.weike.enums.UpdatePwd;
import cn.edu.swpu.cins.weike.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.dao.AdminDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.enums.RegisterEnum;
import cn.edu.swpu.cins.weike.service.AuthService;


/**
 * Created by muyi on 17-4-18.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/weike")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;
    private AuthService authService;
    private StudentDao studentDao;
    private MailService mailService;
    private TeacherDao teacherDao;
    private AdminDao adminDao;

    @Autowired
    public AuthController(AuthService authService, StudentDao studentDao, MailService mailService, TeacherDao teacherDao, AdminDao adminDao) {
        this.authService = authService;
        this.studentDao = studentDao;
        this.mailService = mailService;
        this.teacherDao = teacherDao;
        this.adminDao = adminDao;
    }

    //学生或者老师登录获取验证码
    @GetMapping("/getVerifyCode")
    public ResultData getVerifyCodeForLogin() {
        try {
            return new ResultData(true, authService.getVerifyCodeForLogin());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    //学生登录
    //登录生成token
    @RequestMapping(value = "/student/login", method = RequestMethod.POST)
    public ResultData createStudentAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {

            StudentInfo studentInfo = studentDao.selectStudent(authenticationRequest.getUsername());
            if (studentInfo == null) {
                return new ResultData(false, LoginEnum.NO_USER);
            }
            StudentDetail studentDetail = studentDao.queryForStudentPhone(authenticationRequest.getUsername());
            String image;
            if (studentDetail != null) {
                image = studentDetail.getImage();
            } else {
                image = null;
            }
            String username = studentInfo.getUsername();
            String role = studentInfo.getRole();
            final String token = authService.studentLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, image));
        } catch (Exception e) {

            return new ResultData(false, e.getMessage());
        }

    }

    //学生获取验证码
    @RequestMapping(value = "/student/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData studentGetveriyCode(@RequestParam String username, @RequestParam String email) {
        try {
            if (studentDao.selectStudent(username) != null) {
                return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());
            }
            if (studentDao.queryEmail(email) != null) {
                return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());
            }
            return new ResultData<StudentInfo>(true, mailService.sendSimpleMail(username, email));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    //学生保存信息（在前端验证码通过之后）
    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    public ResultData StudentSaveToDB(@RequestBody StudentInfo studentinfo) {
        try {
            if (authService.studentRegister(studentinfo) == 1) {
                return new ResultData(true, RegisterEnum.SUCCESS_SAVE.getMessage());}
            return new ResultData(true, RegisterEnum.FAIL_SAVE.getMessage());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    //学生修改密码获取验证码
    @GetMapping("/student/getVerifyCodeForFindPassword")
    public ResultData studentGetVerifyForUpdate(@RequestParam String username, @RequestParam String email) {
        try {
            StudentInfo studentinfo = studentDao.selectStudent(username);
            if (studentinfo == null) {
                return new ResultData(false, UpdatePwd.NO_USER.getMsg());}
            if (!email.equals(studentinfo.getEmail())) {
                return new ResultData(false, UpdatePwd.WRONG_EMALI.getMsg());}
            return new ResultData(true, mailService.sendMailForUpdatePwd(studentinfo.getEmail()));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    @PostMapping("/student/FindPassword")
    public ResultData studentUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            authService.studentUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword());
            if (authService.studentUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword()) != 1) {
                return new ResultData(false, UpdatePwd.UPDATE_PWD_WRONG.getMsg());}
            return new ResultData(true, UpdatePwd.UPDATE_PWD_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());

        }

    }

    //教师登录 同时返回token
    @RequestMapping(value = "/teacher/login", method = RequestMethod.POST)
    public ResultData createTeacherAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            TeacherInfo teacherInfo = teacherDao.queryByName(authenticationRequest.getUsername());
            if (teacherInfo == null) {
                return new ResultData(false, LoginEnum.NO_USER.getMessage());}
            final String token = authService.teacherLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            TeacherDetail teacherDetail = teacherDao.queryForPhone(authenticationRequest.getUsername());
            String image;
            if (teacherDetail != null) {
                image = teacherDetail.getImage();
            } else {
                image = null;}
            String username = teacherInfo.getUsername();
            String role = teacherInfo.getRole();
            // Return the token
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, image));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    //教师注册（生成验证码）
    @RequestMapping(value = "/teacher/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData teacherGetverifyCode(@RequestParam String username, @RequestParam String email) {

        try {
            if (teacherDao.queryByName(username) != null) {
                return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());}
            if (teacherDao.queryEamil(email) != null) {
                return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());}
            return new ResultData(true, mailService.sendSimpleMail(username, email));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }


    //教师召回密码 生成验证码

    @GetMapping("/teacher/getVerifyCodeForFindPassword")
    public ResultData teacherGetVerifyForUpdatePassword(@RequestParam String username, @RequestParam String email) {
        try {
            TeacherInfo teacherinfo = teacherDao.queryByName(username);
            if (teacherinfo == null) {
                return new ResultData(false, UpdatePwd.NO_USER.getMsg());}
            if (!email.equals(teacherinfo.getEmail())) {
                return new ResultData(false, UpdatePwd.WRONG_EMALI.getMsg());}
            return new ResultData(true, mailService.sendMailForUpdatePwd(teacherinfo.getEmail()));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    //验证码验证成功

    @PostMapping("/teacher/FindPassword")
    public ResultData teacherUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            authService.teacherUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword());
            if (authService.teacherUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword()) != 1) {
                return new ResultData(false, UpdatePwd.UPDATE_PWD_WRONG.getMsg());}
            return new ResultData(true, UpdatePwd.UPDATE_PWD_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    //教师注册（保存数据库） 在前端未通过之前我们不进行保存
    @RequestMapping(value = "/teacher/register", method = RequestMethod.POST)
    public ResultData teacherSaveToDB(@RequestBody TeacherInfo teacherinfo) {
        try {
            if (authService.teacherRegister(teacherinfo) == 1) {
                return new ResultData(true, RegisterEnum.SUCCESS_SAVE.getMessage());
            }
            return new ResultData(true, RegisterEnum.FAIL_SAVE);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }


    }


    //管理员登录
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResultData createAdminAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {

            final String token = authService.adminLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            // Return the token
            String username = authenticationRequest.getUsername();
            String role = adminDao.queryByName(username).getRole();
            if (role == null) {
                return new ResultData(false, LoginEnum.NO_USER.getMessage());
            }
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, null));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }


    }

    //    //管理员注册 注册完毕关闭接口
//    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
//    public ResultData adminSaveToDB(@RequestBody AdminInfo adminInfo)throws AuthenticationException {
//
//
//        return new ResultData<AdminInfo> (true,authService.adminRegister(adminInfo));
//
//    }


}
