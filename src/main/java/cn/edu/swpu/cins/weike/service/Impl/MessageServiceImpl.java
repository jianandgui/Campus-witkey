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
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public int addMessage(String content, String projectName, String userSender) throws MessageException{
        try {
            Message message = new Message();
            StudentDetail studentSender = studentDao.queryForStudentPhone(userSender);
            TeacherDetail teacherSender = teacherDao.queryForPhone(userSender);
            String userSaver = projectDao.queryProjectDetail(projectName).getProjectConnector();
            StudentDetail studentSaver = studentDao.queryForStudentPhone(userSaver);
            TeacherDetail teacherSaver = teacherDao.queryForPhone(userSaver);
            String email = projectDao.queryProjectDetail(projectName).getEmail();
            if (studentSender != null) {
                message.setFromName(studentSender.getUsername());
            } else
                message.setFromName(teacherSender.getUsername());
            if (studentSaver != null) {
                message.setToName(studentSaver.getUsername());
//                mailService.sendMailForProject(email, studentSaver.getUsername(), projectName);
                eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("email",email)
                             .setExts("username",studentSaver.getUsername())
                             .setExts("projectName",projectName));
            } else {
                message.setToName(teacherSaver.getUsername());
//                mailService.sendMailForProject(email, teacherSaver.getUsername(), projectName)
                eventProducer.fireEvent(new EventModel(EventType.MAIL).setExts("email",email)
                        .setExts("username",teacherSaver.getUsername())
                        .setExts("projectName",projectName));}
            message.setContent(content);
            message.setCreateDate(new Date());
            return messageDao.addMessage(message);
        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
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
    public MessageList getConversationList(String username) throws MessageException{
        try {
            StudentDetail studentDetail = null;
            studentDetail=studentDao.queryForStudentPhone(username);
            TeacherDetail teacherDetail = null;
            teacherDetail=teacherDao.queryForPhone(username);
            int userId=0;
            String userName=null;
            if (studentDetail != null) {
                userId = studentDetail.getId();

            } else {
                userId = teacherDetail.getId();

            }
//            return null;
            List<Message> fromMessages=new ArrayList<Message>();
            List<Message> toMessages=new ArrayList<Message>();
            for (Message message:messageDao.getConversationList(username)){
                if(message.getToName().equals(username)){
                    fromMessages.add(message);}
                else {
                    toMessages.add(message);}
            }
            MessageList messageList=new MessageList(fromMessages,toMessages);
            return messageList;

        } catch (Exception e) {
            throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }
    @Override
    public int deleteMessage(int id) throws MessageException {
       try{
           return messageDao.deleteMessage(id)>0?1:0;
       } catch (Exception e){
           throw new MessageException(ExceptionEnum.INNER_ERROR.getMsg());
       }

    }
}
