package com.mao.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 公平
 * https://blog.csdn.net/nimasike/article/details/51567653 -- 使用ZooKeeper实现Java跨JVM的分布式锁
 * @author bigdope
 * @create 2020-01-09
 **/
public class ZKFairDistributinLock implements Lock {

    // 操作的zk节点
    private String lockPath;

    private InterProcessMutex lock;

    public ZKFairDistributinLock(String lockPath) {
        this.lockPath = lockPath;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework  client = CuratorFrameworkFactory.newClient("192.168.0.82:2181", retryPolicy);
        client.start();
        lock = new InterProcessMutex(client, lockPath);
    }

    @Override
    public void lock() {
        try {
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean isLock = false;
        try {
            isLock = lock.acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLock;
    }

    @Override
    public void unlock() {
        try {
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
