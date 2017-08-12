package cn.edu.swpu.cins.weike.service;

import cn.edu.swpu.cins.weike.exception.MailException;

/**
 * Created by muyi on 17-4-22.
 */
public interface MailService {


     String sendSimpleMail(String username,String to);
     String sendMailForUpdatePwd(String to) ;
     void sendMailForProject(String email,String username,String projectName);
     void sendMailForJoinPro(String email,String username,String projectName) ;

}
