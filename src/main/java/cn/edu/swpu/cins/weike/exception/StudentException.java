package cn.edu.swpu.cins.weike.exception;

/**
 * Created by muyi on 17-6-8.
 */
public class StudentException extends WeiKeException {

    public StudentException() {
    }

    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentException(Throwable cause) {
        super(cause);
    }

    public StudentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
