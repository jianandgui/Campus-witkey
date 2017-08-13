package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventProducer;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.entity.persistence.*;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.enums.LoginEnum;
import cn.edu.swpu.cins.weike.enums.UpdatePwd;
import cn.edu.swpu.cins.weike.service.MailService;

import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import cn.edu.swpu.cins.weike.dao.AdminDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.enums.RegisterEnum;
import cn.edu.swpu.cins.weike.service.AuthService;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;


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


    @Autowired
    JedisAdapter jedisAdapter;


    @Autowired
    EventProducer eventProducer;

    @Autowired
    public AuthController(AuthService authService, StudentDao studentDao, MailService mailService, TeacherDao teacherDao, AdminDao adminDao) {
        this.authService = authService;
        this.studentDao = studentDao;
        this.mailService = mailService;
        this.teacherDao = teacherDao;
        this.adminDao = adminDao;
    }

    //学生或者老师登录获取验证码

    /**
     * 获取登录验证码接口
     * @return
     */
    @GetMapping("/getVerifyCode")
    public ResultData getVerifyCodeForLogin() {
        try {
            return new ResultData(true, authService.getVerifyCodeForLogin());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage()); }
    }

    //学生登录
    //登录生成token

    /**
     * 学生登录接口
     * @param authenticationRequest 请求体
     * @return 返回token和申请项目的信息
     */
    @RequestMapping(value = "/student/login", method = RequestMethod.POST)
    public ResultData createStudentAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            JoinProject joinProject = new JoinProject();

            String applyingProKey = RedisKey.getBizApplyingPro(authenticationRequest.getUsername());
            String applySuccessKey = RedisKey.getBizJoinSuccess(authenticationRequest.getUsername());
            String applyFailedKey = RedisKey.getBizJoinFail(authenticationRequest.getUsername());
            String followerProKey = RedisKey.getBizAttentionPro(authenticationRequest.getUsername());

            joinProject.setReleased(studentDao.queryAllProject(authenticationRequest.getUsername()));
            joinProject.setJoining((jedisAdapter.smenber(applyingProKey).stream().collect(Collectors.toList())));
            joinProject.setJoinSuccess(jedisAdapter.smenber(applySuccessKey).stream().collect(Collectors.toList()));
            joinProject.setJoinFailed(jedisAdapter.smenber(applyFailedKey).stream().collect(Collectors.toList()));
            joinProject.setFollowPro(jedisAdapter.smenber(followerProKey).stream().collect(Collectors.toList()));
            StudentInfo studentInfo = studentDao.selectStudent(authenticationRequest.getUsername());
            if (studentInfo == null) {
                return new ResultData(false, LoginEnum.NO_USER);
            }
            StudentDetail studentDetail = studentDao.queryForStudentPhone(authenticationRequest.getUsername());
            String image;
            boolean isCompleted;
            if (studentDetail != null) {
                image = studentDetail.getImage();
                isCompleted = true;
            } else {
                image = null;
                isCompleted = false;
            }
            String username = studentInfo.getUsername();
            String role = studentInfo.getRole();
            final String token = authService.studentLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, image, isCompleted, joinProject));
        } catch (Exception e) {

            return new ResultData(false, e.getMessage());
        }

    }

    //教师登录 同时返回token

    /**
     * 教师登录接口
     * @param authenticationRequest
     * @return 返回token和申请项目的信息
     */
    @RequestMapping(value = "/teacher/login", method = RequestMethod.POST)
    public ResultData createTeacherAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            JoinProject joinProject = new JoinProject();

            String applyingProKey = RedisKey.getBizApplyingPro(authenticationRequest.getUsername());
            String applySuccessKey = RedisKey.getBizJoinSuccess(authenticationRequest.getUsername());
            String applyFailedKey = RedisKey.getBizJoinFail(authenticationRequest.getUsername());
            String followerProKey = RedisKey.getBizAttentionPro(authenticationRequest.getUsername());

            joinProject.setReleased(studentDao.queryAllProject(authenticationRequest.getUsername()));
            joinProject.setJoining((jedisAdapter.smenber(applyingProKey).stream().collect(Collectors.toList())));
            joinProject.setJoinSuccess(jedisAdapter.smenber(applySuccessKey).stream().collect(Collectors.toList()));
            joinProject.setJoinFailed(jedisAdapter.smenber(applyFailedKey).stream().collect(Collectors.toList()));
            joinProject.setFollowPro(jedisAdapter.smenber(followerProKey).stream().collect(Collectors.toList()));
            TeacherInfo teacherInfo = teacherDao.queryByName(authenticationRequest.getUsername());
            if (teacherInfo == null) {
                return new ResultData(false, LoginEnum.NO_USER.getMessage());
            }
            final String token = authService.teacherLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            TeacherDetail teacherDetail = teacherDao.queryForPhone(authenticationRequest.getUsername());
            String image;
            boolean isCompleted;
            if (teacherDetail != null) {
                image = teacherDetail.getImage();
                isCompleted = true;
            } else {
                image = null;
                isCompleted = false;
            }
            String username = teacherInfo.getUsername();
            String role = teacherInfo.getRole();
            // Return the token
            return new ResultData(true, new JwtAuthenticationResponse(token, username, role, image, isCompleted, joinProject));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }

    //学生获取验证码
    //前端验证成后端验证 采用异步发送邮件

    /**
     * 学生注册时获取验证码
     * @param username
     * @param email
     * @return
     */
    @RequestMapping(value = "/student/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData studentGetVerifyCode(@RequestParam String username, @RequestParam String email) {
        try {
            if (studentDao.selectStudent(username) != null) {
                return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());
            }
            if (studentDao.queryEmail(email) != null) {
                return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());
            }
            if (eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("username", username).setExts("email", email).setExts("status", "register"))) {
                return new ResultData<StudentInfo>(true, "请到您的邮箱查看验证码");
            }
            return new ResultData(false, "服务器内部异常");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }



    /**
     * 老师注册获取验证码
     * @param username
     * @param email
     * @return
     */
    @RequestMapping(value = "/teacher/GetVerifyCodeForRegister", method = RequestMethod.GET)
    public ResultData teacherGetverifyCode(@RequestParam String username, @RequestParam String email) {

        try {
            if (teacherDao.queryByName(username) != null) {
                return new ResultData(false, RegisterEnum.REPETE_USERNAME.getMessage());
            }
            if (teacherDao.queryEamil(email) != null) {
                return new ResultData(false, RegisterEnum.REPEATE_EMAIL.getMessage());
            }
            if (eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("username", username).setExts("email", email).setExts("status", "register"))) {
                return new ResultData<StudentInfo>(true, "请到您的邮箱查看验证码");
            }
            return new ResultData(false, "服务器内部异常");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }

    }


    /**
     * 学生通过注册验证码，将数据保存在DB中
     * @param registerStudentVO
     * @return
     */
    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    public ResultData StudentSaveToDB(@RequestBody RegisterStudentVO registerStudentVO) {

        try {

            Jedis jedis = jedisAdapter.getJedis();
            String username = registerStudentVO.getStudentInfo().getUsername();
            String redisKey = RedisKey.getBizRegisterKey(username);
            if (jedis.exists(redisKey)) {
                if (jedis.get(redisKey).equals(registerStudentVO.getVerifyCode())) {
                    if (authService.studentRegister(registerStudentVO.getStudentInfo()) == 1) {
                        return new ResultData(true, RegisterEnum.SUCCESS_SAVE.getMessage());
                    }
                    return new ResultData(true, RegisterEnum.FAIL_SAVE.getMessage());
                }
                return new ResultData(false, "验证码错误");
            }
            return new ResultData(false, "请重新获取验证码");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 教师信息保存数据库
     * @param registerTeacherVO
     * @return
     */
    @RequestMapping(value = "/teacher/register", method = RequestMethod.POST)
    public ResultData teacherSaveToDB(@RequestBody RegisterTeacherVO registerTeacherVO) {
        try {

            Jedis jedis = jedisAdapter.getJedis();
            String username = registerTeacherVO.getTeacherInfo().getUsername();
            String redisKey = RedisKey.getBizRegisterKey(username);
            if (jedis.exists(redisKey)) {
                if (jedis.get(redisKey).equals(registerTeacherVO.getVerifyCode())) {
                    if (authService.teacherRegister(registerTeacherVO.getTeacherInfo()) == 1) {
                        return new ResultData(true, RegisterEnum.SUCCESS_SAVE.getMessage());
                    }
                    return new ResultData(true, RegisterEnum.FAIL_SAVE.getMessage());
                }
                return new ResultData(false, "验证码错误");
            }
            return new ResultData(false, "请重新获取验证码");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }



    /**
     * 学生修改密码获取邮件验证码， 通过消息队列
     * @param username
     * @param email
     * @return
     */
    @GetMapping("/student/getVerifyCodeForFindPassword")
    public ResultData studentGetVerifyForUpdate(@RequestParam String username, @RequestParam String email) {
        try {
            StudentInfo studentinfo = studentDao.selectStudent(username);
            if (studentinfo == null) {
                return new ResultData(false, UpdatePwd.NO_USER.getMsg());
            }
            if (!email.equals(studentinfo.getEmail())) {
                return new ResultData(false, UpdatePwd.WRONG_EMALI.getMsg());
            }
//            return new ResultData(true, mailService.sendMailForUpdatePwd(studentinfo.getEmail()));
            if (eventProducer.fireEvent(new EventModel(EventType.MAIL)
                    .setExts("username", username)
                    .setExts("email", email)
                    .setExts("updatePwd", "UPDATE_PWD")
                    .setExts("status", "updatePwd"))) {
                return new ResultData<StudentInfo>(true, "邮件发送成功");
            }
            return new ResultData(false, "发送邮件失败");

        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 老师修改密码获取验证码
     * @param username
     * @param email
     * @return
     */
    @GetMapping("/teacher/getVerifyCodeForFindPassword")
    public ResultData teacherGetVerifyForUpdatePassword(@RequestParam String username, @RequestParam String email) {
        try {
            TeacherInfo teacherinfo = teacherDao.queryByName(username);
            if (teacherinfo == null) {
                return new ResultData(false, UpdatePwd.NO_USER.getMsg());
            }
            if (!email.equals(teacherinfo.getEmail())) {
                return new ResultData(false, UpdatePwd.WRONG_EMALI.getMsg());
            }
            if (eventProducer.fireEvent(new EventModel(EventType.MAIL)
                    .setExts("username", username)
                    .setExts("email", email)
                    .setExts("status", "updatePwd"))) {
                return new ResultData<StudentInfo>(true, "邮件发送成功");
            }
            return new ResultData(false, "发送邮件失败");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage()); }
    }

    /**
     * 学生修改密码操作
     * @param updatePassword
     * @return
     */
    @PostMapping("/student/FindPassword")
    public ResultData studentUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            Jedis jedis = jedisAdapter.getJedis();
            String username = updatePassword.getUsername();
            String redisKey = RedisKey.getBizFindPassword(username);
            if (jedis.exists(redisKey)) {
                if (jedis.get(redisKey).equals(updatePassword.getVerifyCode())) {
                    // authService.studentUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword());
                    if (authService.studentUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword()) != 1) {
                        return new ResultData(false, UpdatePwd.UPDATE_PWD_WRONG.getMsg());
                    }
                    return new ResultData(true, UpdatePwd.UPDATE_PWD_SUCCESS.getMsg());
                }
                return new ResultData(false, "验证码错误");
            }
            return new ResultData(false, "请重新获取验证码");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    /**
     * 老师修改密码
     * @param updatePassword 修改密码信息
     * @return
     */
    @PostMapping("/teacher/FindPassword")
    public ResultData teacherUpdatePassword(@RequestBody UpdatePassword updatePassword) {
        try {
            Jedis jedis = jedisAdapter.getJedis();
            String username = updatePassword.getUsername();
            String redisKey = RedisKey.getBizFindPassword(username);

            if (jedis.exists(redisKey)) {
                if (jedis.get(redisKey).equals(updatePassword.getVerifyCode())) {
                    //authService.teacherUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword());
                    if (authService.teacherUpdatePassword(updatePassword.getUsername(), updatePassword.getPassword()) != 1) {
                        return new ResultData(false, UpdatePwd.UPDATE_PWD_WRONG.getMsg());
                    }
                    return new ResultData(true, UpdatePwd.UPDATE_PWD_SUCCESS.getMsg());
                }
                return new ResultData(false, "验证码错误");
            }
            return new ResultData(false, "请重新获取验证码");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }


    //管理员登录
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResultData createAdminAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {

            JoinProject joinProject = null;

            final String token = authService.adminLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            // Return the token
            String username = authenticationRequest.getUsername();
            String role = adminDao.queryByName(username).getRole();
            boolean isCompleted = false;
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
