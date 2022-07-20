package base.framework.rpc.motan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by cl on 2017/12/2.
 * BasicServiceConfigBean的配置
 */
@ConfigurationProperties(prefix = "motan.basicService")
public class BasicServiceProperties {

    /* 服务发布的端口号 */
    private Integer port;

    private String application;

    private String group;

    private String module;

    private String version;

    private Boolean accessLog;

    /* 默认共享通道，就不需要为每个@MotanService设置export */
    private Boolean shareChannel = true;

    /* 默认请求超时时间15s */
    private Integer requestTimeout = 15000;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public Boolean getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(Boolean accessLog) {
        this.accessLog = accessLog;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Boolean getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(Boolean shareChannel) {
        this.shareChannel = shareChannel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

}
