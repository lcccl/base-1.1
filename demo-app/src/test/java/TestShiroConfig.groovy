import base.framework.security.shiro.ShiroConfiguration
import base.utils.CommonUtils

/**
 * Created by cl on 2017/10/26.
 */
class TestShiroConfig {

    public static void main(String[] args) {
        def inputStream = CommonUtils.getClassPathResourceAsStream("shiro-config.xml");
        ShiroConfiguration.load(inputStream);
    }

}
