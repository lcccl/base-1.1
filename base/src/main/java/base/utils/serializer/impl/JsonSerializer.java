package base.utils.serializer.impl;

import base.utils.serializer.Serializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by cl on 2018/4/9.
 * JSON序列化器
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            String jsonStr = new String(bytes, "UTF-8").trim();
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                return JSON.parseArray(jsonStr, clazz);
            } else {
                return JSON.parseObject(jsonStr, clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to deserialize, reason:\n" + e.getLocalizedMessage(), e);
        }
    }

}
