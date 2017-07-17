package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by muyi on 17-7-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthServiceImplTest {

    @MockBean
    private StudentDao studentDao;
    @MockBean
    private TeacherDao teacherDao;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthService service;

    @MockBean
    UserDetails userDetails;

    @Test
    public void studentRegister() throws Exception {
        StudentInfo studentInfo=new StudentInfo();
        studentInfo.setEmail("test");
        studentInfo.setLastPasswordResetDate(12312313);
        studentInfo.setRole("ROLE_STUDENT");
        studentInfo.setUsername("test");

        when(studentDao.studntRegister(studentInfo)).thenReturn(1);


    }

    @Test
    public void studentLogin() throws Exception {

        String token=jwtTokenUtil.generateToken(userDetails);
        System.out.println(token);
    }

    @Test
    public void teacherRegister() throws Exception {
    }

    @Test
    public void teacherLogin() throws Exception {
    }

    @Test
    public void adminLogin() throws Exception {
    }

    @Test
    public void studentUpdatePassword() throws Exception {
        assertEquals(1,studentDao.updatePassword("muyi","asdasdasd"));

    }

    @Test
    public void teacherUpdatePassword() throws Exception {
    }

    @Test
    public void getRandomString() throws Exception {

        System.out.println(service.getVerifyCodeForLogin());
    }

    @Test
    public void getVerifyCodeForLogin() throws Exception {
        System.out.println(service.getVerifyCodeForLogin());
    }

    @Test
    public void adminRegister() throws Exception {
    }

}