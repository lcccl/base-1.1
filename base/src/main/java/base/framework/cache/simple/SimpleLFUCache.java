package base.framework.cache.simple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cl on 2017/8/14.
 * 简单的LFU缓存实现
 * 缓存策略：到达上限时，删除使用频率最低、最早的缓存项
 */
public class SimpleLFUCache implements SimpleCache {

    /* 默认HashMap的负载因子 */
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;
    /* 默认缓存最大数量1000 */
    private final static int DEFAULT_MAX_SIZE = 1000;

    /* 最大缓存数量 */
    private int maxSize;

    /* 缓存项容器 */
    private Map<Object, CacheItem> data = new ConcurrentHashMap<Object, CacheItem>();

    public SimpleLFUCache() {
        this(DEFAULT_MAX_SIZE);
    }

    public SimpleLFUCache(int maxSize) {
        this.maxSize = maxSize;
        // 设置初始化容量，防止容器容量到达maxSize时自动扩容
        int capacity = (int) Math.ceil(maxSize / DEFAULT_LOAD_FACTOR) + 1;
        data = new ConcurrentHashMap<Object, CacheItem>(capacity, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public Object get(Object key) {
        CacheItem item = data.get(key);
        return null != item ? item.getVal() : null;
    }

    @Override
    public void put(Object key, Object val) {
        // 检查缓存是否到达上限
        if (data.size() >= maxSize) {
            this.releaseCapacity();
        }

        CacheItem item = new CacheItem(key, val);
        data.put(key, item);
    }

    @Override
    public Object remove(Object key) {
        CacheItem item = data.remove(key);
        return null != item ? item.getVal() : null;
    }

    @Override
    public void removeAll() {
        data.clear();
    }

    /**
     * 释放缓存容量，删除使用次数最少、最早的缓存项
     */
    private synchronized void releaseCapacity() {
        if (data.size() >= maxSize) {
            // 获取使用次数最少的缓存项
            List<CacheItem> minUsageItems = new ArrayList<CacheItem>();
            int minUsages = -1;
            for (CacheItem item : data.values()) {
                int usages = item.getUsages();
                if (minUsages == -1) {
                    minUsages = item.getUsages();
                    minUsageItems.add(item);
                } else if (usages == minUsages) {
                    minUsageItems.add(item);
                } else if (usages < minUsages) {
                    minUsages = usages;
                    minUsageItems.clear();
                    minUsageItems.add(item);
                }
            }

            // 存在多个使用次数最少的缓存项时，删除缓存使用时间最早的项
            Object deleteKey = null;
            long earliestUsedTime = -1;
            for (CacheItem item : minUsageItems) {
                long usedTime = item.getUsedTime();
                if (earliestUsedTime == -1) {
                    earliestUsedTime = usedTime;
                    deleteKey = item.getKey();
                } else if (usedTime < earliestUsedTime) {
                    earliestUsedTime = usedTime;
                    deleteKey = item.getKey();
                }
            }
            data.remove(deleteKey);
        }
    }

    /**
     * 缓存项
     */
    private class CacheItem {
        private Object key;
        private Object val;
        /* 使用次数 */
        private int usages;
        /* 使用时间 */
        private long usedTime;

        public CacheItem(Object key, Object val) {
            this.key = key;
            this.val = val;
            this.usages = 0;
            this.usedTime = new Date().getTime();
        }

        public Object getKey() {
            return key;
        }

        public Object getVal() {
            usages++;
            usedTime = new Date().getTime();
            return val;
        }

        public int getUsages() {
            return usages;
        }

        public long getUsedTime() {
            return usedTime;
        }
    }

}
