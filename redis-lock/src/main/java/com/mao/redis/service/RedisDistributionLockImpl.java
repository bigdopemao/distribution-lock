package com.mao.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author bigdope
 * @create 2018-12-14
 **/
@Service
public class RedisDistributionLockImpl implements RedisDistributionLock {

    //加锁超时数据，单位毫秒，即：加锁时间内执行完操作，如果未完成会有并发现象。
    private static final long LOCK_TIMEOUT =  5 * 1000;
//    private static final long LOCK_TIMEOUT =  5 * 10000;

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributionLockImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * 取到锁加锁，取不到锁一直等待直到获取锁
     * @param lockKey
     * @param threadName
     * @return
     */
    @Override
    public long lock(String lockKey, String threadName) {
        logger.info(threadName + " 开始执行加锁");
        while (true) { //循环获取锁
            Long lock_timeout = this.currentTimeForRedis() + LOCK_TIMEOUT + 1;
            if (redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    //定义序列化方式
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] value = serializer.serialize(lock_timeout.toString());
                    Boolean flag = redisConnection.setNX(lockKey.getBytes(), value);
                    return flag;
                }
            })) {
                //如果加锁成功
                logger.info(threadName + " 加锁成功 +++++++++ 111111");
                //设置超时时间，释放内存
                redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                //返回加锁时间
                return lock_timeout;
            } else {
                //获取redis里面的时间
                String result = redisTemplate.opsForValue().get(lockKey);
                Long current_lock_timeout = result == null ? null : Long.parseLong(result);
                //锁已失效
                if (current_lock_timeout != null && current_lock_timeout < this.currentTimeForRedis()) {
                    //判断是否为空，不为空时，如果被其他线程设置了值，则第二个条件判断无法执行
                    //获取上一个锁到期时间，并设置现在的锁到期时间
                    Long old_lock_timeout = Long.parseLong(redisTemplate.opsForValue().getAndSet(lockKey, lock_timeout.toString()));
                    if (old_lock_timeout != null && old_lock_timeout.equals(current_lock_timeout)) {
                        //多线程运行时，多个线程正好都到了这里，但只有一个线程的设置值和当前值仙童，它才有权利获取锁
                        logger.info(threadName + "加锁成功 +++++++++ 222222");
                        //设置超时时间，释放内存
                        redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                        //返回加锁时间
                        return lock_timeout;
                    }
                }
            }

            try {
                logger.info(threadName + " 等待加锁：睡眠100毫秒");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 解锁
     * @param lockKey
     * @param lockValue
     * @param threadName
     */
    @Override
    public void unLock(String lockKey, long lockValue, String threadName) {
        logger.info(threadName + " 执行解锁 -----------");
        //正常直接删除，如果异常关闭判断加锁会判断过期时间
        String result = redisTemplate.opsForValue().get(lockKey);
        Long current_lock_timeout = result == null ? null : Long.parseLong(result);

        //如果是加锁者，则删除锁，如果不是，则等待自动过期，重新竞争加锁
        if (current_lock_timeout != null && current_lock_timeout == lockValue) {
            redisTemplate.delete(lockKey);
            logger.info(threadName + " 解锁成功 -----------");
        }
    }

    @Override
    public long currentTimeForRedis() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }

}
