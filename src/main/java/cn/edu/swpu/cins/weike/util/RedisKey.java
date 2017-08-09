package cn.edu.swpu.cins.weike.util;

public class RedisKey {

    private static String BIZ_EVENTQUEUE ="EVENT_QUEUE";
    private static String BIZ_REGISTER = "REGISTER";
    private static String SPLIT = ":";
    private static String BIZ_FIND_PASSWORD = "FIND_PWD";

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    public static String getBizRegisterKey(String username){
        return BIZ_REGISTER+SPLIT+username;
    }

    public static String getBizFindPassword(String username){
        return BIZ_FIND_PASSWORD+SPLIT+username;
    }
}
