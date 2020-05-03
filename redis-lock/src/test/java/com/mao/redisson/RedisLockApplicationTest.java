package com.mao.redisson;

import com.mao.redisson.RedissonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author bigdope
 * @create 2018-12-14
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockApplicationTest {

    private static String LOCK_NO = "TICKET";
    private int tickets = 100;

    @Test
    public void testRedissonLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://47.98.183.70:6379").setPassword("123456");
        RedissonClient redissonClient = RedissonUtils.getInstance().getRedisson(config);

        RLock rLock = RedissonUtils.getInstance().getRLock(redissonClient, LOCK_NO);

        for (int i = 1; i <= 3; i++) {
            String name = "窗口 " + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (tickets >0 ) {

//                        try {
//                            rLock.lock(1000, TimeUnit.MILLISECONDS);
//                            try {
////                                Thread.sleep(2000);
//                                if (tickets > 0) {
//                                    // 开始执行任务
//                                    System.out.println(name + " 正在出售第 " + tickets-- + " 张票");
//                                } else {
//                                    System.out.println("已经出售完毕，请下次再参加，谢谢！");
//                                }
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                System.out.println(name + " 被中断了");
//                                e.printStackTrace();
//                            } finally {
//                                rLock.unlock();
//                            }
//                        } catch (IllegalMonitorStateException e) {
//                            System.out.println(name + " 解锁失败了");
//                        }

                        rLock.lock();
                        try {
                            if (tickets > 0) {
                                // 开始执行任务
                                System.out.println(name + " 正在出售第 " + tickets-- + " 张票");
                            } else {
                                System.out.println("已经出售完毕，请下次再参加，谢谢！");
                            }
                        } finally {
                            rLock.unlock();
                        }
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test() throws InterruptedException {
        //redisson配置
        Config config = new Config();

        //redisjiqun
//        config.useClusterServers().addNodeAddress("redis://47.98.183.70:6379").setPassword("123456");

        //单个redis服务
        SingleServerConfig singleSerververConfig = config.useSingleServer();
//        singleSerververConfig.setAddress("127.0.0.1:6379");
        singleSerververConfig.setAddress("redis://47.98.183.70:6379");
        singleSerververConfig.setPassword("123456");
        //redisson客户端
        RedissonClient redissonClient = RedissonUtils.getInstance().getRedisson(config);
        RBucket<Object> rBucket = RedissonUtils.getInstance().getRBucket(redissonClient, "key");
        //rBucket.set("wangnian");
        System.out.println(rBucket.get());

        while (true) {
            RLock lock = redissonClient.getLock("lock");
            lock.tryLock(0, 1, TimeUnit.SECONDS);//第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
            try {
                System.out.println("执行");
            } finally {
                lock.unlock();
            }
        }

    }

}
