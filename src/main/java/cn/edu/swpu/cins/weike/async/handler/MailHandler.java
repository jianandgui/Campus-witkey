package cn.edu.swpu.cins.weike.async.handler;

import cn.edu.swpu.cins.weike.async.EventHandler;
import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MailHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    @Autowired
    MailService mailService;

    @Override
    public void doHandle(EventModel model){
        try{
            mailService.sendMailForProject(model.getExts().get("email"),model.getExts().get("username"),model.getExts().get("projectName"));
        }catch (Exception e){
            logger.info("出现错误咯！");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MAIL);
    }


}
