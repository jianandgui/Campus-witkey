package cn.edu.swpu.cins.weike.util;

public class RedisKey {

    private static String BIZ_EVENTQUEUE ="EVENT_QUEUE";

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }
}
