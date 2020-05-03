package com.mao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * https://blog.csdn.net/u012867699/article/details/78796114/ -- 分布式锁解决并发的三种实现方式
 * https://blog.csdn.net/wuzhiwei549/article/details/80692278 -- 三种实现分布式锁的方式
 * @author bigdope
 * @create 2018-12-14
 **/
@SpringBootApplication
public class RedisLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class, args);
    }

}
