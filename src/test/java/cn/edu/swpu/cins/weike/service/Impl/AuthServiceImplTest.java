package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.RegisterStudentVO;
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


    @Autowired
    private AuthService service;


    @Test
    public void studentRegister() throws Exception {
        StudentInfo studentInfo=new StudentInfo();
        studentInfo.setEmail("test@test.com");
        studentInfo.setUsername("tongxue");
        studentInfo.setPassword("123456");
        RegisterStudentVO registerStudentVO = new RegisterStudentVO(studentInfo, "asd");
        System.out.println(service.studentRegister(registerStudentVO));

    }
}