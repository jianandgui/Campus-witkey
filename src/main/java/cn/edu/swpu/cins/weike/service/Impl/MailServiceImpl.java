package cn.edu.swpu.cins.weike.service.Impl;

import cn.edu.swpu.cins.weike.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public static final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
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


    @Override
    public String sendSimpleMail(String to, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        String verifyCode=getRandomString();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(verifyCode);

        try {
            sender.send(message);
        } catch (Exception e) {
        }
        return verifyCode;
    }

    @Override
    public void sendMail(String to, String subject,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(forMatInfo(content));
        try {
            sender.send(message);
        } catch (Exception e) {
        }

    }

    //处理发送邮件信息

    public String forMatInfo(String content){
        return content="尊敬的老师你好，我是"+content+"我想报名参加您的项目";
    }


}
