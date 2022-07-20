package base.framework.security.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by cl on 2017/10/26.
 * 默认的密码加密器，基于SimpleHash实现
 */
public class DefaultPasswordEncryptor implements PasswordEncryptor {

    /* 散列算法 */
    private String algorithmName;
    /* 散列次数 */
    private int hashIterations;
    /* 加密的编码 */
    private boolean toHex;

    public DefaultPasswordEncryptor(String algorithmName, int hashIterations, boolean toHex) {
        this.algorithmName = algorithmName;
        this.hashIterations = hashIterations;
        this.toHex = toHex;
    }

    @Override
    public String encrypt(String password, String salt) {
        SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);
        if (toHex) {
            return hash.toHex();
        } else {
            return hash.toBase64();
        }
    }

}
