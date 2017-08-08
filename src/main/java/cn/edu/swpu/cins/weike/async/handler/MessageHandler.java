package cn.edu.swpu.cins.weike.async.handler;

import cn.edu.swpu.cins.weike.async.EventHandler;
import cn.edu.swpu.cins.weike.async.EventModel;
import cn.edu.swpu.cins.weike.async.EventType;
import cn.edu.swpu.cins.weike.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 这个类想实现的功能是完成参加项目后发送邮件和站内信的异步
 * 但是站内信和邮件理论上应该保持同步
 *  TODO
 *
 */
public class MessageHandler implements EventHandler{


    @Autowired
    private MailService mailService;
    
    @Override
    public void doHandle(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}
