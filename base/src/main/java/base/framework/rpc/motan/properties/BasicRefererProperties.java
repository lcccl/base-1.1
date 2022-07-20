package base.framework.rpc.motan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by cl on 2017/12/2.
 * BasicRefererConfigBean的配置
 */
@ConfigurationProperties(prefix = "motan.basicReferer")
public class BasicRefererProperties {

    private String application;

    private String group;

    private String module;

    private String version;

    private Boolean accessLog;

    private Boolean shareChannel;

    private Integer retries;

    private Boolean throwException;

    private String check;

    private Boolean async;

    private String mock;

    private String proxy;

    /* 默认请求超时时间15s */
    private Integer requestTimeout = 15000;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(Boolean accessLog) {
        this.accessLog = accessLog;
    }

    public Boolean getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(Boolean shareChannel) {
        this.shareChannel = shareChannel;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Boolean getThrowException() {
        return throwException;
    }

    public void setThrowException(Boolean throwException) {
        this.throwException = throwException;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getMock() {
        return mock;
    }

    public void setMock(String mock) {
        this.mock = mock;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

}
