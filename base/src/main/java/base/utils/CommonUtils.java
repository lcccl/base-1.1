package base.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by cl on 2017/7/5.
 * 通用工具类
 */
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * 获取classpath下资源
     */
    public static URL getClassPathResource(String path) {
        path = path.indexOf("/") == 0 ? path.replaceFirst("/", "") : path;
        return CommonUtils.class.getClassLoader().getResource(path);
    }

    /**
     * 以流的形式获取classpath下资源
     */
    public static InputStream getClassPathResourceAsStream(String path) {
        path = path.indexOf("/") == 0 ? path.replaceFirst("/", "") : path;
        return CommonUtils.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * 获取classpath下的文本文件
     */
    public static String getClassPathText(String path, String encoding) {
        try {
            InputStream in = getClassPathResourceAsStream(path);
            return loadText(in, encoding);
        } catch (Exception e) {
            throw new RuntimeException("classpath:" + path + "文本获取失败\n" + e.getLocalizedMessage(), e);
        }
    }

    /**
     * 获取classpath下的properties文件
     */
    public static Properties getClassPathProperties(String path) {
        try {
            InputStream in = getClassPathResourceAsStream(path);
            return loadProperties(in);
        } catch (Exception e) {
            throw new RuntimeException("classpath:" + path + "文件获取失败\n" + e.getLocalizedMessage(), e);
        }
    }

    /**
     * 获取classpath下资源的绝对路径
     */
    public static String getClassPathResourcePath(String path) {
        URL url = getClassPathResource(path);
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            try {
                String srcPath = url.toURI().getPath();
                // 规范化路径格式，防止window下路径以"/"开头
                File file = new File(srcPath);
                return file.getAbsolutePath();
            } catch (Exception e) {
                throw new RuntimeException("classpath:" + path + "绝对路径获取失败\n" + e.getLocalizedMessage(), e);
            }
        } else {
            throw new RuntimeException("classpath:" + path + "路径协议为" + protocol + "，不是标准的文件路径");
        }
    }

    /**
     * 加载文本文件
     */
    public static String loadText(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            return new String(bos.toByteArray(), encoding);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(bos);
        }
    }

    /**
     * 加载properties文件
     */
    public static Properties loadProperties(InputStream in) throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(in);

            return prop;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 读取文件到字节数组
     */
    public static byte[] readFile(File file) {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 判断字符串数组中是否包含指定字符串
     */
    public static boolean contains(String[] arr, String key, boolean ignoreCase) {
        for (String str : arr) {
            boolean equalsFlag = ignoreCase ? key.equalsIgnoreCase(str) : key.equals(str);
            if (equalsFlag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数组中是否包含指定元素
     */
    public static boolean contains(Object[] arr, Object key) {
        for (Object obj : arr) {
            if (key.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
