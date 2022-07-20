package base.utils.serializer;

/**
 * Created by cl on 2018/4/9.
 * 序列化器
 */
public interface Serializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

}
