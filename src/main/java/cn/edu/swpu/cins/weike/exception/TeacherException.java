package cn.edu.swpu.cins.weike.exception;

/**
 * Created by muyi on 17-6-8.
 */
public class TeacherException extends WeiKeException{

    public TeacherException() {
    }

    public TeacherException(String message) {
        super(message);
    }

    public TeacherException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeacherException(Throwable cause) {
        super(cause);
    }

    public TeacherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
