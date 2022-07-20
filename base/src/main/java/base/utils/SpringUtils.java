package base.utils;

import org.springframework.context.ApplicationContext;

/**
 * Created by cl on 2017/6/22.
 * Spring工具类
 */
public class SpringUtils {

    private static ApplicationContext applicationContext;

    private SpringUtils() {
    }

    /**
     * 初始化
     */
    public static void init(ApplicationContext ac) {
        if (null == applicationContext) {
            applicationContext = ac;
        }
    }

    /**
     * 获取Spring上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取Bean
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取Bean
     */
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

}
