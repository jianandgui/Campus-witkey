package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.view.ResultData;
import cn.edu.swpu.cins.weike.enums.MessageEnum;
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
@RestController
@RequestMapping
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private GetUsrName getUsrName;




    @PostMapping("/sendMessage")
    public ResultData sendMessage(@RequestParam String projectName,@RequestParam String content,HttpServletRequest request){
        try{
            String userSender = getUsrName.AllProjects(request);
            int num=messageService.addMessage(content,projectName,userSender);
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
            List<Message> list=messageService.getConversationList(username);
            if(list.isEmpty()){
                return new ResultData(MessageEnum.NO_MESSAGE.getMsg());}
            return new ResultData(true,list);
        }catch (Exception e){
            return new ResultData(false,e.getMessage());
        }

    }
}
