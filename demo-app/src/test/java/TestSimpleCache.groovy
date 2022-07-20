import base.framework.cache.simple.SimpleLRUCache;

/**
 * Created by cl on 2017/8/14.
 */
class TestSimpleCache {

    public static void main(String[] args) {
        def cache = new SimpleLRUCache(3);
        cache.put("a", [name: "aaa"]);
        cache.put("b", [name: "bbb"]);
        cache.put("c", [name: "ccc"]);

        cache.get("a");
        cache.get("a");
        Thread.currentThread().sleep(10);
        cache.get("b");
        cache.get("b");
        cache.get("c");
        cache.get("c");
        cache.get("c");
        cache.get("a");

        cache.put("d", [name: "ddd"]);

        println cache.get("a");
        println cache.get("b");
        println cache.get("c");
    }

}
