package base.framework.web.mvc.mapping;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * Created by cl on 2017/7/21.
 * 请求映射信息创建器
 */
public interface MappingInfoCreator {

    /**
     * 创建请求映射信息
     */
    RequestMappingInfo create(Method method, Class<?> clazz, MappingHandler handler);

}
