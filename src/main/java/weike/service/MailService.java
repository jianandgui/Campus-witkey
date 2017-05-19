package weike.service;

/**
 * Created by muyi on 17-4-22.
 */
public interface MailService {

    public String sendSimpleMail(String to, String subject);

    public void sendMail(String to, String subject,String content);
}
