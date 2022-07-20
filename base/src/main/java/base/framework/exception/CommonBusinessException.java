package base.framework.exception;

/**
 * Created by cl on 2017/9/18.
 * 公共的业务异常
 */
public class CommonBusinessException extends RuntimeException {

    public CommonBusinessException() {
    }

    public CommonBusinessException(Exception e) {
        super(e);
    }

    public CommonBusinessException(String message) {
        super(message);
    }

    public CommonBusinessException(String message, Exception e) {
        super(message, e);
    }

}
