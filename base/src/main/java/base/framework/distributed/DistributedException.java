package base.framework.distributed;

/**
 * Created by cl on 2018/4/9.
 * 分布式异常
 */
public class DistributedException extends RuntimeException {

    public DistributedException() {
    }

    public DistributedException(String msg) {
        super(msg);
    }

    public DistributedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
