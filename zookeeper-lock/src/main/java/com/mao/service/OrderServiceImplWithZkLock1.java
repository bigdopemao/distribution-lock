package com.mao.service;

import com.mao.curator.ZkReadWriteDistributionLock;
import com.mao.utils.OrderIdGenerator;

/**
 * @author bigdope
 * @create 2019-01-03
 **/
public class OrderServiceImplWithZkLock1 implements OrderService {

    // 用static参数来模拟共用一个订单编号服务
    private static OrderIdGenerator orderIdGenerator = new OrderIdGenerator();

    @Override
    public void createOrder() {
        String orderCode = null;

        // 测试读写锁
        ZkReadWriteDistributionLock lock = new ZkReadWriteDistributionLock("/order");

        // 获取读锁
        // 在写锁没释放之前能读取写锁
        // 在读锁没释放之前不能读取写锁
        try {
            lock.readLock();
            System.out.println(Thread.currentThread().getName() + "获取到读锁");
            // 获取订单号
            orderCode = orderIdGenerator.getOrderCode();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unReadLock();
            System.out.println(Thread.currentThread().getName() + "释放到读锁");
        }

        System.out.println(Thread.currentThread().getName() + "\t=========>\t" + orderCode);

        // 业务代码，省略

    }

}
