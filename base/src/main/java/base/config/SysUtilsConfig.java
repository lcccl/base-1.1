package base.config;

import base.utils.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cl on 2017/6/22.
 * 系统工具类初始化
 */
@Configuration
public class SysUtilsConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /*========================= 初始化系统工具类 =========================*/
        // 初始化SpringUtils工具类
        SpringUtils.init(applicationContext);
    }

}
