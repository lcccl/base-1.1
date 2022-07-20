package base.framework.dao.sql.template;

import java.util.HashMap;

/**
 * Created by cl on 2017/6/9.
 * SQL参数Map
 */
public class ParamMap extends HashMap<String, Object> {

    /* 占位符前缀 */
    private String prefix;

    /* 占位符索引 */
    private int idx = 0;

    public ParamMap() {
    }

    public ParamMap(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 根据参数值创建对应的预编译SQL占位符
     */
    public String buildParam(Object paramValue) {
        String paramName = prefix + idx++;
        // 保存参数
        this.put(paramName, paramValue);

        return ":" + paramName;
    }

}
