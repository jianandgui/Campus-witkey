package cn.edu.swpu.cins.weike.util;

public class RedisKey {

    private static String BIZ_EVENTQUEUE ="EVENT_QUEUE";
    private static String BIZ_REGISTER = "REGISTER";
    private static String SPLIT = ":";
    private static String BIZ_FIND_PASSWORD = "FIND_PWD";

    //正在申请的项目
    private static String BIZ_APPLYING_PRO = "JOINING_PRO";

    //申请失败的项目
    private static String BIZ_JOIN_FAIL = "JOIN_FAIL";

    //申请成功的项目
    private static String BIZ_JOIN_SUCCESS = "JOIN_SUCCESS";

    //项目申请人
    private static String BIZ_PRO_APPLICANT = "PRO_APPLICANT";

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    public static String getBizRegisterKey(String username){
        return BIZ_REGISTER+SPLIT+username;
    }

    public static String getBizFindPassword(String username){
        return BIZ_FIND_PASSWORD+SPLIT+username;
    }

    public static String getBizApplyingPro(String username){
        return BIZ_APPLYING_PRO+SPLIT+username;
    }

    public static String getBizProApplicant(String projectName){
        return BIZ_PRO_APPLICANT+SPLIT+projectName;
    }

    public static String getBizJoinFail(String username){
        return BIZ_JOIN_FAIL+SPLIT+username;
    }

    public static String getBizJoinSuccess(String username){
        return BIZ_JOIN_SUCCESS+SPLIT+username;
    }
}
