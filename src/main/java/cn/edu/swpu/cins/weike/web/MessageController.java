package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.view.MessageList;
import cn.edu.swpu.cins.weike.entity.view.MessageView;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.enums.MessageEnum;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.service.MessageService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/WeiKe")
public class MessageController {
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")

    private String tokenHead;
    private MessageService messageService;
    private JwtTokenUtil jwtTokenUtil;
    private GetUsrName getUsrName;
    private MailService service;

    @Autowired
    public MessageController(MessageService messageService, JwtTokenUtil jwtTokenUtil, GetUsrName getUsrName, MailService service) {
        this.messageService = messageService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.getUsrName = getUsrName;
        this.service = service;
    }

    @PostMapping("/sendMessage")
    public ResultData sendMessage(@RequestBody MessageView messageView, HttpServletRequest request){
        try{
            String userSender = getUsrName.AllProjects(request);
            int num=messageService.addMessage(messageView.getContent(),messageView.getProjectName(),userSender);
            if(num==1){
                return new ResultData(true, MessageEnum.SEND_MESSAGE_SUCCESS.getMsg());}
            else
                return new ResultData(false,MessageEnum.SEND_MESSAGE_FAILD.getMsg());
        }catch (Exception e){
            return new ResultData(false,e.getMessage());
        }


    }
    //获取与某人的通信信息
    @GetMapping("/messageDetail")
    public ResultData getMessageDetail(@RequestParam String conversationId){
        try{
            List<Message> messages=messageService.getConversationDetail(conversationId);
            if(messages.isEmpty()){
                return new ResultData(MessageEnum.NO_MESSAGE.getMsg());}
            return new ResultData(true,messages);
        }catch (Exception e){
            return new ResultData(false,e.getMessage());
        }
    }

    //获取所有通信信息
    @GetMapping("/messageList")
    public ResultData getConversationDetail(HttpServletRequest request){
        try{
            String username = getUsrName.AllProjects(request);
            MessageList list=messageService.getConversationList(username);
            if(list==null){
                return new ResultData(MessageEnum.NO_MESSAGE.getMsg());}
            return new ResultData(true,list);
        }catch (Exception e){
            return new ResultData(false,e.getMessage());
        }
    }
}
