package base.framework.rpc.motan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by cl on 2017/12/2.
 * RegistryConfigBean的配置
 */
@ConfigurationProperties(prefix = "motan.registry")
public class RegistryProperties {

    private String name;
    /* 替代regProtocol */
    private String protocol;
    private String address;
    private Integer port;
    private Integer requestTimeout;
    private Integer connectTimeout;
    private Integer registrySessionTimeout;
    private Integer registryRetryPeriod;
    private String check;
    private Boolean register;
    private Boolean subscribe;
    private Boolean isDefault;
    private String excise;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getRegistrySessionTimeout() {
        return registrySessionTimeout;
    }

    public void setRegistrySessionTimeout(Integer registrySessionTimeout) {
        this.registrySessionTimeout = registrySessionTimeout;
    }

    public Integer getRegistryRetryPeriod() {
        return registryRetryPeriod;
    }

    public void setRegistryRetryPeriod(Integer registryRetryPeriod) {
        this.registryRetryPeriod = registryRetryPeriod;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getExcise() {
        return excise;
    }

    public void setExcise(String excise) {
        this.excise = excise;
    }

}
