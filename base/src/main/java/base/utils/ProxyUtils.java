package base.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2018/5/10.
 * 代理工具类
 */
public class ProxyUtils {

    public static Object createProxy(Class<?>[] interfaces, Object[] targets) {
        return createProxy(interfaces, targets, null);
    }

    public static Object createProxy(Class<?>[] interfaces, ProxyHandler handler) {
        return createProxy(interfaces, null, handler);
    }

    public static Object createProxy(Class<?>[] interfaces, Object[] targets, ProxyHandler handler) {
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler(targets, handler);
        return Proxy.newProxyInstance(ProxyUtils.class.getClassLoader(), interfaces, proxyInvocationHandler);
    }

    public static List<Method> getMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<Method>();
        while (null != clazz && Object.class != clazz) {
            for (Method method : clazz.getDeclaredMethods()) {
                int mod = method.getModifiers();
                if (!Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
                    list.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return list;
    }

    public static String getMethodSign(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getName()).append(" ");
        sb.append(method.getName()).append(" ");
        sb.append("(");
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; null != types && i < types.length; i++) {
            sb.append(types[i].getName());
            if (i < types.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    private static class ProxyInvocationHandler implements InvocationHandler {

        /* 被代理对象数组 */
        private Object[] targets;

        /* 代理处理器 */
        private ProxyHandler handler;

        /* 方法对应的被代理对象 */
        private Map<String, Object> methodTargetMap = new HashMap<String, Object>();

        public ProxyInvocationHandler(Object[] targets, ProxyHandler handler) {
            this.targets = targets;
            this.handler = handler;

            for (Object target : targets) {
                List<Method> methods = getMethods(target.getClass());
                for (Method method : methods) {
                    methodTargetMap.put(getMethodSign(method), target);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object target = methodTargetMap.get(getMethodSign(method));
            if (null != handler) {
                return handler.invoke(proxy, target, method, args);
            } else {
                return method.invoke(target, args);
            }
        }

    }

    /**
     * 代理处理器
     */
    public interface ProxyHandler {
        Object invoke(Object proxy, Object target, Method method, Object[] args) throws Throwable;
    }

}
