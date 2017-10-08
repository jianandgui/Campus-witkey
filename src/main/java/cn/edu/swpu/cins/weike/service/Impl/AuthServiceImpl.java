package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventProducer;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.*;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.enums.*;
import cn.edu.swpu.cins.weike.exception.AuthException;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import cn.edu.swpu.cins.weike.util.UpdatePwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import cn.edu.swpu.cins.weike.dao.AdminDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.service.AuthService;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-4-18.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private StudentDao studentDao;
    private TeacherDao teacherDao;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private AdminDao adminDao;


    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    EventProducer eventProducer;


    @Autowired
    public AuthServiceImpl(StudentDao studentDao, TeacherDao teacherDao, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, AdminDao adminDao) {
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.adminDao = adminDao;
    }

    @Override
    public int studentRegister(RegisterStudentVO registerStudentVO) throws AuthException {
        try {
            final String username = registerStudentVO.getStudentInfo().getUsername();
            registerCheck(username,registerStudentVO.getVerifyCode(),"STUDENT");
            StudentInfo studentInfo = registerStudentVO.getStudentInfo();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = studentInfo.getPassword();
            studentInfo.setPassword(encoder.encode(rawPassword));
            studentInfo.setLastPasswordResetDate(new Date().getTime());
            studentInfo.setRole("ROLE_STUDENT");
            if (studentDao.studntRegister(studentInfo) != 1) {
                throw new AuthException(RegisterEnum.FAIL_SAVE.getMessage());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    public  int registerCheck(String username,String verifyCode,String role) {
        if (studentDao.selectStudent(username) != null && teacherDao.queryByName(username) != null) {
            return 0;
        }
        String redisKey = RedisKey.getBizRegisterKey(username);
        if (!jedisAdapter.exists(redisKey)) {
            throw new AuthException(VerifyCodeEnum.GET_CODE_AGAIN.getMsg());
        }
        if (!jedisAdapter.get(redisKey).equals(verifyCode)) {
            throw new AuthException(VerifyCodeEnum.CODE_ERROR.getMsg());
        }
        switch (role) {
            case "TEACHER":
                if(teacherDao.queryByName(username) !=null){
                    throw new AuthException(UserEnum.REPEATE_REGISTER.getMsg());
                }
                break;
            case "STUDENT":
                if (studentDao.selectStudent(username) != null) {
                    throw new AuthException(UserEnum.REPEATE_REGISTER.getMsg());
                }
                break;
            default:
                throw new AuthException("注册失败");
        }
        return 1;
    }

    @Override
    public JwtAuthenticationResponse studentLogin(JwtAuthenticationRequest authenticationRequest, String captchaCode) throws AuthException {

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        try {
            String loginKey = captchaCode;
            if (!jedisAdapter.exists(loginKey)) {
                throw new AuthException(VerifyCodeEnum.GET_CODE_AGAIN.getMsg());
            }
            String code = jedisAdapter.get(loginKey);
            if (!code.equals(authenticationRequest.getVerifyCode())) {
                throw new AuthException(VerifyCodeEnum.CODE_ERROR.getMsg());
            }
            StudentInfo studentInfo = studentDao.selectStudent(authenticationRequest.getUsername());

            if (studentInfo == null) {
                throw new AuthException(LoginEnum.NO_USER.getMessage());
            }
            StudentDetail studentDetail = studentDao.queryForStudentPhone(authenticationRequest.getUsername());
            JoinProject joinProject = loginUtils(authenticationRequest);
            joinProject.setReleased(studentDao.queryAllProject(authenticationRequest.getUsername()));
            String image = null;
            boolean isCompleted;
            if (studentDetail != null) {
                image = studentDetail.getImage();
                isCompleted = true;
            } else {
                isCompleted = false;
            }
            String username = studentInfo.getUsername();
            String role = studentInfo.getRole();
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createStudent(studentDao.selectStudent(username));
            final String token = jwtTokenUtil.generateToken(userDetails);

            JwtAuthenticationResponse response = new JwtAuthenticationResponse(token, username, role, image, isCompleted, joinProject);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int teacherRegister(RegisterTeacherVO registerTeacherVO) throws AuthException {

        String username = registerTeacherVO.getTeacherInfo().getUsername();
        try {
            registerCheck(username, registerTeacherVO.getVerifyCode(), "TEACHER");
            TeacherInfo teacherInfo = registerTeacherVO.getTeacherInfo();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = registerTeacherVO.getTeacherInfo().getPassword();
            teacherInfo.setPassword(encoder.encode(rawPassword));
            teacherInfo.setLastPasswordResetDate(new Date().getTime());
            teacherInfo.setRole("ROLE_TEACHER");
            if (teacherDao.teacherRegister(teacherInfo) != 1) {
                throw new AuthException(RegisterEnum.FAIL_SAVE.getMessage());
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 工具类方法用去去除重复代码
     *
     * @param authenticationRequest
     * @return
     */
    public JoinProject loginUtils(JwtAuthenticationRequest authenticationRequest) {
        JoinProject joinProject = new JoinProject();
        String applyingProKey = RedisKey.getBizApplyingPro(authenticationRequest.getUsername());
        String applySuccessKey = RedisKey.getBizJoinSuccess(authenticationRequest.getUsername());
        String applyFailedKey = RedisKey.getBizJoinFail(authenticationRequest.getUsername());
        String followerProKey = RedisKey.getBizAttentionPro(authenticationRequest.getUsername());
        joinProject.setJoining((jedisAdapter.smenber(applyingProKey).stream().collect(Collectors.toList())));
        joinProject.setJoinSuccess(jedisAdapter.smenber(applySuccessKey).stream().collect(Collectors.toList()));
        joinProject.setJoinFailed(jedisAdapter.smenber(applyFailedKey).stream().collect(Collectors.toList()));
        joinProject.setFollowPro(jedisAdapter.smenber(followerProKey).stream().collect(Collectors.toList()));
        return joinProject;
    }

    @Override
    public JwtAuthenticationResponse teacherLogin(JwtAuthenticationRequest authenticationRequest, String captchaCode) throws AuthException {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        try {
            String loginKey = captchaCode;
            if (!jedisAdapter.exists(loginKey)) {
                throw new AuthException(VerifyCodeEnum.GET_CODE_AGAIN.getMsg());
            }
            String code = jedisAdapter.get(loginKey);
            if (!code.equals(authenticationRequest.getVerifyCode())) {
                throw new AuthException(VerifyCodeEnum.CODE_ERROR.getMsg());
            }

            TeacherDetail teacherDetail = teacherDao.queryForPhone(authenticationRequest.getUsername());
            TeacherInfo teacherInfo = teacherDao.queryByName(authenticationRequest.getUsername());
            if (teacherInfo == null) {
                throw new AuthException(LoginEnum.NO_USER.getMessage());
            }
            JoinProject joinProject = loginUtils(authenticationRequest);
            joinProject.setReleased(teacherDao.queryAllProject(authenticationRequest.getUsername()));
            String image = null;
            //判断个人信息是否完整
            boolean isCompleted;
            if (teacherDetail != null) {
                image = teacherDetail.getImage();
                isCompleted = true;
            } else {
                isCompleted = false;
            }
            String username = teacherInfo.getUsername();
            String role = teacherInfo.getRole();

            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createTeacher(teacherDao.queryByName(username));
            final String token = jwtTokenUtil.generateToken(userDetails);

            JwtAuthenticationResponse response = new JwtAuthenticationResponse(token, username, role, image, isCompleted, joinProject);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }


    public String adminLogin(String userName, String password) throws AuthException {
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createAdmin(adminDao.queryByName(userName));
            final String token = jwtTokenUtil.generateToken(userDetails);
            return token;
        } catch (Exception e) {

            throw new AuthException(LoginEnum.ERROR_LOGIN.getMessage());
        }
    }

    @Override
    public int studentUpdatePassword(UpdatePassword updatePassword) throws AuthException {
        try {
            return updateUtils(updatePassword, "student");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int teacherUpdatePassword(UpdatePassword updatePassword) throws AuthException {
        try {
            return updateUtils(updatePassword, "teacher");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 工具类方法 去掉修改密码的重复代码
     *
     * @param
     * @return
     */
    public int updateUtils(UpdatePassword updatePassword, String role) throws AuthException {
        try {

            String username = updatePassword.getUsername();
            String redisKey = RedisKey.getBizFindPassword(username);
            if (!jedisAdapter.exists(redisKey)) {
                throw new AuthException(VerifyCodeEnum.GET_CODE_AGAIN.getMsg());
            }
            if (!jedisAdapter.get(redisKey).equals(updatePassword.getVerifyCode())) {
                throw new AuthException(VerifyCodeEnum.CODE_ERROR.getMsg());
            }
            if (role.equals("student")) {
                if (studentDao.updatePassword(username, UpdatePwd.updatePwd(updatePassword.getPassword())) != 1) {
                    throw new AuthException(UpdatePwdEnum.UPDATE_PWD_WRONG.getMsg());
                }
                return 1;
            } else {
                if (teacherDao.updatePassword(username, UpdatePwd.updatePwd(updatePassword.getPassword())) != 1) {
                    throw new AuthException(UpdatePwdEnum.UPDATE_PWD_WRONG.getMsg());
                }
                return 1;
            }
        } catch (Exception e) {
            throw e;
        }
    }


/*    public static final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuioplkjhgfdsazxcvbnm".toCharArray();
    public static Random random = new Random();

    public static String getRandomString() {
        StringBuffer buffer = new StringBuffer();
        int index;   //获取随机chars下标
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(chars.length);  //获取随机chars下标
            buffer.append(chars[index]);
        }
        return buffer.toString();
    }

    @Override
    public String  getVerifyCodeForLogin() throws AuthException {
        try {
            return getRandomString();
        } catch (Exception e) {
            throw new AuthException("获取验证码异常");
        }
    }*/

    @Override
    public AdminInfo adminRegister(AdminInfo adminInfo) throws AuthException {
        try {
            final String username = adminInfo.getUsername();
            if (adminDao.queryByName(username) != null) {
                return null;
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = adminInfo.getPassword();
            adminInfo.setPassword(encoder.encode(rawPassword));
            adminInfo.setLastPasswordResetDate(new Date().getTime());
            adminInfo.setRole("ROLE_ADMIN");
            return adminDao.addAdmin(adminInfo) == 0 ? null : adminInfo;
        } catch (Exception e) {
            throw new AuthException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public void studentGetVerifyCodeForRegister(String username, String email) throws AuthException {
        try {
            if (studentDao.selectStudent(username) != null && teacherDao.queryByName(username) != null) {
                throw new AuthException(RegisterEnum.REPETE_USERNAME.getMessage()); }
            if (studentDao.queryEmail(email) != null) {
                throw new AuthException(RegisterEnum.REPEATE_EMAIL.getMessage()); }
            eventProducerUtils(username, email, "register");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void teacherGetVerifyCodeForRegister(String username, String email) throws AuthException {
        try {
            if (teacherDao.queryByName(username) != null && studentDao.selectStudent(username) != null) {
                throw new AuthException(RegisterEnum.REPETE_USERNAME.getMessage()); }
            if (teacherDao.queryEamil(email) != null) {
                throw new AuthException(RegisterEnum.REPEATE_EMAIL.getMessage()); }
            eventProducerUtils(username, email, "register");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 发送邮件工具方法 去除重复代码
     *
     * @param username
     * @param email
     */
    public void eventProducerUtils(String username, String email, String status) throws AuthException {
        try {
            switch (status) {
                case "register":
                    eventProducer.fireEvent(new EventModel(EventType.MAIL)
                            .setExts("username", username)
                            .setExts("email", email)
                            .setExts("status", "register"));
                    break;

                case "findPwd":
                    eventProducer.fireEvent(new EventModel(EventType.MAIL)
                            .setExts("username", username)
                            .setExts("email", email)
                            .setExts("status", "updatePwd"));
                    break;

                default:
                    throw new AuthException(ExceptionEnum.INNER_ERROR.getMsg());
            }

        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void studentGetVerifyCodeForFindPassword(String username, String email) throws AuthException {
        try {
            StudentInfo studentinfo = studentDao.selectStudent(username);
            if (studentinfo == null) {
                throw new AuthException(UpdatePwdEnum.NO_USER.getMsg());
            }
            if (!email.equals(studentinfo.getEmail())) {
                throw new AuthException(UpdatePwdEnum.WRONG_EMALI.getMsg());
            }
            eventProducerUtils(username, email, "findPwd");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void teacherGetVerifyCodeForFindPassword(String username, String email) throws AuthException {
        try {
            TeacherInfo teacherinfo = teacherDao.queryByName(username);
            if (teacherinfo == null) {
                throw new AuthException(UpdatePwdEnum.NO_USER.getMsg());
            }
            if (!email.equals(teacherinfo.getEmail())) {
                throw new AuthException(UpdatePwdEnum.WRONG_EMALI.getMsg());
            }
            eventProducerUtils(username, email, "findPwd");
        } catch (Exception e) {
            throw e;
        }
    }
}

