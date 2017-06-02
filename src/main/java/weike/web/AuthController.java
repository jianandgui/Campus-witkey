package weike.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import weike.dao.AdminDao;
import weike.dao.StudentDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.StudentDetail;
import weike.entity.persistence.StudentInfo;
import weike.entity.persistence.TeacherDetail;
import weike.entity.persistence.TeacherInfo;
import weike.entity.view.*;
import weike.enums.RegisterEnum;
import weike.service.AuthService;
import weike.service.MailService;


/**
 * Created by muyi on 17-4-18.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/weike")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private AdminDao adminDao;


    //学生或者老师登录获取验证码
    @GetMapping("/getVerifyCode")
    public ResultData getVerifyCodeForLogin() {

        return new ResultData(true, authService.getVerifyCodeForLogin());
    }

    //学生登录
    //登录生成token
    @RequestMapping(value = "/student/login", method = RequestMethod.POST)
    public ResultData createStudentAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {


        StudentInfo studentInfo = studentDao.selectStudent(authenticationRequest.getUsername());

        if(studentInfo==null){

            return new ResultData(false,"没有该用户信息，请确认信息后登录");
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
    }

    //学生获取验证码
    @RequestMapping(value = "/student/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData studentGetveriyCode(@RequestParam String username,@RequestParam String email) throws AuthenticationException {



        if (studentDao.selectStudent(username) != null) {

            return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());
        }
        if (studentDao.queryEmail(email) != null) {

            return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());
        }


        return new ResultData<StudentInfo>(true, mailService.sendSimpleMail(email, "注册验证码"));
    }

    //学生保存信息（在前端验证码通过之后）
    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    public ResultData StudentSaveToDB(@RequestBody StudentInfo studentinfo) throws AuthenticationException {

        if (authService.studentRegister(studentinfo) == 1) {
            return new ResultData(true, "注册成功");
        }
        return new ResultData(true, "注册失败，请勿重复注册");
    }

    //学生修改密码获取验证码
    @GetMapping("/student/getVerifyCodeForFindPassword")
    public ResultData studentGetVerifyForUpdate(@RequestParam String username,@RequestParam String email) {

        StudentInfo studentinfo = studentDao.selectStudent(username);

        if (studentinfo == null) {

            return new ResultData(false, "没有该用户的信息");
        }
        if (!email.equals(studentinfo.getEmail())) {

            return new ResultData(false, "非本人邮箱");
        }

        return new ResultData(true, mailService.sendSimpleMail(studentinfo.getEmail(), "修改密码验证码"));
    }

    @PostMapping("/student/FindPassword")
    public ResultData studentUpdatePassword(@RequestBody UpdatePassowrd updatePassowrd) {

        authService.studentUpdatePassword(updatePassowrd.getUsername(), updatePassowrd.getPassword());
        if (authService.studentUpdatePassword(updatePassowrd.getUsername(), updatePassowrd.getPassword()) != 1) {

            return new ResultData(false, "密码修改失败");
        }

        return new ResultData(true, "密码修改成功");
    }

    //教师登录 同时返回token
    @RequestMapping(value = "/teacher/login", method = RequestMethod.POST)
    public ResultData createTeacherAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        final String token = authService.teacherLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        TeacherInfo teacherInfo = teacherDao.queryByName(authenticationRequest.getUsername());
        if(teacherInfo==null){

            return new ResultData(false,"没有该用户信息，请确认信息后登录");
        }
        TeacherDetail teacherDetail = teacherDao.queryForPhone(authenticationRequest.getUsername());

        String image;
        if (teacherDetail != null) {

            image = teacherDetail.getImage();
        } else {
            image = null;
        }
        String username = teacherInfo.getUsername();
        String role = teacherInfo.getRole();


        // Return the token
        return new ResultData(true, new JwtAuthenticationResponse(token, username, role, image));

    }

    //教师注册（生成验证码）
    @RequestMapping(value = "/teacher/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData teacherGetverifyCode(@RequestParam String username,@RequestParam String email) throws AuthenticationException {



        if (teacherDao.queryByName(username) != null) {

            return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());
        }
        if (teacherDao.queryEamil(email) != null) {

            return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());
        }


        return new ResultData(true, mailService.sendSimpleMail(email, "注册验证码"));
    }


    //教师召回密码 生成验证码

    @GetMapping("/teacher/getVerifyCodeForFindPassword")
    public ResultData teacherGetVerifyForUpdatePassword(@RequestParam String username,@RequestParam String email) {

        TeacherInfo teacherinfo = teacherDao.queryByName(username);

        if (teacherinfo == null) {

            return new ResultData(false, "没有该用户的信息");
        }
        if (!email.equals(teacherinfo.getEmail())) {

            return new ResultData(false, "非本人邮箱");
        }

        return new ResultData(true, mailService.sendSimpleMail(teacherinfo.getEmail(), "修改密码验证码"));
    }

    //验证码验证成功

    @PostMapping("/teacher/FindPassword")
    public ResultData teacherUpdatePassword(@RequestBody UpdatePassowrd updatePassowrd) {

        authService.teacherUpdatepassword(updatePassowrd.getUsername(), updatePassowrd.getPassword());
        if (authService.teacherUpdatepassword(updatePassowrd.getUsername(), updatePassowrd.getPassword()) != 1) {

            return new ResultData(false, "密码修改失败");
        }

        return new ResultData(true, "密码修改成功");
    }


    //教师注册（保存数据库） 在前端未通过之前我们不进行保存
    @RequestMapping(value = "/teacher/register", method = RequestMethod.POST)
    public ResultData teacherSaveToDB(@RequestBody TeacherInfo teacherinfo) throws AuthenticationException {

        if (authService.teacherRegister(teacherinfo) == 1) {

            return new ResultData(true, "注册成功！");
        }


        return new ResultData(true, "注册失败，请勿重复注册");

    }


    //管理员登录
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResultData createAdminAuthenticationToken(

            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        final String token = authService.adminLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Return the token
        String username = authenticationRequest.getUsername();
        String role = adminDao.queryByName(username).getRole();

        if(role==null){

            return new ResultData(false,"没有该用户信息，请确认信息后登录");
        }

        return new ResultData(true, new JwtAuthenticationResponse(token, username, role, null));

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
