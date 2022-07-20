package base.utils.date;

import java.util.Date;

/**
 * Created by cl on 2017/7/5.
 * 日期解析器
 */
public interface DateParser {

    /**
     * 判断是否匹配，可以进行解析
     */
    boolean match(Object dateObj);

    /**
     * 解析成日期
     */
    Date parse(Object dateObj);

}
