package test

import base.utils.serializer.Serializer
import base.utils.serializer.impl.JdkSerializer
import base.utils.serializer.impl.JsonSerializer
import org.junit.Test

/**
 * Created by cl on 2018/4/9.
 */
class TestSerializer {

    /**
     * 测试JSON序列化器
     */
    @Test
    void testJsonSerializer() {
        Serializer serializer = new JsonSerializer();
        doTest(serializer);
    }

    /**
     * 测试JDK序列化器
     */
    @Test
    void testJdkSerializer() {
        Serializer serializer = new JdkSerializer();
        doTest(serializer);
    }

    private void doTest(Serializer serializer) {
        Person p = new Person("jack", 10);
        byte[] bytes = serializer.serialize(p);
//        println new String(bytes, "UTF-8");
        println bytes;
        println serializer.deserialize(bytes, Person.class).dump();
    }

    private static class Person implements Serializable {
        String name;
        int age

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}
