package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventProducer;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.entity.view.MessageList;
import cn.edu.swpu.cins.weike.entity.view.ProjectDetail;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.MessageEnum;
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.exception.ProjectException;
import cn.edu.swpu.cins.weike.exception.WeiKeException;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.service.MessageService;
import cn.edu.swpu.cins.weike.service.ProjectService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-6-12.
 */
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageDao messageDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;
    private ProjectDao projectDao;
    private MailService mailService;
    private JedisAdapter jedisAdapter;
    private ProjectService projectService;
    private GetUsrName getUsrName;
    private EventProducer eventProducer;



    /*
    事务操作：发送信息的同时给该用户发送一封邮件通知他的项目有新动态
     */

    @Override
    @Transactional(rollbackFor = {SQLException.class,RuntimeException.class, WeiKeException.class, MessagingException.class})
    public int addMessage(String content, String projectName, String userSender) throws MessageException {
        try {
            String sender=null;
            sendApplyMessage(content, projectName, userSender, sender);
            return 1;
        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    //redis申请项目操作
    public void redisApplyPro(String sender,String projectName) {
        //申请项目
        String joiningProjectKey = RedisKey.getBizApplyingPro(sender);
        //项目正在申请人
        String projectApplyingKey = RedisKey.getBizProApplying(projectName);
        jedisAdapter.sadd(projectApplyingKey, sender);
        jedisAdapter.sadd(joiningProjectKey, projectName);
    }

    public void sendApplyMessage(String content, String projectName, String userSender,String sender) {
        Message message = new Message();
        StudentDetail studentSender = studentDao.queryForStudentPhone(userSender);
        TeacherDetail teacherSender = teacherDao.queryForPhone(userSender);
        String userSaver = projectDao.queryProjectDetail(projectName).getProjectConnector();
        StudentDetail studentSaver = studentDao.queryForStudentPhone(userSaver);
        TeacherDetail teacherSaver = teacherDao.queryForPhone(userSaver);
        String email = projectDao.queryProjectDetail(projectName).getEmail();
        if (studentSender != null) {
            sender = studentSender.getUsername();
            message.setFromName(studentSender.getUsername());
        } else {
            message.setFromName(teacherSender.getUsername());
            sender = teacherSender.getUsername();
        }
        //修改redis中的情况
        redisApplyPro(sender,projectName);
        message = sendMailForApply(message, studentSaver, teacherSaver, email, projectName);
        message.setContent(content);
        message.setCreateDate(new Date());
        message.setProjectAbout(projectName);
        int num = messageDao.addMessage(message);
        if (num != 1) {
            throw new MessageException(MessageEnum.SEND_MESSAGE_FAILD.getMsg());
        }
    }

    public Message sendMailForApply(Message message,StudentDetail studentSaver,TeacherDetail teacherSaver,String email,String projectName ) {
        if (studentSaver != null) {
            message.setToName(studentSaver.getUsername());
            eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("email", email)
                    .setExts("username", studentSaver.getUsername())
                    .setExts("projectName", projectName)
                    .setExts("status", "joinPro"));
        } else {
            message.setToName(teacherSaver.getUsername());
            eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("email", email)
                    .setExts("username", teacherSaver.getUsername())
                    .setExts("projectName", projectName)
                    .setExts("status", "joinPro"));
        }
        return message;
    }

    /*
    获取该条站内信详细信息
     */

    @Override
    public List<Message> getConversationDetail(String conversationId) throws MessageException {
        try {
            return messageDao.getConversationDetail(conversationId);
        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    /*
  实现思路：查询两张表，看是否存在该用户（两张表相互都不能存在同名）
   */
    @Override
    public MessageList getConversationList(String username) throws MessageException {
        try {
            List<Message> fromMessages = new ArrayList<Message>();
            List<Message> toMessages = new ArrayList<Message>();
            for (Message message : messageDao.getConversationList(username)) {
                if (message.getToName().equals(username)) {
                    fromMessages.add(message);
                } else {
                    toMessages.add(message);
                }
            }
            MessageList messageList = new MessageList(fromMessages, toMessages);
            return messageList;
        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public int deleteMessage(int id) throws MessageException {
        try {
            return messageDao.deleteMessage(id);
        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }

    @Override
    public void followPro(String projectName, String username, String toName) {
        String followProKey = RedisKey.getBizAttentionPro(username);
        String proFollower = RedisKey.getBizProFollower(projectName);
        Message message = createMessage(projectName, username, toName);
        try {
            messageDao.addMessage(message);
            jedisAdapter.sadd(followProKey, projectName);
            jedisAdapter.sadd(proFollower, username);
        } catch (Exception e) {
            throw e;
        }
    }

    public Message createMessage(String projectName, String username, String toName) {
        Message message = new Message();
        message.setToName(toName);
        message.setHasRead(0);
        message.setToName("系统消息");
        message.setFromName(username);
        message.setCreateDate(new Date());
        message.setProjectAbout(projectName);
        message.setContent("尊敬的" + toName + "你好，" + username + "关注了你的项目 : " + projectName + "去看看吧！");
        return message;
    }

    @Override
    public void unFollowPro(String projectName, String username, String toName) {
        String followProKey = RedisKey.getBizAttentionPro(username);
        String proFollower = RedisKey.getBizProFollower(projectName);
        try {
            jedisAdapter.srem(followProKey, projectName);
            jedisAdapter.srem(proFollower, username);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<String> queryFollower(String projectName) {
        String proFollowerKeys = RedisKey.getBizProFollower(projectName);
        try {
            return jedisAdapter.smenber(proFollowerKeys).stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ProjectDetail> queryFollowPros(HttpServletRequest request) {
        String username = getUsrName.AllProjects(request);
        String key = RedisKey.getBizAttentionPro(username);
        List<String> proNames;
        List<ProjectDetail> projectDetailList;
        try {
            proNames = jedisAdapter.smenber(key).stream().collect(Collectors.toList());
            projectDetailList = new ArrayList<>();
            proNames.stream().forEach(proName ->{
                projectDetailList.add(projectService.showProject(proName).getProjectDetails());

            });
        } catch (ProjectException e) {
            throw new ProjectException(ExceptionEnum.INNER_ERROR.getMsg());
        }
        return projectDetailList;
    }
}
