package demo.config;

import base.config.*;
import base.framework.web.interceptor.InterceptorRegistrationBean;
import base.utils.forward.HttpForwardManager;
import demo.web.inteceptor.CommonInterceptor;
import demo.web.inteceptor.TestInterceptorA;
import demo.web.inteceptor.TestInterceptorB;
import demo.web.inteceptor.TestMethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Created by cl on 2017/4/6.
 * 系统配置类，通过注解导入各个功能组件的配置
 */
@Configuration
@Import({
        JpaDaoConfig.class,
        RedisCacheConfig.class,
        EhCacheConfig.class,
        CacheServiceConfig.class,
        PacketTransferServiceConfig.class,
        ExtMvcAutoConfig.class,
        SysUtilsConfig.class,
        CorsFilterConfig.class,
        MvcAutoConfig.class,
        MethodInterceptorConfig.class
})
public class SystemConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化HttpForwardManager
        HttpForwardManager.init();
    }

    /**
     * 公共拦截器
     */
    @Bean
    public InterceptorRegistrationBean interceptorRegistration() {
        CommonInterceptor interceptor = new CommonInterceptor();
        InterceptorRegistrationBean registrationBean = new InterceptorRegistrationBean();
        registrationBean.setInterceptor(interceptor);
        registrationBean.setPathPatterns("/**");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    /**
     * 测试拦截器A
     */
    @Bean
    public InterceptorRegistrationBean testInterceptorARegistration() {
        TestInterceptorA interceptor = new TestInterceptorA();
        InterceptorRegistrationBean registrationBean = new InterceptorRegistrationBean();
        registrationBean.setInterceptor(interceptor);
        registrationBean.setPathPatterns("/**");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * 测试拦截器B
     */
    @Bean
    public InterceptorRegistrationBean testInterceptorBRegistration() {
        TestInterceptorB interceptor = new TestInterceptorB();
        InterceptorRegistrationBean registrationBean = new InterceptorRegistrationBean();
        registrationBean.setInterceptor(interceptor);
        registrationBean.setPathPatterns("/**");
        registrationBean.setOrder(3);
        return registrationBean;
    }

    /**
     * 测试方法拦截器
     */
    @Bean
    public InterceptorRegistrationBean methodInterceptorRegistration() {
        TestMethodInterceptor interceptor = new TestMethodInterceptor();
        InterceptorRegistrationBean registrationBean = new InterceptorRegistrationBean();
        registrationBean.setInterceptor(interceptor);
        registrationBean.setPathPatterns("/*/test*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
