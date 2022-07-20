package base.utils.codec;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

/**
 * Created by cl on 2017/9/8.
 * md5工具类
 */
public class Md5Utils {

    private Md5Utils() {
    }

    public static String encode(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    public static String encode(InputStream in) {
        try {
            return DigestUtils.md5Hex(in);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String encode(File file) {
        try {
            return encode(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    public static String encode(String str) {
        return DigestUtils.md5Hex(str);
    }

}
