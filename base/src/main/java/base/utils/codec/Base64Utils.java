package base.utils.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by cl on 2017/7/11.
 * base64工具类
 */
public class Base64Utils {

    private Base64Utils() {
    }

    /**
     * 对字节数组进行base64编码
     */
    public static String encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 对base64编码的字符串进行解码
     */
    public static byte[] decode(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    /**
     * 对文件进行base64编码
     */
    public static String encode(File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(in, bos);
            byte[] bytes = bos.toByteArray();
            return encode(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bos);
        }
    }

    /**
     * 将base64编码的字符串解码成图片
     */
    public static byte[] decode2Image(String base64Str) {
        byte[] bytes = decode(base64Str);
        // 调整异常数据
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        return bytes;
    }

    /**
     * 对字符串进行base64编码（默认字符串编码UTF-8）
     */
    public static String encodeString(String str) {
        return encodeString(str, "UTF-8");
    }

    /**
     * 对base64字符串进行解码（默认字符串编码UTF-8）
     */
    public static String decodeString(String base64Str) {
        return decodeString(base64Str, "UTF-8");
    }

    /**
     * 对字符串进行base64编码
     */
    public static String encodeString(String str, String charset) {
        try {
            return encode(str.getBytes(charset));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 对base64字符串进行解码
     */
    public static String decodeString(String base64Str, String charset) {
        try {
            byte[] bytes = decode(base64Str);
            return new String(bytes, charset);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

}
