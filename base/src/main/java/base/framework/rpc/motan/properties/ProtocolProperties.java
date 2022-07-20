package base.framework.rpc.motan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by cl on 2017/12/2.
 * ProtocolConfigBean的配置
 */
@ConfigurationProperties(prefix = "motan.protocol")
public class ProtocolProperties {

    private String name;
    private String serialization;
    private String codec;
    private Integer iothreads;
    protected Integer requestTimeout;
    protected Integer minClientConnection;
    protected Integer maxClientConnection;
    protected Integer minWorkerThread;
    protected Integer maxWorkerThread;
    protected Integer maxContentLength;
    protected Integer maxServerConnection;
    protected Boolean poolLifo;
    protected Boolean lazyInit;
    protected String endpointFactory;
    protected String cluster;
    protected String loadbalance;
    protected String haStrategy;
    protected Integer workerQueueSize;
    protected Integer acceptConnections;
    protected String proxy;
    protected String filter;
    protected Integer retries;
    protected Boolean async;
    private Boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Integer getIothreads() {
        return iothreads;
    }

    public void setIothreads(Integer iothreads) {
        this.iothreads = iothreads;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getMinClientConnection() {
        return minClientConnection;
    }

    public void setMinClientConnection(Integer minClientConnection) {
        this.minClientConnection = minClientConnection;
    }

    public Integer getMaxClientConnection() {
        return maxClientConnection;
    }

    public void setMaxClientConnection(Integer maxClientConnection) {
        this.maxClientConnection = maxClientConnection;
    }

    public Integer getMinWorkerThread() {
        return minWorkerThread;
    }

    public void setMinWorkerThread(Integer minWorkerThread) {
        this.minWorkerThread = minWorkerThread;
    }

    public Integer getMaxWorkerThread() {
        return maxWorkerThread;
    }

    public void setMaxWorkerThread(Integer maxWorkerThread) {
        this.maxWorkerThread = maxWorkerThread;
    }

    public Integer getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public Integer getMaxServerConnection() {
        return maxServerConnection;
    }

    public void setMaxServerConnection(Integer maxServerConnection) {
        this.maxServerConnection = maxServerConnection;
    }

    public Boolean getPoolLifo() {
        return poolLifo;
    }

    public void setPoolLifo(Boolean poolLifo) {
        this.poolLifo = poolLifo;
    }

    public Boolean getLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getEndpointFactory() {
        return endpointFactory;
    }

    public void setEndpointFactory(String endpointFactory) {
        this.endpointFactory = endpointFactory;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(String haStrategy) {
        this.haStrategy = haStrategy;
    }

    public Integer getWorkerQueueSize() {
        return workerQueueSize;
    }

    public void setWorkerQueueSize(Integer workerQueueSize) {
        this.workerQueueSize = workerQueueSize;
    }

    public Integer getAcceptConnections() {
        return acceptConnections;
    }

    public void setAcceptConnections(Integer acceptConnections) {
        this.acceptConnections = acceptConnections;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
