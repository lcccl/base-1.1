package base.framework.cache.simple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cl on 2017/8/15.
 * 简单的LRU缓存实现
 */
public class SimpleLRUCache implements SimpleCache {

    /* 默认HashMap的负载因子 */
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;
    /* 默认缓存最大数量1000 */
    private final static int DEFAULT_MAX_SIZE = 1000;

    /* 最大缓存数量 */
    private int maxSize;

    /* 缓存项容器 */
    private Map<Object, CacheNode> data = new ConcurrentHashMap<Object, CacheNode>();

    /* 双向链表首节点：控制缓存的淘汰机制，最近最少使用的位于链表尾部，最活跃的位于链表头部 */
    private CacheNode headNode;

    public SimpleLRUCache() {
        this(DEFAULT_MAX_SIZE);
    }

    public SimpleLRUCache(int maxSize) {
        this.maxSize = maxSize;
        // 设置初始化容量，防止容器容量到达maxSize时自动扩容
        int capacity = (int) Math.ceil(maxSize / DEFAULT_LOAD_FACTOR) + 1;
        data = new ConcurrentHashMap<Object, CacheNode>(capacity, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public Object get(Object key) {
        CacheNode node = data.get(key);
        if (null == node) {
            return null;
        }

        // 缓存命中时，将缓存节点移动到链表头部
        this.moveNodeToHead(node);
        return node.val;
    }

    @Override
    public void put(Object key, Object val) {
        // 检查缓存数量是否达到最大数量
        if (data.size() >= maxSize) {
            synchronized (headNode) {
                if (data.size() >= maxSize) {
                    data.remove(headNode.pre.key);
                    this.removeNode(headNode.pre);
                }
            }
        }

        CacheNode node = data.get(key);
        if (null == node) {
            node = new CacheNode(key, val);
            data.put(key, node);
        } else {
            node.val = val;
        }
        // 放入缓存时，将节点移动到链表头部
        this.moveNodeToHead(node);
    }

    @Override
    public Object remove(Object key) {
        CacheNode node = data.get(key);
        if (null == node) {
            return null;
        }

        data.remove(key);
        this.removeNode(node);
        return node.val;
    }

    @Override
    public void removeAll() {
        data.clear();
        headNode = null;
    }

    /**
     * 将节点移动到链表头部
     */
    private synchronized void moveNodeToHead(CacheNode node) {
        if (null == headNode) {
            node.pre = node;
            node.next = node;
            headNode = node;
            return;
        }

        if (headNode == node) {
            return;
        }

        if (null != node.pre && null != node.next) {
            this.removeNode(node);
        }

        CacheNode lastNode = headNode.pre;
        node.pre = lastNode;
        node.next = headNode;
        headNode.pre = node;
        lastNode.next = node;
        headNode = node;
    }

    /**
     * 从链表中删除节点
     */
    private synchronized void removeNode(CacheNode node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.pre = null;
        node.next = null;
    }

    /**
     * 缓存链表节点
     */
    private class CacheNode {
        CacheNode pre;
        CacheNode next;
        Object key;
        Object val;

        public CacheNode(Object key, Object val) {
            this.key = key;
            this.val = val;
        }
    }

}
