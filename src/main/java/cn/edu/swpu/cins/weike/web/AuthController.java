package cn.edu.swpu.cins.weike.web;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.FishEyeGimpyRenderer;
import cn.edu.swpu.cins.weike.entity.persistence.*;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.LoginEnum;
import cn.edu.swpu.cins.weike.enums.UpdatePwdEnum;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.GetVerifyCode;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.dao.AdminDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.enums.RegisterEnum;
import cn.edu.swpu.cins.weike.service.AuthService;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.beans.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


/**
 * Created by muyi on 17-4-18.
 */
//@CrossOrigin(maxAge = 3600)
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
    private JedisAdapter jedisAdapter;
    private GetVerifyCode getVerifyCode;


    @Autowired
    public AuthController(AuthService authService, StudentDao studentDao, MailService mailService, TeacherDao teacherDao, AdminDao adminDao, JedisAdapter jedisAdapter, GetVerifyCode getVerifyCode) {
        this.getVerifyCode = getVerifyCode;
        this.jedisAdapter = jedisAdapter;
        this.authService = authService;
        this.studentDao = studentDao;
        this.mailService = mailService;
        this.teacherDao = teacherDao;
        this.adminDao = adminDao;
    }



    /**
     * 获取登录验证码接口
     *
     * @return
     */
    @GetMapping(value = "/getVerifyCode")
    public ResultData getVerifyCodeForLogin(HttpServletResponse response) {
        try {
            String result = getVerifyCode.getVerifyCode(response);
            return new ResultData(true, result);
        } catch (Exception e) {
            return new ResultData(false, ExceptionEnum.INNER_ERROR.getMsg());
        }
    }


    /**
     * 学生登录接口
     *
     * @param authenticationRequest 请求体
     * @return 返回token和申请项目的信息
     */
    @RequestMapping(value = "/student/login", method = RequestMethod.POST)
    public ResultData createStudentAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, @RequestHeader("captcha-code") String captchaCode) {
        try {
            JwtAuthenticationResponse response = authService.studentLogin(authenticationRequest, captchaCode);
            return new ResultData(true, response);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 教师登录接口
     *
     * @param authenticationRequest
     * @return 返回token和申请项目的信息
     */
    @RequestMapping(value = "/teacher/login", method = RequestMethod.POST)
    public ResultData createTeacherAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, @RequestHeader("captcha-code") String captchaCode) {
        try {
            // Return the token
            JwtAuthenticationResponse response = authService.teacherLogin(authenticationRequest, captchaCode);
            return new ResultData(true, response);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 学生注册时获取验证码
     *
     * @param username
     * @param email
     * @return
     */
    @RequestMapping(value = "/student/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData studentGetVerifyCode(@RequestParam String username, @RequestParam String email) {
        try {
            authService.studentGetVerifyCodeForRegister(username, email);
            return new ResultData<StudentInfo>(true, RegisterEnum.MAIL_CODE.getMessage());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 老师注册获取验证码
     *
     * @param username
     * @param email
     * @return
     */
    @RequestMapping(value = "/teacher/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData teacherGetVerifyCode(@RequestParam String username, @RequestParam String email) {
        try {
            authService.teacherGetVerifyCodeForRegister(username, email);
            return new ResultData<StudentInfo>(true, RegisterEnum.MAIL_CODE.getMessage());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 学生通过注册验证码，将数据保存在DB中
     *
     * @param registerStudentVO
     * @return
     */
    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    public ResultData StudentSaveToDB(@RequestBody RegisterStudentVO registerStudentVO) {
        try {
            authService.studentRegister(registerStudentVO);
            return new ResultData(true, RegisterEnum.SUCCESS_SAVE);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    @RequestMapping(value = "/teacher/register", method = RequestMethod.POST)
    public ResultData teacherSaveToDB(@RequestBody RegisterTeacherVO registerTeacherVO) {
        try {
            authService.teacherRegister(registerTeacherVO);
            return new ResultData(true, RegisterEnum.SUCCESS_SAVE);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    @GetMapping("/student/getVerifyCodeForFindPassword")
    public ResultData studentGetVerifyForUpdate(@RequestParam String username, @RequestParam String email) {
        try {
            authService.studentGetVerifyCodeForFindPassword(username, email);
            return new ResultData(true, UpdatePwdEnum.MAIL_SEND_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    @GetMapping("/teacher/getVerifyCodeForFindPassword")
    public ResultData teacherGetVerifyForUpdatePassword(@RequestParam String username, @RequestParam String email) {
        try {
            authService.teacherGetVerifyCodeForFindPassword(username, email);
            return new ResultData(true, UpdatePwdEnum.MAIL_SEND_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/student/FindPassword")
    public ResultData studentUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            authService.studentUpdatePassword(updatePassword);
            return new ResultData(true, UpdatePwdEnum.UPDATE_PWD_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/teacher/FindPassword")
    public ResultData teacherUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            authService.teacherUpdatePassword(updatePassword);
            return new ResultData(true, UpdatePwdEnum.UPDATE_PWD_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResultData createAdminAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            JoinProject joinProject = null;
            final String token = authService.adminLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            // Return the token
            String username = authenticationRequest.getUsername();
            String role = adminDao.queryByName(username).getRole();
            if (role == null) {
                return new ResultData(false, LoginEnum.NO_USER.getMessage());
            }
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, null, false, joinProject));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

/*
    //管理员注册 注册完毕关闭接口
    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public ResultData adminSaveToDB(@RequestBody AdminInfo adminInfo) throws AuthenticationException {


        return new ResultData<AdminInfo>(true, authService.adminRegister(adminInfo));

    }
*/
}
