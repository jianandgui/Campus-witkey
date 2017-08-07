package cn.edu.swpu.cins.weike.async.handler;

import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MailHandlerTest {

    @MockBean
    MailService mailService;

    @Test
    public void doHandle() throws Exception {

        EventModel model=new EventModel();
        model.setExts("username","testMQ").setExts("email","yangquan95@163.com");

        mailService.sendSimpleMail(model.getExts().get("username"),model.getExts().get("email"));
        assertEquals("testMQ",model.getExts().get("username"));
        assertEquals("yangquan95@163.com",model.getExts().get("email"));

    }

}