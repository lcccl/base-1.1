package test

import base.framework.distributed.DistributedFactory
import base.framework.distributed.Hash
import base.framework.distributed.Lock
import base.framework.distributed.Queue
import base.framework.distributed.impl.redis.RedisDistributedFactory
import org.junit.Before
import org.junit.Test
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

import java.util.concurrent.CountDownLatch

/**
 * Created by cl on 2018/4/9.
 */
class TestDistributed {

    private DistributedFactory factory;

    @Before
    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxTotal(200);
        config.setMaxWaitMillis(30 * 1000);
        config.setTestOnBorrow(true);

        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 30000, "redis");

        factory = new RedisDistributedFactory(jedisPool);
    }

    /**
     * 测试锁
     */
    @Test
    public void testLock() {
        int n = 500;

        // JUnit不支持多线程，需要一个总的倒数锁让主线程阻塞
        CountDownLatch totalCountDownLatch = new CountDownLatch(n);
        // 为了模拟所有线程同时开始执行，需要一个倒数锁
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < n; i++) {
            new TestLockThread(i, countDownLatch, totalCountDownLatch).start();
        }
        countDownLatch.countDown();

        totalCountDownLatch.await();
    }

    /**
     * 测试队列
     */
    @Test
    public void testQueue() {
        // 预先创建一定的红包放入队列中
        Queue<Map> queue = factory.createQueue("test_queue", Map.class);
        for (int i = 0; i < 20; i++) {
            queue.push([
                    idx   : i,
                    amount: (int) (Math.random() * 100) + 5
            ]);
        }

        int n = 1500;

        long startTime = System.currentTimeMillis();
        // JUnit不支持多线程，需要一个总的倒数锁让主线程阻塞
        CountDownLatch totalCountDownLatch = new CountDownLatch(n);
        // 为了模拟所有线程同时开始执行，需要一个倒数锁
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < n; i++) {
            new TestQueueThread(i, countDownLatch, totalCountDownLatch).start();
        }
        countDownLatch.countDown();

        totalCountDownLatch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("cost: " + (endTime - startTime));
    }

    /**
     * 测试Hash表
     */
    @Test
    public void testHash() {
        Hash<Map> hash = factory.createHash("test_hash", Map.class);
        hash.put("a", [
                name: "test-a",
                age : 1
        ]);
        hash.put("b", [
                name: "test-b",
                age : 1
        ]);

        println "test get: " + hash.get("a");
        println "test size: " + hash.size();
        println "test keys: " + hash.keys();
        println "test remove: " + hash.remove("a");

//        hash.clear();
    }

    /**
     * 锁测试线程
     */
    private class TestLockThread extends Thread {
        int num;

        CountDownLatch countDownLatch;

        CountDownLatch totalCountDownLatch;

        TestLockThread(int num, CountDownLatch countDownLatch, CountDownLatch totalCountDownLatch) {
            this.num = num;
            this.countDownLatch = countDownLatch;
            this.totalCountDownLatch = totalCountDownLatch;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();

                Lock lock = factory.createLock("test_lock");
                lock.lock(30 * 1000, 60 * 1000);
                println "线程-${this.num}，实例（${lock.toString()}）获取到锁";
                Thread.currentThread().sleep(10);
                println "线程-${this.num}，实例（${lock.toString()}）释放锁";
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                totalCountDownLatch.countDown();
            }
        }
    }

    /**
     * 队列测试线程
     */
    private class TestQueueThread extends Thread {
        int num;

        CountDownLatch countDownLatch;

        CountDownLatch totalCountDownLatch;

        TestQueueThread(int num, CountDownLatch countDownLatch, CountDownLatch totalCountDownLatch) {
            this.num = num;
            this.countDownLatch = countDownLatch;
            this.totalCountDownLatch = totalCountDownLatch;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();

                Queue<Map> queue = factory.createQueue("test_queue", Map.class);
                Map rs = queue.pop();
                if (rs) {
                    println "线程-${this.num}抢到红包，编号${rs.idx}，金额：${rs.amount}";
                } else {
//                    println "线程-${this.num}未抢到红包";
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                totalCountDownLatch.countDown();
            }
        }
    }

}
