package base.utils.xml;

import base.utils.xml.handler.KeyValHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对象Map
 */
public class ObjectMap extends LinkedHashMap<String, Object> implements Map<String, Object> {

    private static final long serialVersionUID = 1850678000580448667L;

    private KeyValHandler handler;

    public ObjectMap() {
        super();
    }

    public ObjectMap(Map<String, Object> map) {
        super(map);
    }

    public ObjectMap(KeyValHandler handler) {
        super();
        this.handler = handler;
    }

    public ObjectMap(Map<String, Object> map, KeyValHandler handler) {
        super(map);
        this.handler = handler;
    }

    /**
     * 替代Map的put方法，存储键值对时可以对键值对进行处理
     */
    public Object put2(String key, Object value) {
        if (null != handler) {
            Object[] rs = handler.handle(key, value);
            key = (String) rs[0];
            value = rs[1];
        }

        return this.put(key, value);
    }

}
