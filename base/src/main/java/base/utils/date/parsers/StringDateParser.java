package base.utils.date.parsers;

import base.utils.date.DateParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/7/5.
 * 字符串日期格式的日期解析器
 */
public class StringDateParser implements DateParser {

    /* 单例 */
    private static StringDateParser instance = new StringDateParser();

    /**
     * SimpleDateFormat非线程安全，为了安全和性能考虑使用ThreadLocal
     */
    private Map<String, ThreadLocal<SimpleDateFormat>> formats = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    private StringDateParser() {
        // 初始化常用的日期格式format
        formats.put("yyyy-MM-dd HH:mm:ss", new ThreadLocal<SimpleDateFormat>());
        formats.put("yyyyMMdd HH:mm:ss", new ThreadLocal<SimpleDateFormat>());
        formats.put("yyyyMMddHH:mm:ss", new ThreadLocal<SimpleDateFormat>());
        formats.put("yyyyMMddHHmmss", new ThreadLocal<SimpleDateFormat>());
        formats.put("yyyy-MM-dd", new ThreadLocal<SimpleDateFormat>());
        formats.put("yyyyMMdd", new ThreadLocal<SimpleDateFormat>());
    }

    @Override
    public boolean match(Object dateObj) {
        return dateObj instanceof String;
    }

    @Override
    public Date parse(Object dateObj) {
        Date date = null;
        for (String pattern : formats.keySet()) {
            SimpleDateFormat df = getCurrentThreadFormat(pattern);
            try {
                date = df.parse((String) dateObj);
            } catch (Exception e) {
            }
        }
        return date;
    }

    /**
     * 日期格式化
     */
    public String format(Date date, String pattern) {
        SimpleDateFormat df = getCurrentThreadFormat(pattern);
        return df.format(date);
    }

    /**
     * 获取当前线程的DateFormat
     */
    private SimpleDateFormat getCurrentThreadFormat(String pattern) {
        SimpleDateFormat df = null;
        ThreadLocal<SimpleDateFormat> local = formats.get(pattern);
        if (null == local) {
            synchronized (formats) {
                local = formats.get(pattern);
                if (null == local) {
                    local = new ThreadLocal<SimpleDateFormat>();
                    formats.put(pattern, local);
                }
            }
        }

        df = local.get();
        if (null == df) {
            df = new SimpleDateFormat(pattern);
            local.set(df);
        }

        return df;
    }

    public static StringDateParser getInstance() {
        return instance;
    }

}
