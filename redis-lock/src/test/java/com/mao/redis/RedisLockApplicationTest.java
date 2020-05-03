package com.mao.redis;

import com.mao.redis.service.RedisDistributionLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author bigdope
 * @create 2018-12-14
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockApplicationTest {

    private static String LOCK_NO = "TICKET";
    private int tickets = 100;

    private CountDownLatch countDownLatch = new CountDownLatch(3);

    @Autowired
    private RedisDistributionLock redisDistributionLock;

    @Test
    public void testRedisLock() {
        for (int i = 1; i <= 3; i++) {
            String name = "窗口 " + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (tickets >0 ) {
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // 加锁时间
                        Long lockTime = redisDistributionLock.lock(LOCK_NO, name);
                        try {
                            if (tickets > 0) {
                                if (lockTime != null) {
                                    // 开始执行任务
                                    System.out.println(name + " 正在出售第 " + tickets-- + " 张票");
                                }
                            } else {
                                System.out.println("已经出售完毕，请下次再参加，谢谢！");
                            }
                        } finally {
                            redisDistributionLock.unLock(LOCK_NO, lockTime, name);
                        }
                    }
                }
            }).start();

            countDownLatch.countDown();

        }

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetTime() {
        long time = redisDistributionLock.currentTimeForRedis();
        System.out.println("redis 时间");
        System.out.println(time);
    }

    @Test
    public void testGetLock() {
        String name = "窗口 " + 1;
        // 加锁时间
        Long lockTime = redisDistributionLock.lock(LOCK_NO, name);
        try {
            if (lockTime != null) {
                // 开始执行任务
                System.out.println(name + " 正在出售第 " + 1 + " 张票");
            }
        } finally {
//            redisDistributionLock.unLock(LOCK_NO, lockTime, name);
        }
    }

}