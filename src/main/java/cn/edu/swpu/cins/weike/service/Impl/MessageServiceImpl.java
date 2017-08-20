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
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.enums.MessageEnum;
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.service.MessageService;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by muyi on 17-6-12.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private MessageDao messageDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;
    private ProjectDao projectDao;
    private MailService mailService;

    @Autowired
    private JedisAdapter jedisAdapter;


    @Autowired
    EventProducer eventProducer;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao, TeacherDao teacherDao, StudentDao studentDao, ProjectDao projectDao, MailService mailService) {
        this.messageDao = messageDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.projectDao = projectDao;
        this.mailService = mailService;
    }

    /*
    事务操作：发送信息的同时给该用户发送一封邮件通知他的项目有新动态
     */

    @Override
    public int addMessage(String content, String projectName, String userSender) throws MessageException {
        try {
            String sender;
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
            message.setContent(content);
            message.setCreateDate(new Date());
            message.setProjectAbout(projectName);

            int num = messageDao.addMessage(message);
            if (num != 1) {
                throw new MessageException(MessageEnum.SEND_MESSAGE_FAILD.getMsg());
            }
            //申请项目
            String joiningProjectKey = RedisKey.getBizApplyingPro(sender);
            //项目正在申请人
            String projectApplyingKey = RedisKey.getBizProApplying(projectName);
            jedisAdapter.sadd(projectApplyingKey, sender);
            jedisAdapter.sadd(joiningProjectKey, projectName);
            return num;
        } catch (Exception e) {
            throw e;
        }
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
        Message message = new Message();
        message.setToName(toName);
        message.setHasRead(0);
        message.setFromName(username);
        message.setCreateDate(new Date());
        message.setProjectAbout(projectName);
        message.setContent("尊敬的" + toName + "你好，" + username + "关注了你的项目 : " + projectName + "去看看吧！");
        try {
            messageDao.addMessage(message);
            jedisAdapter.sadd(followProKey, projectName);
            jedisAdapter.sadd(proFollower, username);
        } catch (Exception e) {
            throw e;
        }
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
}
