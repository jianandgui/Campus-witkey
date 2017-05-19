package weike.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import weike.config.filter.JwtTokenUtil;
import weike.dao.AdminDao;
import weike.dao.StudentDao;
import weike.dao.TeacherDao;
import weike.entity.persistence.AdminInfo;
import weike.entity.persistence.StudentInfo;
import weike.entity.persistence.TeacherInfo;
import weike.entity.view.JWTuserFactory;
import weike.service.AuthService;

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
    public int studentRegister(StudentInfo studentinfo) {

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
    }

    @Override
    public String studentLogin(String username, String password) {

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = JWTuserFactory.createStudent(studentDao.selectStudent(username));
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public int teacherRegister(TeacherInfo teacherinfo) {

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
    }






    @Override
    public String teacherLogin(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = JWTuserFactory.createTeacher(teacherDao.queryByName(username));
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    public String adminLogin(String userName, String password){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = JWTuserFactory.createAdmin(adminDao.queryByName(userName));
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;

    }

    @Override
    public int studentUpdatePassword(String username, String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = password;

        return studentDao.updatePassword(username,encoder.encode(rawPassword));
    }

    @Override
    public int teacherUpdatepassword(String username, String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = password;
        return teacherDao.updatePassword(username,encoder.encode(rawPassword));
    }

    public static final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
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
    public String getVerifyCodeForLogin() {


        return getRandomString();
    }

    @Override
    public AdminInfo adminRegister(AdminInfo adminInfo) {
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
    }
}
