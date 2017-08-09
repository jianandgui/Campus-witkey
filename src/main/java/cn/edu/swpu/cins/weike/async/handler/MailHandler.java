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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MailHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    @Autowired
    MailService mailService;
    @Autowired
    JedisAdapter jedisAdapter;



    @Override
    public void doHandle(EventModel model){
        try{
            Jedis jedis =jedisAdapter.getJedis();
            if(model.getExts().containsKey("projectName")){
                mailService.sendMailForProject(model.getExts().get("email"),model.getExts().get("username"),model.getExts().get("projectName"));
            }
            else if(model.getExts().containsKey("updatePwd")){
                String toEmail=model.getExts().get("email");
                String verifyCode =mailService.sendMailForUpdatePwd(toEmail);
                jedis.setex(RedisKey.getBizFindPassword(model.getExts().get("username")),1800,verifyCode);
            }
            else {
                String username=model.getExts().get("username");
                String verifyCode=mailService.sendSimpleMail(username,model.getExts().get("email"));
                jedis.setex(RedisKey.getBizRegisterKey(username),1800,verifyCode);
            }
        }catch (Exception e){
            logger.info("出现错误咯！");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MAIL);
    }


}
