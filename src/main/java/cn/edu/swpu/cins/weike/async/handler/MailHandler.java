package cn.edu.swpu.cins.weike.async.handler;

import cn.edu.swpu.cins.weike.async.EventHandler;
import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

@Component
public class MailHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    @Autowired
    MailService mailService;
    @Autowired
    JedisAdapter jedisAdapter;



    @Override
    public void doHandle(EventModel model) {
        try{
            String status=model.getExts().get("status");
            String verifyCode;
            switch (status) {
                case "joinPro" :
                    mailService.sendMailForProject(model.getExts().get("email"),
                            model.getExts().get("username"),
                            model.getExts().get("projectName"));
                    break;
                case "updatePwd" :
                    String toEmail=model.getExts().get("email");
                    verifyCode =mailService.sendMailForUpdatePwd(toEmail);
                    jedisAdapter.setex(RedisKey.getBizFindPassword(model.getExts().get("username")),1800,verifyCode);
                    break;
                case "register" :
                    String username=model.getExts().get("username");
                    verifyCode=mailService.sendSimpleMail(username,model.getExts().get("email"));
                    jedisAdapter.setex(RedisKey.getBizRegisterKey(username),1800,verifyCode);
                    break;
                default:
                    throw new Exception("服务器内部异常");
        }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MAIL);
    }


}
