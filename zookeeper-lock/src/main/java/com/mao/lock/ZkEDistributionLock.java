package com.mao.lock;

import com.mao.MyZkSerializer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 利用zookeeper的同父子节点不可重名的特点来实现分布式锁
 * 加锁：会创建指定名称的节点，如果能创建成功，则获得锁（加锁成功），
 *      如果节点已存在，则表示锁被人获取了，你就等阻塞，等待
 * 释放锁：删除指定名称的节点
 * @author bigdope
 * @create 2019-01-03
 **/
public class ZkEDistributionLock implements Lock {

    // 操作的zk节点
    private String lockPath;

    // zk client
    private ZkClient client;

    public ZkEDistributionLock(String lockPath) {
//        super();
        this.lockPath = lockPath;
        client = new ZkClient("bigdope.aliyun:2181");
        client.setZkSerializer(new MyZkSerializer());
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            // 阻塞等待
            waitForLock();

            // 再次尝试加锁，回调自己
            lock();
        }

    }

    private void waitForLock() {
        // 使用 CountDownLatch 实现阻塞
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 注册watcher
        IZkDataListener listener = new IZkDataListener() {

            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("------ 监听到节点被删除 ------");
                countDownLatch.countDown();
            }

        };

        // 完成watcher注册
        client.subscribeDataChanges(lockPath, listener);

        // 阻塞自己
        if (client.exists(lockPath)) {
            try {
                // 阻塞自己，等待 countDown
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 取消watcher注册
        client.unsubscribeDataChanges(lockPath, listener);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            // 创建临时节点，存在会抛出异常
            client.createEphemeral(lockPath);
        } catch (ZkNodeExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        // 删除节点
        client.delete(lockPath);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
