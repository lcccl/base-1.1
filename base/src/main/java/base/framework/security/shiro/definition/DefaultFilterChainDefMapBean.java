package base.framework.security.shiro.definition;

import base.framework.security.shiro.FilterChainDefinitionMapBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cl on 2017/12/10.
 * 默认的FilterChainDefinitionMapBean实现
 */
public class DefaultFilterChainDefMapBean implements FilterChainDefinitionMapBean {

    @Override
    public Map<String, String> createDefinitionMap(Map<String, String> attrs, String text) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        String[] strArr = text.split("\n");
        for (String str : strArr) {
            String[] arr = str.split("=");
            if (arr.length == 2) {
                map.put(arr[0].trim(), arr[1].trim());
            }
        }

        return map;
    }

}
