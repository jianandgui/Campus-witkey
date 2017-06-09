package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherInfo;
import cn.edu.swpu.cins.weike.entity.view.JwtUserFactory;
import cn.edu.swpu.cins.weike.exception.AuthException;
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
import cn.edu.swpu.cins.weike.entity.persistence.AdminInfo;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.service.AuthService;

import java.util.Date;
import java.util.Random;

/**
 * Created by muyi on 17-4-18.
 */
@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AdminDao adminDao;

    @Override
    public int studentRegister(StudentInfo studentinfo) throws AuthException{
        try{
            final String username = studentinfo.getUsername();
            if(studentDao.selectStudent(username)!=null) {
                return 0;
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = studentinfo.getPassword();
            studentinfo.setPassword(encoder.encode(rawPassword));
            studentinfo.setLastPasswordResetDate(new Date().getTime());
            studentinfo.setRole("ROLE_STUDENT");
            return studentDao.studntRegister(studentinfo);
        }catch (Exception e){

            throw new AuthException("数据库异常！");
        }

    }

    @Override
    public String studentLogin(String username, String password) throws AuthException{
        try {

            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createStudent(studentDao.selectStudent(username));
            final String token = jwtTokenUtil.generateToken(userDetails);
            return token;
        }catch (Exception e){

            throw new AuthException("获取token异常");

        }

    }

    @Override
    public int teacherRegister(TeacherInfo teacherinfo) throws AuthException{
        try {
            final String username = teacherinfo.getUsername();

            if(teacherDao.queryByName(username)!=null) {
                return 0;
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = teacherinfo.getPassword();
            teacherinfo.setPassword(encoder.encode(rawPassword));
            teacherinfo.setLastPasswordResetDate(new Date().getTime());
            teacherinfo.setRole("ROLE_TEACHER");
            return teacherDao.teacherRegister(teacherinfo);
        }catch (Exception e){
            throw new AuthException("数据库异常");
        }

    }

    @Override
    public String teacherLogin(String username, String password) throws AuthException{

        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createTeacher(teacherDao.queryByName(username));
            final String token = jwtTokenUtil.generateToken(userDetails);
            return token;
        }catch (Exception e){

            throw new AuthException("获取token失败");
        }



    }

    public String adminLogin(String userName, String password) throws AuthException{
        try{
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Reload password post-security so we can generate token
            final UserDetails userDetails = JwtUserFactory.createAdmin(adminDao.queryByName(userName));
            final String token = jwtTokenUtil.generateToken(userDetails);
            return token;
        }catch (Exception e){

            throw new AuthException("获取token失败");
        }

    }

    @Override
    public int studentUpdatePassword(String username, String password) throws AuthException{
        try{
            return studentDao.updatePassword(username,UpdatePwd.updatePwd(password));
        }catch (Exception e){
            throw new AuthException("更新密码异常");
        }

    }

    @Override
    public int teacherUpdatePassword(String username, String password) throws AuthException{
        try{
            return teacherDao.updatePassword(username,UpdatePwd.updatePwd(password));
        }catch (Exception e){
            throw new AuthException("更新密码异常");

        }

    }

    public static final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuioplkjhgfdsazxcvbnm".toCharArray();
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
    public String getVerifyCodeForLogin() throws AuthException {
        try{
            return getRandomString();
        }catch (Exception e){

            throw new AuthException("获取验证码异常");
        }

    }

    @Override
    public AdminInfo adminRegister(AdminInfo adminInfo) throws AuthException{
        try{
            final String username = adminInfo.getUsername();
            if(adminDao.queryByName(username)!=null) {
                return null;
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = adminInfo.getPassword();
            adminInfo.setPassword(encoder.encode(rawPassword));
            adminInfo.setLastPasswordResetDate(new Date().getTime());
            adminInfo.setRole("ROLE_ADMIN");
            return adminDao.addAdmin(adminInfo) == 0 ? null : adminInfo;
        }catch (Exception e){
            throw new AuthException("数据库异常");
        }

    }
}
