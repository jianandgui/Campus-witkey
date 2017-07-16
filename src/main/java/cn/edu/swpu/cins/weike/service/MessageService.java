package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.view.MessageList;
import cn.edu.swpu.cins.weike.exception.MessageException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
public interface MessageService {

    public int addMessage(String content,String projectName,String userSender) throws MessageException;

    public List<Message> getConversationDetail(String conversationId) throws MessageException;

    public MessageList getConversationList(String username) throws MessageException;

    public int deleteMessage(int id) throws MessageException;
}
