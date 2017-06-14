package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.dao.ProjectDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.persistence.StudentDetail;
import cn.edu.swpu.cins.weike.entity.persistence.TeacherDetail;
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ProjectDao projectDao;

    @Override
    public int addMessage(String content, String projectName, String userSender) throws MessageException {
        try {
            Message message = new Message();
            StudentDetail studentSender = studentDao.queryForStudentPhone(userSender);
            TeacherDetail teacherSender = teacherDao.queryForPhone(userSender);
            String userSaver = projectDao.queryProjectDetail(projectName).getProjectConnector();
            StudentDetail studentSaver = studentDao.queryForStudentPhone(userSaver);
            TeacherDetail teacherSaver = teacherDao.queryForPhone(userSaver);
            if (studentSender != null) {
                message.setFromId(studentSender.getId());
            } else
                message.setFromId(teacherSender.getId());

            if (studentSaver != null) {
                message.setToId(studentSaver.getId());
            } else
                message.setToId(teacherSaver.getId());
            message.setContent(content);
            message.setCreateDate(new Date());
            return messageDao.addMessage(message);
        } catch (Exception e) {
            throw new MessageException("发送信息失败！");
        }

    }

    @Override
    public List<Message> getConversationDetail(String conversationId) throws MessageException {
        try {
            return messageDao.getConversationDetail(conversationId);
        } catch (Exception e) {
            throw new MessageException("获取信息失败");
        }

    }

    @Override
    public List<Message> getConversationList(String username) throws MessageException {
        try {
            StudentDetail studentDetail = studentDao.queryForStudentPhone(username);
            TeacherDetail teacherDetail = teacherDao.queryForPhone(username);
            int userId;
            if (studentDetail != null) {
                userId = studentDetail.getId();
            } else {
                userId = teacherDao.queryForPhone(username).getId();
            }
            return messageDao.getConversationList(userId);
        } catch (Exception e) {
            throw new MessageException("服务器异常，获取信息失败");
        }

    }
}
