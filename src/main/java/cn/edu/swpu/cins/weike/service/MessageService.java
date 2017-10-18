package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.view.JwtAuthenticationResponse;
import cn.edu.swpu.cins.weike.entity.view.MessageList;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.exception.AuthException;
import cn.edu.swpu.cins.weike.exception.MessageException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
public interface MessageService {

    int addMessage(String content,String projectName,String userSender) throws MessageException;

    List<Message> getConversationDetail(String conversationId) throws MessageException;

    MessageList getConversationList(String username) throws MessageException;

    int deleteMessage(int id) throws MessageException;

    void followPro(String projectName,String username,String toName);

    void unFollowPro(String projectName,String username,String toName);

    List<String> queryFollower(String projectName);

    List<ProjectDetail> queryFollowPros(HttpServletRequest request);

    JwtAuthenticationResponse loginByToken(HttpServletRequest request) throws AuthException;
}
