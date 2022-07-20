package base.framework.web.mvc.mapping.creator;

import base.framework.web.mvc.mapping.MappingHandler;
import base.framework.web.mvc.mapping.MappingInfoCreator;
import base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by cl on 2017/7/24.
 * 根据名字规则处理的请求映射信息创建器
 */
@RequestMapping
public class ByNameMappingInfoCreator implements MappingInfoCreator {

    private final static String[] KEY_WORDS = new String[]{"invokeMethod", "getMetaClass", "setMetaClass", "getProperty", "setProperty"};

    private final static Class<? extends Annotation>[] ANNO_CLASSES = new Class[]{ExceptionHandler.class, Autowired.class, PostConstruct.class};

    private static RequestMapping REQUEST_MAPPING = ByNameMappingInfoCreator.class.getAnnotation(RequestMapping.class);

    @Override
    public RequestMappingInfo create(Method method, Class<?> clazz, MappingHandler handler) {
        // 根据RequestMapping注解创建方法的映射信息，不存在RequestMapping注解时，根据方法名创建映射信息
        RequestMappingInfo info = handler.createRequestMappingInfo(method);
        if (null == info && this.needMapping(clazz, method)) {
            info = this.createMethodRequestMappingInfo(method, handler);
        }

        // 合并Controller的映射信息
        if (null != info) {
            // 根据RequestMapping注解创建Controller的映射信息，不存在RequestMapping注解时，根据Controller类名创建映射信息
            RequestMappingInfo typeInfo = handler.createRequestMappingInfo(clazz);
            if (null == typeInfo) {
                typeInfo = this.createClassRequestMappingInfo(clazz, handler);
            }
            info = typeInfo.combine(info);
        }

        return info;
    }

    /**
     * 根据方法名创建请求映射信息
     */
    private RequestMappingInfo createMethodRequestMappingInfo(Method method, MappingHandler handler) {
        RequestCondition<?> methodCondition = handler.getMethodCondition(method);
        String[] paths = new String[]{method.getName()};

        return this.createRequestMappingInfo(paths, methodCondition);
    }

    /**
     * 根据Controller类名创建请求映射信息
     */
    private RequestMappingInfo createClassRequestMappingInfo(Class<?> clazz, MappingHandler handler) {
        RequestCondition<?> typeCondition = handler.getClazzCondition(clazz);
        // 映射路径为类名字符串中Controller前的部分，且首字母小写
        String className = clazz.getSimpleName();
        String controllerName = className.substring(0, className.length() - 10);
        controllerName = Character.toLowerCase(controllerName.charAt(0)) + controllerName.substring(1);
        String[] paths = new String[]{controllerName};

        return this.createRequestMappingInfo(paths, typeCondition);
    }

    /**
     * 根据路径创建请求映射信息
     */
    private RequestMappingInfo createRequestMappingInfo(String[] paths,
                                                        RequestCondition<?> customCondition) {
        return RequestMappingInfo.paths(paths)
                .params(REQUEST_MAPPING.params())
                .headers(REQUEST_MAPPING.headers())
                .consumes(REQUEST_MAPPING.consumes())
                .produces(REQUEST_MAPPING.produces())
                .customCondition(customCondition)
                .build();
    }

    /**
     * 判断方法是否需要进行映射
     */
    private boolean needMapping(Class<?> clazz, Method method) {
        // 不包含私有方法
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }

        // 方法不能包含特殊注解
        for (Class<? extends Annotation> annoClazz : ANNO_CLASSES) {
            Annotation annotation = method.getAnnotation(annoClazz);
            if (null != annotation) {
                return false;
            }
        }

        String methodName = method.getName();

        // 不包含属性的getters和setters方法
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            String fieldName = methodName.substring(3);
            fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
            for (Field field : ObjectUtils.getFields(clazz)) {
                if (field.getName().equals(fieldName)) {
                    return false;
                }
            }
        }

        // 方法名不能包含$
        if (methodName.contains("$")) {
            return false;
        }

        // 方法名不能为特殊方法名关键字
        for (String word : KEY_WORDS) {
            if (word.equals(methodName)) {
                return false;
            }
        }

        return true;
    }

}
