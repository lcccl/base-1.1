package base.config;

import base.framework.web.mvc.mapping.ExtRequestMappingHandlerMapping;
import base.framework.web.mvc.FastJsonConverter;
import base.framework.web.mvc.mapping.MappingProperties;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.io.Resource;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.*;

/**
 * Created by cl on 2017/6/14.
 * Spring MVC自动配置扩展
 * 存在自定义的support时，默认的WebMvcAutoConfigurationAdapter会失效，所以要重写WebMvcAutoConfigurationAdapter
 * 由于继承WebMvcAutoConfigurationAdapter会导致@Import自定义的support失效，所以将WebMvcAutoConfigurationAdapter部分方法拷贝进来
 */
@Configuration
@Import({ExtMvcAutoConfig.ExtMvcSupportConfig.class})
@EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
public class ExtMvcAutoConfig extends WebMvcConfigurerAdapter {

    private static final Log logger = LogFactory.getLog(WebMvcConfigurerAdapter.class);

    /*============================== 来自WebMvcAutoConfigurationAdapter的方法 - begin ==============================*/
    @Autowired
    private ResourceProperties resourceProperties = new ResourceProperties();
    @Autowired
    private WebMvcProperties mvcProperties = new WebMvcProperties();
    @Autowired
    private ListableBeanFactory beanFactory;
    @Autowired
    private HttpMessageConverters messageConverters;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        Long timeout = this.mvcProperties.getAsync().getRequestTimeout();
        if (timeout != null) {
            configurer.setDefaultTimeout(timeout.longValue());
        }
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map mediaTypes = this.mvcProperties.getMediaTypes();
        Iterator var3 = mediaTypes.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry mediaType = (Map.Entry) var3.next();
            configurer.mediaType((String) mediaType.getKey(), (MediaType) mediaType.getValue());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public InternalResourceViewResolver defaultViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(this.mvcProperties.getView().getPrefix());
        resolver.setSuffix(this.mvcProperties.getView().getSuffix());
        return resolver;
    }

    @Bean
    @ConditionalOnMissingBean({RequestContextListener.class, RequestContextFilter.class})
    public RequestContextFilter requestContextFilter() {
        return new OrderedRequestContextFilter();
    }

    @Bean
    @ConditionalOnBean({View.class})
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(2147483637);
        return resolver;
    }

    @Bean
    @ConditionalOnBean({ViewResolver.class})
    @ConditionalOnMissingBean(
            name = {"viewResolver"},
            value = {ContentNegotiatingViewResolver.class}
    )
    public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager((ContentNegotiationManager) beanFactory.getBean(ContentNegotiationManager.class));
        resolver.setOrder(-2147483648);
        return resolver;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "spring.mvc",
            name = {"locale"}
    )
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(this.mvcProperties.getLocale());
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.mvc",
            name = {"date-format"}
    )
    public Formatter<Date> dateFormatter() {
        return new DateFormatter(this.mvcProperties.getDateFormat());
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        if (this.mvcProperties.getMessageCodesResolverFormat() != null) {
            DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
            resolver.setMessageCodeFormatter(this.mvcProperties.getMessageCodesResolverFormat());
            return resolver;
        } else {
            return null;
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Iterator var2 = this.getBeansOfType(Converter.class).iterator();

        while (var2.hasNext()) {
            Converter formatter = (Converter) var2.next();
            registry.addConverter(formatter);
        }

        var2 = this.getBeansOfType(GenericConverter.class).iterator();

        while (var2.hasNext()) {
            GenericConverter formatter1 = (GenericConverter) var2.next();
            registry.addConverter(formatter1);
        }

        var2 = this.getBeansOfType(Formatter.class).iterator();

        while (var2.hasNext()) {
            Formatter formatter2 = (Formatter) var2.next();
            registry.addFormatter(formatter2);
        }
    }

    private <T> Collection<T> getBeansOfType(Class<T> type) {
        return this.beanFactory.getBeansOfType(type).values();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        Resource page = this.resourceProperties.getWelcomePage();
        if (page != null) {
            logger.info("Adding welcome page: " + page);
            registry.addViewController("/").setViewName("forward:index.html");
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!this.resourceProperties.isAddMappings()) {
            logger.debug("Default resource handling disabled");
        } else {
            Integer cachePeriod = this.resourceProperties.getCachePeriod();
            if (!registry.hasMappingForPattern("/webjars/**")) {
                this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(cachePeriod));
            }

            String staticPathPattern = this.mvcProperties.getStaticPathPattern();
            if (!registry.hasMappingForPattern(staticPathPattern)) {
                this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(this.resourceProperties.getStaticLocations()).setCachePeriod(cachePeriod));
            }

        }
    }

    private void customizeResourceHandlerRegistration(ResourceHandlerRegistration registration) {
        ResourceProperties.Chain properties = this.resourceProperties.getChain();
        this.configureResourceChain(properties, registration.resourceChain(properties.isCache()));
    }

    private void configureResourceChain(ResourceProperties.Chain properties, ResourceChainRegistration chain) {
        ResourceProperties.Strategy strategy = properties.getStrategy();
        if (strategy.getFixed().isEnabled() || strategy.getContent().isEnabled()) {
            chain.addResolver(this.getVersionResourceResolver(strategy));
        }

        if (properties.isHtmlApplicationCache()) {
            chain.addTransformer(new AppCacheManifestTransformer());
        }
    }

    private ResourceResolver getVersionResourceResolver(ResourceProperties.Strategy properties) {
        VersionResourceResolver resolver = new VersionResourceResolver();
        if (properties.getFixed().isEnabled()) {
            String paths = properties.getFixed().getVersion();
            String[] paths1 = properties.getFixed().getPaths();
            resolver.addFixedVersionStrategy(paths, paths1);
        }

        if (properties.getContent().isEnabled()) {
            String[] paths2 = properties.getContent().getPaths();
            resolver.addContentVersionStrategy(paths2);
        }

        return resolver;
    }
    /*============================== 来自WebMvcAutoConfigurationAdapter的方法 - end ==============================*/


    /*======================================= 定义功能扩展 - begin =======================================*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 增加fastjson
        converters.add(new FastJsonConverter());

        // 增加默认的转换器
        converters.addAll(messageConverters.getConverters());
    }

    /**
     * 默认的StandardServletMultipartResolver在weblogic下报错"Required MultipartFile parameter 'file' is not present"
     * 改用CommonsMultipartResolver，依赖Apache的fileupload组件
     * 普通@Configuration的加载永远在Auto-configuration之前，所以会优先于MultipartAutoConfiguration的加载
     */
    @Bean(name = "multipartResolver")
    @ConditionalOnClass({FileUpload.class})
    @ConditionalOnMissingBean({MultipartResolver.class})
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        return resolver;
    }

    /**
     * 扩展的WebMvcConfigurationSupport，继承自EnableWebMvcConfiguration
     */
    @Configuration
    @EnableConfigurationProperties(MappingProperties.class)
    public static class ExtMvcSupportConfig extends WebMvcAutoConfiguration.EnableWebMvcConfiguration {

        @Autowired
        private MappingProperties mappingProperties;

        /**
         * 使用自定义的请求映射处理器，将[Controller名称]/[方法名]的请求路径映射到该Controller的方法
         */
        @Override
        protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
            ExtRequestMappingHandlerMapping mapping = new ExtRequestMappingHandlerMapping(mappingProperties);
            return mapping;
        }

    }
    /*======================================= 定义功能扩展 - end =======================================*/

}
