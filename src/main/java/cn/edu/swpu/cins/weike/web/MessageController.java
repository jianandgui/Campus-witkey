package cn.edu.swpu.cins.weike.web;

import cn.edu.swpu.cins.weike.config.filter.JwtTokenUtil;
import cn.edu.swpu.cins.weike.entity.persistence.Message;
import cn.edu.swpu.cins.weike.entity.view.*;
import cn.edu.swpu.cins.weike.enums.MessageEnum;
import cn.edu.swpu.cins.weike.service.JoinProjectService;
import cn.edu.swpu.cins.weike.service.MailService;
import cn.edu.swpu.cins.weike.service.MessageService;
import cn.edu.swpu.cins.weike.util.GetUsrName;
import cn.edu.swpu.cins.weike.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
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
    JoinProjectService joinProjectService;


    @Autowired
    public MessageController(MessageService messageService, JwtTokenUtil jwtTokenUtil, GetUsrName getUsrName, MailService service) {
        this.messageService = messageService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.getUsrName = getUsrName;
        this.service = service;
    }

    /**
     * 用户申请项目 发送消息和站内信
     *
     * @param messageView
     * @param request
     * @return
     */
    @PostMapping("/sendMessage")
    public ResultData sendMessage(@RequestBody MessageView messageView, HttpServletRequest request) {
        try {
            String userSender = getUsrName.AllProjects(request);
            messageService.addMessage(messageView.getContent(), messageView.getProjectName(), userSender);
            return new ResultData(true, MessageEnum.SEND_MESSAGE_SUCCESS.getMsg());
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 增加这个接口用来接受项目参加的请求，此时我们需要给他发送一条站内信和邮件，通知他被同意参加项目
     * 并且在他申请项目的列表中删除这个项目，并在他申请成功项目列表中加入这个项目
     * 对于发布项目的人我们需要在他项目的申请人列表中删除这个人，并且在项目团队名单中加入这个人。
     *
     * @param joinMessage
     * @return
     */
    @PostMapping("/acceptApply")
    public ResultData acceptJoin(@RequestBody JoinMessage joinMessage, HttpServletRequest request) {
        try {
            joinProjectService.acceptJoin(joinMessage, request);
            return new ResultData(true);
        } catch (Exception e) {
            return new ResultData(false, "服务器内部异常");
        }
    }


    @PostMapping("/refuseApply")
    public ResultData refuseJoin(@RequestBody JoinMessage joinMessage, HttpServletRequest request) {
        try {
            joinProjectService.refuseJoin(joinMessage, request);
            return new ResultData(true, "拒绝申请成功");
        } catch (Exception e) {
            return new ResultData(false, "服务器内部异常");
        }
    }

    /*//获取与某人的通信信息
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
    }*/

    @GetMapping("/messageList")
    public ResultData getConversationDetail(HttpServletRequest request) {
        try {
            String username = getUsrName.AllProjects(request);
            MessageList list = messageService.getConversationList(username);
            if (list == null) {
                return new ResultData(MessageEnum.NO_MESSAGE.getMsg());
            }
            return new ResultData(true, list);
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 根据id删除一条信息
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteMessage")
    public ResultData deleteMessage(@RequestBody MessageDelete id) {
        try {
            messageService.deleteMessage(id.getId());
            return new ResultData(true, "信息删除成功");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 用户关注一个项目
     *
     * @param followPro
     * @param request
     * @return
     */
    @PostMapping("/followPro")
    public ResultData attentionPro(@RequestBody FollowPro followPro, HttpServletRequest request) {
        try {
            messageService.followPro(followPro.getProjectName(), getUsrName.AllProjects(request), followPro.getProjectConnector());
            return new ResultData(true, "关注项目成功");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    @PostMapping("/unFollowPro")
    public ResultData unAttentionPro(@RequestBody FollowPro followPro, HttpServletRequest request) {
        try {
            messageService.unFollowPro(followPro.getProjectName(), getUsrName.AllProjects(request), followPro.getProjectConnector());
            return new ResultData(true, "取消关注项目成功");
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }

    /**
     * 查看项目关注列表
     *
     * @param projectName
     * @return
     */
    @GetMapping("/proFollower")
    public ResultData queryProFollower(@RequestParam String projectName) {
        try {
            return new ResultData(true, messageService.queryFollower(projectName));
        } catch (Exception e) {
            return new ResultData(false, e.getMessage());
        }
    }
}
