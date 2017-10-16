package cn.edu.swpu.cins.weike.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RedisToList {
    private JedisAdapter jedisAdapter;
    public List<String> redisToList(String key) {
        return jedisAdapter.smenber(key).stream().collect(Collectors.toList());
    }
}
