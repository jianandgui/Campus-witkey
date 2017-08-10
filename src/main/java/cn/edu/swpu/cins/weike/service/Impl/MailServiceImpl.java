package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by muyi on 17-4-22.
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送纯文本的简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */

//    private String to = "yangquan95@163.com";
    //    获取4位随机数(验证码)
    public static final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm".toCharArray();
    public static Random random = new Random();

    public static String getRandomString() {
        StringBuffer buffer = new StringBuffer();
        int index;   //获取随机chars下标
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(chars.length);  //获取随机chars下标
            buffer.append(chars[index]);
        }
        return buffer.toString();
    }


    public String getSignUpSubject(String username) {
        final String format = "尊敬的username，欢迎注册校园威客平台！";
        return format.replaceAll("username", username);
    }

    public String getSignUpContent() {
        final String previous = "请您在威客平台注册页面输入下面的验证码以激活您的邮箱，倘若非您本人操作，请忽略这封邮件，验证码为：";
        return previous;
    }

    @Override
    public String sendSimpleMail(String username,String to)  throws Exception{
        try {
            String verifyCode=getRandomString();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(getSignUpSubject(username));
            message.setText(getSignUpContent()+verifyCode+",验证码有效期为30分钟。");
            sender.send(message);
            return verifyCode;
        } catch (Exception e) {
            throw new Exception("邮件发送失败");
        }
    }

    @Override
    public String sendMailForUpdatePwd(String to) throws Exception {
        try {
            String verifyCode=getRandomString();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("校园威客平台修改密码");
            message.setText("请记住下面的验证码，将用于修改您的密码，倘若非您本人操作，请忽略这封邮件，验证码为："+verifyCode+",验证码有效期为30分钟。");
            sender.send(message);
            return verifyCode;
        } catch (Exception e) {
            throw new Exception("邮件发送失败");
        }
    }

    @Override
    public String sendMailForProject(String email,String username,String projectName) throws Exception{
        try {
            String verifyCode=getRandomString();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email);
            message.setSubject("威客平台消息");
            message.setText(forMatInfo(username,projectName));
            sender.send(message);
            return verifyCode;
        } catch (Exception e) {
            throw new Exception("邮件发送失败");
        }
    }

    //处理发送邮件信息

    public String forMatInfo(String username,String projectName) {
        return "尊敬的"+username+"您好，您在威客平台发布的项目:"+projectName+"有新动态，请您登录平台查看详情!";
    }

}
