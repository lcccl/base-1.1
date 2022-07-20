package base.utils.h2;

import java.util.UUID;

/**
 * Created by cl on 2017/12/1.
 * H2数据库扩展函数库
 */
public class H2FunctionLib {

    private H2FunctionLib() {
    }

    /**
     * 生成UUID，用法：select uuid();
     * h2中注册函数：create alias uuid for "base.utils.h2.H2FunctionLib.uuid";
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
