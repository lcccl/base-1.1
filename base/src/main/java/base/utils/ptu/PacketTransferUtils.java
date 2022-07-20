package base.utils.ptu;

import base.utils.ptu.model.PacketConfig;
import base.utils.ptu.model.PacketResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by cl on 2017/4/14.
 * 报文传输工具
 */
public class PacketTransferUtils {

    private final static Log logger = LogFactory.getLog(PacketTransferUtils.class);

    /* 报文处理器 */
    private Map<String, PacketHandler> handlers;

    /* 报文传输器 */
    private Map<String, PacketTransfer> transfers;


    public PacketTransferUtils() {
    }

    /**
     * 初始化
     */
    public void init() {
        // 加载默认配置（当前类同级的ptu.properties配置文件）
        Map<Object, Object> defaultConfig = this.loadConfig("ptu.properties");
        // 如果classpath根目录下存在ptu.properties配置文件，加载并覆盖默认配置
        Map<Object, Object> config = this.loadConfig("/ptu.properties");
        if (null != config) {
            defaultConfig.putAll(config);
        }

        this.initial(defaultConfig);
    }

    /**
     * 发送报文
     *
     * @param config 报文配置
     * @param params 参数
     * @return
     */
    public PacketResult sendPacket(PacketConfig config, Map<String, Object> params) {
        PacketHandler handler = handlers.get(config.getPacketHandler());
        PacketTransfer transfer = transfers.get(config.getPacketTransfer());
        PacketListener listener = config.getListener();

        // 生成报文
        String sendPacket = handler.toPacket(config, params);
        if (null != listener) {
            sendPacket = listener.beforeSend(sendPacket);
        }
        logger.info("[发送报文]：" + sendPacket);

        // 传输报文
        PacketResult rs = transfer.transport(config, sendPacket);
        String backPacket = rs.getBackPacket();
        if (null != listener) {
            backPacket = listener.afterSend(backPacket);
        }
        logger.info("[返回报文]：" + backPacket);

        // 处理返回结果
        Object data = handler.fromPacket(backPacket);
        rs.setData(data);
        rs.setSendPacket(sendPacket);

        return rs;
    }

    /**
     * 加载配置文件
     */
    private Map<Object, Object> loadConfig(String path) {
        InputStream in = this.getClass().getResourceAsStream(path);
        if (null != in) {
            try {
                Properties prop = new Properties();
                prop.load(in);
                return prop;
            } catch (IOException e) {
                logger.error(path + "配置文件加载失败\n" + e.getLocalizedMessage(), e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        return null;
    }

    /**
     * 根据配置初始化
     *
     * @param config 初始化配置
     */
    private void initial(Map<?, ?> config) {
        handlers = new HashMap<String, PacketHandler>();
        transfers = new HashMap<String, PacketTransfer>();

        try {
            for (Map.Entry<?, ?> entry : config.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                // 初始化报文处理器、传输器
                if (key.startsWith("ptu.handler.")) {
                    String name = key.substring(key.lastIndexOf(".") + 1);
                    PacketHandler handler = (PacketHandler) newInstance(value);
                    handler.init(config);
                    handlers.put(name, handler);
                } else if (key.startsWith("ptu.transfer.")) {
                    String name = key.substring(key.lastIndexOf(".") + 1);
                    PacketTransfer transfer = (PacketTransfer) newInstance(value);
                    transfer.init(config);
                    transfers.put(name, transfer);
                }
            }
        } catch (Exception e) {
            logger.error("PacketTransferUtils初始化失败\n" + e.getLocalizedMessage(), e);
        }
    }

    private Object newInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        return clazz.newInstance();
    }

}
