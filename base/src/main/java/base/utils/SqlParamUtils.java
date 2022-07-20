package base.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/6/7.
 * sql参数工具类
 */
public class SqlParamUtils {

    private static Map<Class<?>, SqlParamBuilder> sqlParamBuildMap = new HashMap<Class<?>, SqlParamBuilder>();

    private SqlParamUtils() {
    }

    /**
     * 字符参数转义，防止sql注入
     */
    public static String escape(String str) {
        return str == null ? null : StringUtils.replace(str, "\'", "\'\'");
    }

    /**
     * 构建参数字符串，防止sql注入
     */
    public static String buildParam(Object val) {
        SqlParamBuilder builder = sqlParamBuildMap.get(val.getClass());
        return builder.build(val);
    }

    /**
     * sql参数构造器，构建参数字符串
     */
    private static interface SqlParamBuilder {
        String build(Object val);
    }

    static {
        // 字符串参数
        SqlParamBuilder stringBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return "'" + escape((String) val) + "'";
            }
        };
        sqlParamBuildMap.put(String.class, stringBuilder);

        // 字符参数
        SqlParamBuilder charBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return "'" + (Character) val + "'";
            }
        };
        sqlParamBuildMap.put(char.class, charBuilder);
        sqlParamBuildMap.put(Character.class, charBuilder);

        // 整形参数
        SqlParamBuilder intBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return new BigDecimal((Integer) val).toString();
            }
        };
        sqlParamBuildMap.put(int.class, intBuilder);
        sqlParamBuildMap.put(Integer.class, intBuilder);

        // 长整形参数
        SqlParamBuilder longBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return new BigDecimal((Long) val).toString();
            }
        };
        sqlParamBuildMap.put(long.class, longBuilder);
        sqlParamBuildMap.put(Long.class, longBuilder);

        // 浮点数参数
        SqlParamBuilder floatBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return new BigDecimal((Float) val).toString();
            }
        };
        sqlParamBuildMap.put(float.class, floatBuilder);
        sqlParamBuildMap.put(Float.class, floatBuilder);

        // 双精度浮点数参数
        SqlParamBuilder doubleBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                return new BigDecimal((Double) val).toString();
            }
        };
        sqlParamBuildMap.put(double.class, doubleBuilder);
        sqlParamBuildMap.put(Double.class, doubleBuilder);

        // 日期参数
        SqlParamBuilder dateBuilder = new SqlParamBuilder() {
            @Override
            public String build(Object val) {
                Date date = (Date) val;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                return "to_date('" + dateStr + "', 'yyyy-mm-dd hh24:mi:ss')";
            }
        };
        sqlParamBuildMap.put(Date.class, dateBuilder);
    }

}
