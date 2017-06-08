package weike.exception;

/**
 * Created by muyi on 17-6-8.
 */
public class WeiKeException extends Exception{

    public WeiKeException() {
    }

    public WeiKeException(String message) {
        super(message);
    }

    public WeiKeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeiKeException(Throwable cause) {
        super(cause);
    }

    public WeiKeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
