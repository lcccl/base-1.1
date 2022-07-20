package base.utils.date.parsers;

import base.utils.date.DateParser;

import java.util.Date;

/**
 * Created by cl on 2017/7/5.
 * 长整型的日期戳形式的日期解析器
 */
public class LongDateParser implements DateParser {

    @Override
    public boolean match(Object dateObj) {
        return dateObj instanceof Long;
    }

    @Override
    public Date parse(Object dateObj) {
        return new Date((Long) dateObj);
    }

}
