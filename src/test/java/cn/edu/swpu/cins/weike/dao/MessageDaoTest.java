package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.Message;
import javafx.beans.binding.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by muyi on 17-7-17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MessageDaoTest {

    @Autowired
    private MessageDao messageDao;

//    @MockBean
//    private Message message;

    @Test
    public void addMessage1() throws Exception {
        Message message=new Message();
        message.setCreateDate(new Date());
        message.setToName("test");
//        message.setConversationId("asda");
        message.setHasRead(1);
        message.setFromName("test");
        message.setContent("testMessage");

        System.out.println(messageDao.addMessage(message));
    }

    @Test
    public void getConversationDetail1() throws Exception {
    }

    @Test
    public void getConversationList1() throws Exception {

        System.out.println(messageDao.getConversationList("test"));
    }

    @Test
    public void deleteMessage1() throws Exception {
        assertEquals(1,messageDao.deleteMessage(16));
    }

   

}