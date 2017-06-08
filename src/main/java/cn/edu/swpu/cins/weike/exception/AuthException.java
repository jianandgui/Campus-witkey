package cn.edu.swpu.cins.weike.exception;

/**
 * Created by muyi on 17-6-8.
 */
public class AuthException extends WeiKeException{

    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
