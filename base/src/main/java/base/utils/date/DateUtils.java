package base.utils.date;

import base.utils.date.parsers.LongDateParser;
import base.utils.date.parsers.StringDateParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cl on 2017/7/5.
 * 日期工具类
 */
public class DateUtils {

    /* 日期解析器 */
    private static List<DateParser> dateParsers = new ArrayList<DateParser>();

    private DateUtils() {
    }

    /**
     * 解析日期
     */
    public static Date parseDate(Object dateObj) {
        if (dateObj instanceof Date) {
            return (Date) dateObj;
        } else {
            for (DateParser parser : dateParsers) {
                if (parser.match(dateObj)) {
                    return parser.parse(dateObj);
                }
            }
        }
        return null;
    }

    /**
     * 日期格式化
     */
    public static String format(Object dateObj, String pattern) {
        Date date = parseDate(dateObj);
        return StringDateParser.getInstance().format(date, pattern);
    }

    /**
     * 添加自定义的日期解析器
     */
    public static void addDateParser(DateParser parser) {
        dateParsers.add(0, parser);
    }

    static {
        /*============ 添加默认的日期解析器 ============*/
        dateParsers.add(new LongDateParser());
        dateParsers.add(StringDateParser.getInstance());
    }

}
