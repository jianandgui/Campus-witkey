package cn.edu.swpu.cins.weike.service.Impl;


import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.JoinMessage;
import cn.edu.swpu.cins.weike.service.JoinProjectService;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    GetUsrName getName;

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
     * @param request
     * @return
     */
    @Override
    public int acceptJoin(JoinMessage joinMessage,HttpServletRequest request) {
        String projectName=joinMessage.getProjectAbout();
        String saver=joinMessage.getProjectApplicant();

        String sender=getName.AllProjects(request);

        StudentInfo studentInfo=studentDao.selectStudent(saver);
        String email=studentInfo.getEmail();
        mailService.sendMailForJoinPro(email,saver,projectName);

        Message message=new Message();
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
        String projectApplyingKey=RedisKey.getBizProApplying(projectName);

        //申请成功
        String joinProjectSuccessKey = RedisKey.getBizJoinSuccess(saver);
        //项目团队人员（成功通过申请）
        String projectApplySuccessKey = RedisKey.getBizProApplicant(projectName);


        jedisAdapter.srem(joiningProjectKey,projectName);
        jedisAdapter.sadd(joinProjectSuccessKey,projectName);

        jedisAdapter.srem(projectApplyingKey,saver);
        jedisAdapter.sadd(projectApplySuccessKey,saver);


        return messageDao.addMessage(message);
    }

    @Override
    public int refuseJoin(JoinMessage joinMessage,HttpServletRequest request) {
        return 0;
    }


    //用来返回
}
