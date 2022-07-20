package motan.demo.client.config;

import base.config.ExtMvcAutoConfig;
import base.config.MotanConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by cl on 2017/12/1.
 */
@Configuration
@Import({
        ExtMvcAutoConfig.class,
        MotanConfig.class
})
public class SystemConfig {
}
