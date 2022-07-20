package base.framework.web.mvc;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * Created by cl on 2017/4/14.
 * 包装FastJson自带的转换器，默认基本配置
 */
public class FastJsonConverter extends FastJsonHttpMessageConverter {

    public FastJsonConverter() {
        super();

        // 初始化fastjson
        this.setFeatures(SerializerFeature.PrettyFormat);
    }

}
