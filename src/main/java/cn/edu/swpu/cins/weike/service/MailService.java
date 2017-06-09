package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.exception.MailException;

/**
 * Created by muyi on 17-4-22.
 */
public interface MailService {

    public String sendSimpleMail(String to, String subject) throws Exception;

    public void sendMail(String to, String subject, String content) throws Exception;
}
