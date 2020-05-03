package com.mao.redis;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author bigdope
 * @create 2018-12-18
 **/
public class TestCache {

    private final static int THREAD_NUM = 1000;
    //    private static Map<String, String> map = new ConcurrentHashMap<>();
        private static Map<String, String> lockMap = new ConcurrentHashMap<>();
//    private static Map<String, Boolean> lockMap = new ConcurrentHashMap<>();
    private static CountDownLatch countDownLatch1 = new CountDownLatch(THREAD_NUM);
    @Test
    public void test() {
//         boolean result = map.putIfAbsent("a", "aaa") == null ? true : false;
//         System.out.println(result);
//
//         boolean result1 = map.putIfAbsent("b", "bbb") == null ? true : false;
//         System.out.println(result1);


    }

    @Test
    public void testCache() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            Thread thread = new Thread(() -> {
                try {
                    countDownLatch1.await();
                    getInfo("a");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i] = thread;
            thread.start();
            countDownLatch1.countDown();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }


    public void getInfo(String lockKey) {
        boolean lock = lockMap.putIfAbsent(lockKey, "true") == null ? true : false;
//        boolean lock = lockMap.putIfAbsent(lockKey, true) == true ? true : false;
        if (lock) {
            System.out.println(Thread.currentThread().getName() + " 从数据库中获取数据111111");
            lockMap.remove(lockKey);
        } else {
            System.out.println(Thread.currentThread().getName() + " 输出特定的值222222");
        }
    }

}
