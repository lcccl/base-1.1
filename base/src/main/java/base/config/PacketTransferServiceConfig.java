package base.config;

import base.framework.service.util.PacketTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by cl on 2017/4/19.
 * 报文传输服务配置
 */
@Configuration
public class PacketTransferServiceConfig implements ServletContextAware {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ServletContext servletContext;

    @Bean
    public PacketTransferService packetTransferService() {
        PacketTransferService service = new PacketTransferService();
        service.setJdbcTemplate(jdbcTemplate);
        service.setServletContext(servletContext);

        return service;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
