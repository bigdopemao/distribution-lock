package com.mao.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * 读写锁
 * https://blog.csdn.net/nimasike/article/details/51581755 -- 使用ZooKeeper实现Java跨JVM的分布式锁(读写锁)
 * @author bigdope
 * @create 2020-01-09
 **/
public class ZkReadWriteDistributionLock {

    // 操作的zk节点
    private String lockPath;
    // 读锁
    private InterProcessMutex readLock;
    // 写锁
    private InterProcessMutex writeLock;

    public ZkReadWriteDistributionLock(String lockPath) {
        this.lockPath = lockPath;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.82:2181", retryPolicy);
        client.start();
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, lockPath);
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    /**
     * 读锁
     */
    public void readLock() {
        try {
            readLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean tryReadLock(long time, TimeUnit unit) throws InterruptedException {
        boolean isLock = false;
        try {
            isLock = readLock.acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLock;
    }

    public void unReadLock() {
        try {
            readLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写锁
     */
    public void writeLock() {
        try {
            writeLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean tryWriteLock(long time, TimeUnit unit) throws InterruptedException {
        boolean isLock = false;
        try {
            isLock = writeLock.acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLock;
    }

    public void unWriteLock() {
        try {
            writeLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
