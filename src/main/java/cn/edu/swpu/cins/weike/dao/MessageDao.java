package cn.edu.swpu.cins.weike.dao;

import cn.edu.swpu.cins.weike.entity.persistence.Message;
import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by muyi on 17-6-12.
 */
@Mapper
@Repository
public interface MessageDao {

    int addMessage(Message message);

    List<Message> getConversationDetail(String conversationId);

    List<Message> getConversationList(String userName);

    int deleteMessage(int id);

}
