package com.mao.lock;

import com.mao.MyZkSerializer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
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
 * @create 2019-01-04
 **/
public class ZkSDistributionLock implements Lock {

    private String lockPath;

    private ZkClient client;

    private String currentPath;

    private String beforePath;

    public ZkSDistributionLock(String lockPath) {
        super();
        this.lockPath = lockPath;
        client = new ZkClient("bigdope.aliyun:2181");
        client.setZkSerializer(new MyZkSerializer());
        if (!this.client.exists(lockPath)) {
            try {
               this.client.createPersistent(lockPath);
            } catch (Exception e) {

            }
        }
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
        client.subscribeDataChanges(this.beforePath, listener);

        // 阻塞自己
        if (this.client.exists(this.beforePath)) {
            try {
                // 阻塞自己，等待 countDown
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 取消watcher注册
        client.unsubscribeDataChanges(this.beforePath, listener);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        if (this.currentPath == null) {
            currentPath = this.client.createEphemeralSequential(lockPath + "/", "aaa");
        }

        // 获取所有的子节点
        List<String> children = this.client.getChildren(lockPath);

        // 排序list
        Collections.sort(children);

        // 判断当前节点是否是最小的
        if (currentPath.equals(lockPath + "/" + children.get(0))) {
            return true;
        } else {
            // 取到前一个
            // 得到字节的索引号
            int currentIndex = children.indexOf(currentPath.substring(lockPath.length() + 1));
            beforePath = lockPath + "/" + children.get(currentIndex - 1);
        }

        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        // 删除节点
        this.client.delete(this.currentPath);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
