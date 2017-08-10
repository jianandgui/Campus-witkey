package cn.edu.swpu.cins.weike.service.Impl;


import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.JoinMessage;
import cn.edu.swpu.cins.weike.service.JoinProjectService;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class JoinProjectServiceImpl implements JoinProjectService{

    JedisAdapter jedisAdapter;
    MailService mailService;
    MessageDao messageDao;

    TeacherDao teacherDao;
    StudentDao studentDao;

    @Autowired
    public JoinProjectServiceImpl(JedisAdapter jedisAdapter, MailService mailService, MessageDao messageDao, TeacherDao teacherDao, StudentDao studentDao) {
        this.jedisAdapter = jedisAdapter;
        this.mailService = mailService;
        this.messageDao = messageDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
    }

    /**
     * 同意或者拒绝申请，发送邮件和站内信（失败就不发送站内信了）
     * 同时完成redis中的操作
     * @param joinMessage
     * @param principal
     * @return
     */
    @Override
    public int acceptJoin(JoinMessage joinMessage,Principal principal) {
        String projectName=joinMessage.getProjectAbout();
        String saver=joinMessage.getProjectApplicant();

        String sender=principal.getName();

        StudentInfo studentInfo=studentDao.queryEmail(saver);
        String email=studentInfo.getEmail();
        mailService.sendMailForJoinPro(email,saver,projectName);

        Message message=null;
        String content="尊敬的"+saver+"您好,"+"您在威客平台申请参与的项目"+projectName+"已经成功通过申请,请登录平台查看详情！";
        message.setContent(content);
        message.setProjectAbout(projectName);
        message.setCreateDate(new Date());
        message.setFromName(sender);
        message.setHasRead(0);
        message.setToName(saver);

        //正在申请
        String joiningProjectKey= RedisKey.getBizApplyingPro(saver);
        //项目正在申请人
        String projectAppllyingKey=RedisKey.getBizProApplying(projectName);

        //申请成功
        String joinProjectSuccessKey = RedisKey.getBizJoinSuccess(saver);
        //项目团队人员（成功通过申请）
        String projectAppllySuccessKey = RedisKey.getBizProApplicant(projectName);


        jedisAdapter.srem(joiningProjectKey,projectName);
        jedisAdapter.sadd(joinProjectSuccessKey,projectName);

        jedisAdapter.srem(projectAppllyingKey,saver);
        jedisAdapter.sadd(projectAppllySuccessKey,saver);


        return messageDao.addMessage(message);
    }

    @Override
    public int refuseJoin(JoinMessage joinMessage,Principal principal) {
        return 0;
    }


    //用来返回
}
