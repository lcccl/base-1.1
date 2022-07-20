package base.framework.web.mvc.mapping;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Created by cl on 2017/7/24.
 * 映射处理器
 */
public interface MappingHandler {

    RequestMappingInfo createRequestMappingInfo(AnnotatedElement element);

    RequestCondition<?> getClazzCondition(Class<?> handlerType);

    RequestCondition<?> getMethodCondition(Method method);

}
