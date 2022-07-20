package base.framework.web.mvc;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.ResourceResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cl on 2017/4/11.
 * Spring MVC配置适配器，通过重写init方法，调用addInterceptor、addMessageConverter等方法添加对应的配置
 */
public abstract class AbstractMvcConfigureAdapter extends WebMvcConfigurerAdapter {

    /**
     * 拦截器配置
     */
    private List<InterceptorConfig> interceptors = new ArrayList<InterceptorConfig>();

    /**
     * HttpMessageConverter转换器配置
     */
    private List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();

    /**
     * 资源处理器配置
     */
    private List<ResourceConfig> resourceHandlers = new ArrayList<ResourceConfig>();

    public AbstractMvcConfigureAdapter() {
        this.init();
    }

    /**
     * 初始化方法，供子类重写
     */
    public abstract void init();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 根据拦截器配置添加拦截器
        for (InterceptorConfig conf : interceptors) {
            InterceptorRegistration it = registry.addInterceptor(conf.interceptor);
            if (null != conf.patterns) {
                it.addPathPatterns(conf.patterns);
            }
            if (null != conf.excludePatterns) {
                it.excludePathPatterns(conf.excludePatterns);
            }
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 根据配置添加ResourceHandler
        for (ResourceConfig conf : resourceHandlers) {
            ResourceHandlerRegistration handler = registry.addResourceHandler(conf.handler);
            handler.addResourceLocations(conf.locations);
            handler.setCachePeriod(conf.cachePeriod);
            ResourceChainRegistration chain = handler.resourceChain(conf.cacheResource);
            chain.addResolver(conf.resolver);
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(this.converters);
    }

    /**
     * 增加拦截器
     *
     * @param interceptor
     * @param patterns
     */
    protected void addInterceptor(HandlerInterceptor interceptor, String patterns) {
        this.addInterceptor(interceptor, patterns, null);
    }

    /**
     * 增加拦截器
     *
     * @param interceptor
     * @param patterns
     * @param excludePatterns
     */
    protected void addInterceptor(HandlerInterceptor interceptor, String patterns, String excludePatterns) {
        InterceptorConfig conf = new InterceptorConfig();
        conf.interceptor = interceptor;
        conf.patterns = patterns;
        conf.excludePatterns = excludePatterns;

        interceptors.add(conf);
    }

    /**
     * 增加资源处理器
     *
     * @param handler
     * @param locations
     * @param cachePeriod
     * @param cacheResource
     * @param resolver
     */
    protected void addResourceHandler(String handler,
                                      String locations,
                                      Integer cachePeriod,
                                      boolean cacheResource,
                                      ResourceResolver resolver) {
        ResourceConfig conf = new ResourceConfig();
        conf.handler = handler;
        conf.locations = locations;
        conf.cachePeriod = cachePeriod;
        conf.cacheResource = cacheResource;
        conf.resolver = resolver;

        resourceHandlers.add(conf);
    }

    /**
     * 增加HttpMessageConverter转换器
     *
     * @param converter
     */
    protected void addMessageConverter(HttpMessageConverter<?> converter) {
        converters.add(converter);
    }

    private class InterceptorConfig {
        HandlerInterceptor interceptor;
        String patterns;
        String excludePatterns;
    }

    private class ResourceConfig {
        String handler;
        String locations;
        Integer cachePeriod;
        boolean cacheResource;
        ResourceResolver resolver;
    }
}
