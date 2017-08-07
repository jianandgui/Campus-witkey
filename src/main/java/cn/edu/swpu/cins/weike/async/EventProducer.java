package cn.edu.swpu.cins.weike.async;


import cn.edu.swpu.cins.weike.util.JedisAdapter;
import cn.edu.swpu.cins.weike.util.RedisKey;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKey.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
