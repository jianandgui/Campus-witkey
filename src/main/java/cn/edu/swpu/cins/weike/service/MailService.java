package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.exception.MailException;

/**
 * Created by muyi on 17-4-22.
 */
public interface MailService {


    public String sendSimpleMail(String username,String to) throws Exception;
    public String sendMailForUpdatePwd(String to) throws Exception;
    public String sendMailForProject(String email,String username,String projectName) throws Exception;
}
