package base.framework.web.mvc.mapping.creator;

import base.framework.web.mvc.mapping.MappingHandler;
import base.framework.web.mvc.mapping.MappingInfoCreator;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Created by cl on 2017/7/24.
 * spring mvc默认策略的请求映射信息创建器
 */
public class DefaultMappingInfoCreator implements MappingInfoCreator {

    @Override
    public RequestMappingInfo create(Method method, Class<?> clazz, MappingHandler handler) {
        RequestMappingInfo info = handler.createRequestMappingInfo(method);
        if (null != info) {
            RequestMappingInfo typeInfo = handler.createRequestMappingInfo(clazz);
            if (null != typeInfo) {
                info = typeInfo.combine(info);
            }
        }

        return info;
    }

}
