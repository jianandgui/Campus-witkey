package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationRequest;
import cn.edu.swpu.cins.weike.service.AuthService;
import cn.edu.swpu.cins.weike.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by muyi on 17-7-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;
//    @Autowired
//    private StudentDao studentDao;
    @Autowired
    private AuthController authController;

    private MockMvc mockMvc;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
    @Test
    public void createStudentAuthenticationToken() throws Exception {

        JwtAuthenticationRequest request=new JwtAuthenticationRequest("muyi","1234");
        mockMvc.perform(MockMvcRequestBuilders.post("/weike/student/login").param("muyi","1234")
                .accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
//        System.out.println(authService.studentLogin("muyi","1234"));
    }

}