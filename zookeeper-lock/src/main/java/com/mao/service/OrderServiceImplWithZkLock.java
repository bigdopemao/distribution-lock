package com.mao.service;

import com.mao.curator.ZKFairDistributinLock;
import com.mao.curator.ZKUnFairDistributinLock;
import com.mao.curator.ZkReadWriteDistributionLock;
import com.mao.lock.ZkEDistributionLock;
import com.mao.lock.ZkSDistributionLock;
import com.mao.utils.OrderIdGenerator;

import java.util.concurrent.locks.Lock;

/**
 * @author bigdope
 * @create 2019-01-03
 **/
public class OrderServiceImplWithZkLock implements OrderService {

    // 用static参数来模拟共用一个订单编号服务
    private static OrderIdGenerator orderIdGenerator = new OrderIdGenerator();

    @Override
    public void createOrder() {
        String orderCode = null;

        // 临时节点
//        Lock lock = new ZkEDistributionLock("/order");
//        Lock lock = new ZkSDistributionLock("/order");

        // curator
        Lock lock = new ZKFairDistributinLock("/order");
//        Lock lock = new ZKUnFairDistributinLock("/order");

        try {
            lock.lock();
            // 获取订单号
            orderCode = orderIdGenerator.getOrderCode();
        } finally {
            lock.unlock();
        }

        System.out.println(Thread.currentThread().getName() + "\t=========>\t" + orderCode);

        // 业务代码，省略

    }

}
