package cn.edu.swpu.cins.weike.service;


/**
 * Created by muyi on 17-4-22.
 */
public interface MailService {


     String sendSimpleMail(String username,String to) throws Exception;

     String sendMailForUpdatePwd(String to) throws Exception;

     void sendMailForProject(String email,String username,String projectName) throws Exception;

     void sendMailForJoinPro(String email,String username,String projectName) throws Exception;

}
