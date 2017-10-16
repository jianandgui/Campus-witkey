package cn.edu.swpu.cins.weike.service.Impl;


import cn.edu.swpu.cins.weike.dao.MessageDao;
import cn.edu.swpu.cins.weike.dao.StudentDao;
import cn.edu.swpu.cins.weike.dao.TeacherDao;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.persistence.StudentInfo;
import cn.edu.swpu.cins.weike.entity.view.JoinMessage;
import cn.edu.swpu.cins.weike.enums.ExceptionEnum;
import cn.edu.swpu.cins.weike.exception.AuthException;
import cn.edu.swpu.cins.weike.exception.MessageException;
import cn.edu.swpu.cins.weike.exception.WeiKeException;
import cn.edu.swpu.cins.weike.service.JoinProjectService;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class JoinProjectServiceImpl implements JoinProjectService {

    private JedisAdapter jedisAdapter;
    private MailService mailService;
    private MessageDao messageDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;
    private GetUsrName getName;

    /**
     * 同意或者拒绝申请，发送邮件和站内信（失败就不发送站内信了）
     * 同时完成redis中的操作
     *
     * @param
     * @param
     * @return
     */

    public Message getMessageForMail(String sender,String saver,String projectName) {
        Message message = new Message();
        String content = "尊敬的" + saver + "您好," + "您在威客平台申请参与的项目" + projectName + "已经成功通过申请,请登录平台查看详情！";
        message.setContent(content);
        message.setProjectAbout(projectName);
        message.setCreateDate(new Date());
        message.setFromName(sender);
        message.setHasRead(0);
        message.setToName(saver);
        return message;
    }

    //比如将正在申请人删除 在申请成功的人中加入
    public void dealRedis(String sender,String saver,String projectName) {
        //正在申请
        String joiningProjectKey = RedisKey.getBizApplyingPro(saver);
        //项目正在申请人
        String projectApplyingKey = RedisKey.getBizProApplying(projectName);
        //申请成功
        String joinProjectSuccessKey = RedisKey.getBizJoinSuccess(saver);
        //项目团队人员（成功通过申请）
        String projectApplySuccessKey = RedisKey.getBizProApplicant(projectName);
        jedisAdapter.srem(joiningProjectKey, projectName);
        jedisAdapter.sadd(joinProjectSuccessKey, projectName);
        jedisAdapter.srem(projectApplyingKey, saver);
        jedisAdapter.sadd(projectApplySuccessKey, saver);
    }

    @Override
    public int acceptJoin(JoinMessage joinMessage, HttpServletRequest request) throws Exception {
        String projectName = joinMessage.getProjectAbout();
        String saver = joinMessage.getProjectApplicant();
        String sender = getName.AllProjects(request);
        Message message = getMessageForMail(sender,saver,projectName);
        try {
            StudentInfo studentInfo = studentDao.selectStudent(saver);
            String email = studentInfo.getEmail();
            mailService.sendMailForJoinPro(email, saver, projectName);
            dealRedis(sender,saver,projectName);
            return messageDao.addMessage(message);
        } catch (Exception e) {
            throw new AuthException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }



    @Override
    public void refuseJoin(JoinMessage joinMessage, HttpServletRequest request) throws Exception {
        String projectName = joinMessage.getProjectAbout();
        String saver = joinMessage.getProjectApplicant();
        try {
//            String sender=getName.AllProjects(request);
            StudentInfo studentInfo = studentDao.selectStudent(saver);
            String email = studentInfo.getEmail();
            mailService.sendMailForJoinPro(email, saver, projectName);
            //正在申请
            String joiningProjectKey = RedisKey.getBizApplyingPro(saver);
            //项目正在申请人
            String projectApplyingKey = RedisKey.getBizProApplying(projectName);
            //申请失败
            String joinProjectFailedKey = RedisKey.getBizJoinFail(saver);
            //项目团队人员（没有通过申请）
            String projectApplyFailKey = RedisKey.getBizProApplyFail(projectName);
            jedisAdapter.srem(joiningProjectKey, projectName);
            jedisAdapter.sadd(joinProjectFailedKey, projectName);
            jedisAdapter.srem(projectApplyingKey, saver);
            jedisAdapter.sadd(projectApplyFailKey, saver);
        } catch (Exception e) {
            throw new AuthException(ExceptionEnum.INNER_ERROR.getMsg());
        }
    }
}
