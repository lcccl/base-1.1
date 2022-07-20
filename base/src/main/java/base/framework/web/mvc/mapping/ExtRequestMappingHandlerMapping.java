package base.framework.web.mvc.mapping;

import base.framework.web.mvc.annotation.MappingStrategy;
import base.framework.web.mvc.mapping.creator.ByNameMappingInfoCreator;
import base.framework.web.mvc.mapping.creator.DefaultMappingInfoCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/7/24.
 * 扩展的RequestMappingHandlerMapping，根据映射策略创建映射信息
 */
public class ExtRequestMappingHandlerMapping extends RequestMappingHandlerMapping implements MappingHandler {

    private final static Log logger = LogFactory.getLog(ExtRequestMappingHandlerMapping.class);

    /* 请求映射信息创建器 */
    private Map<String, MappingInfoCreator> creatorMap = new HashMap<String, MappingInfoCreator>();

    /* 全局策略 */
    private String globalStrategy;

    public ExtRequestMappingHandlerMapping(MappingProperties properties) {
        /*============== 注册自带的请求映射信息创建器 ==============*/
        creatorMap.put("default", new DefaultMappingInfoCreator());
        creatorMap.put("byName", new ByNameMappingInfoCreator());

        if (null != properties) {
            globalStrategy = properties.getStrategy();

            // 注册自定义的请求映射信息创建器
            if (null != properties.getCreators()) {
                for (Map.Entry<String, String> entry : properties.getCreators().entrySet()) {
                    String key = entry.getKey();
                    String className = entry.getValue();
                    try {
                        Class<?> clazz = Class.forName(className);
                        creatorMap.put(key, (MappingInfoCreator) clazz.newInstance());
                    } catch (Exception e) {
                        logger.error("注册策略[" + key + "]对应的creator[" + className + " ]失败\n"
                                + e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = null;
        String strategy = null;

        // 通过注解获取映射策略
        MappingStrategy annotation = handlerType.getAnnotation(MappingStrategy.class);
        if (null != annotation) {
            strategy = annotation.value();
        }
        // 全局策略
        if (null == strategy) {
            strategy = globalStrategy;
        }
        // 默认策略
        if (null == strategy) {
            strategy = "default";
        }

        MappingInfoCreator creator = creatorMap.get(strategy);
        if (null != creator) {
            mappingInfo = creator.create(method, handlerType, this);
        } else {
            logger.error("找不到策略[" + strategy + "]对应的creator");
        }

        return mappingInfo;
    }


    @Override
    public RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition condition = element instanceof Class ? this.getCustomTypeCondition((Class) element)
                : this.getCustomMethodCondition((Method) element);
        return requestMapping != null ? this.createRequestMappingInfo(requestMapping, condition) : null;
    }

    @Override
    public RequestCondition<?> getClazzCondition(Class<?> handlerType) {
        return super.getCustomTypeCondition(handlerType);
    }

    @Override
    public RequestCondition<?> getMethodCondition(Method method) {
        return getCustomMethodCondition(method);
    }

}
