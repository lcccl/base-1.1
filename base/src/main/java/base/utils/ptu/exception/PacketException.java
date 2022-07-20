package base.utils.ptu.exception;

/**
 * Created by cl on 2017/4/18.
 * 报文异常
 */
public class PacketException extends RuntimeException {

    public PacketException() {
    }

    public PacketException(Exception e) {
        super(e.getLocalizedMessage(), e);
    }

    public PacketException(String messae) {
        super(messae);
    }

    public PacketException(String messae, Exception e) {
        super(messae, e);
    }


}
