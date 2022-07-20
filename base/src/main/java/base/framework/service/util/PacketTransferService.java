package base.framework.service.util;

import base.framework.cache.CacheService;
import base.framework.cache.CacheServiceManager;
import base.utils.ptu.PacketListener;
import base.utils.ptu.PacketTransferUtils;
import base.utils.ptu.exception.PacketException;
import base.utils.ptu.model.PacketConfig;
import base.utils.ptu.model.PacketResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/4/19.
 * 报文传输服务
 */
public class PacketTransferService {

    /* 常量：缓存名 */
    private final static String CACHE_NAME = "ptuCache";

    private JdbcTemplate jdbcTemplate;

    private ServletContext servletContext;

    private PacketTransferUtils ptu;

    public PacketTransferService() {
        ptu = new PacketTransferUtils();
        ptu.init();
    }

    /**
     * 发送报文
     *
     * @param code   接口配置代码
     * @param params 参数
     * @return
     */
    public PacketResult send(String code, Map<String, Object> params) {
        return this.send(code, params, null);
    }

    /**
     * 发送报文
     *
     * @param code     接口配置代码
     * @param params   参数
     * @param listener 监听器
     * @return
     */
    public PacketResult send(String code, Map<String, Object> params, PacketListener listener) {
        Map<String, Object> target = null;
        String cacheKey = "cfg_" + code;

        // 获取缓存服务
        CacheService cacheService = this.getCacheService();

        // 从缓存中读取报文配置
        if (null != cacheService) {
            target = (Map<String, Object>) cacheService.get(CACHE_NAME, cacheKey);
        }

        // 缓存中不存在，从数据库中读取
        if (null == target) {
            String querySql = "select t.code, t.packet_handler, t.packet_transfer, " +
                    "t.send_encode, t.back_encode, t.url, t.ext_config, " +
                    "t.template from packet_interface_conf t where t.code = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(querySql, code);
            if (null == list || list.size() == 0) {
                throw new PacketException("找不到code:" + code + "对应的接口配置");
            }
            target = list.get(0);

            if (null != cacheService) {
                cacheService.put(CACHE_NAME, cacheKey, target);
            }
        }

        PacketConfig conf = new PacketConfig();
        conf.setPacketHandler((String) target.get("packet_handler"));
        conf.setPacketTransfer((String) target.get("packet_transfer"));
        conf.setSendEncode((String) target.get("send_encode"));
        conf.setBackEncode((String) target.get("back_encode"));
        conf.setUrl((String) target.get("url"));
        conf.setExtConfig((String) target.get("ext_config"));
        conf.setTemplate((String) target.get("template"));
        conf.setListener(listener);

        // 读取模板
        String template = conf.getTemplate();
        if (!StringUtils.isEmpty(template)) {
            template = this.loadTemplate(template);
            conf.setTemplate(template);
        }

        return ptu.sendPacket(conf, params);
    }

    /**
     * 读取模板
     *
     * @param tplPath 模板路径
     * @return
     */
    private String loadTemplate(String tplPath) {
        String template = null;
        String cacheKey = "tpl_" + tplPath;

        // 获取缓存服务
        CacheService cacheService = this.getCacheService();

        // 从缓存中读取模板
        if (null != cacheService) {
            template = (String) cacheService.get(CACHE_NAME, cacheKey);
        }

        if (null == template) {
            try {
                if (tplPath.startsWith("classpath:")) {
                    String path = tplPath.substring(10).trim();
                    InputStream in = this.getClass().getResourceAsStream(path);
                    template = IOUtils.toString(in);
                    IOUtils.closeQuietly(in);
                } else {
                    String realPath = servletContext.getRealPath(tplPath);
                    template = FileUtils.readFileToString(new File(realPath));
                }
            } catch (Exception e) {
                throw new PacketException("模板：" + tplPath + "读取失败", e);
            }

            if (null != cacheService) {
                cacheService.put(CACHE_NAME, cacheKey, template);
            }
        }

        return template;
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        // 获取缓存服务
        CacheService cacheService = this.getCacheService();
        if (null != cacheService) {
            cacheService.clear(CACHE_NAME);
        }
    }

    /**
     * 获取缓存服务
     */
    private CacheService getCacheService() {
        return CacheServiceManager.getInstance().getCacheService("ehcache");
    }

    /*========================= Getters and Setters =========================*/
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
