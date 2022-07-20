package base.utils.serializer.impl;

import base.utils.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by cl on 2018/4/9.
 * JDK对象流实现的序列化器
 */
public class JdkSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed to serialize, reason:\n" + e.getLocalizedMessage(), e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("failed to deserialize, reason:\n" + e.getLocalizedMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
