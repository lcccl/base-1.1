package base.framework.security.shiro;

/**
 * Created by cl on 2017/10/26.
 * 密码加密器
 */
public interface PasswordEncryptor {

    /**
     * 加密密码
     */
    String encrypt(String password, String salt);

}
