package com.mao;

import com.mao.service.OrderService;
import com.mao.service.OrderServiceImplWithZkLock;
import com.mao.service.OrderServiceImplWithZkLock1;
import com.mao.service.OrderServiceImplWithZkLock2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author bigdope
 * @create 2019-01-04
 **/
public class ZkLockTest {

    public static void main(String[] args) throws Exception {
        // 并发数
        int currency = 20;

        // 循环屏障
        CyclicBarrier cyclicBarrier = new CyclicBarrier(currency);

        for (int i = 0; i < currency; i++) {

            new Thread(() -> {
                // 模拟分布式集群的场景
//                OrderService orderService = new OrderServiceImplWithZkLock();
                OrderService orderService = new OrderServiceImplWithZkLock1();

                System.out.println(Thread.currentThread().getName() + "------ 我准备好了 ------");

                // 等待一起出发
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                // 调用创建订单服务
                orderService.createOrder();
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 模拟分布式集群的场景
//                    OrderService orderService = new OrderServiceImplWithZkLock();
                    OrderService orderService = new OrderServiceImplWithZkLock2();

                    System.out.println(Thread.currentThread().getName() + "------ 我准备好了 ------");

                    // 等待一起出发
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    // 调用创建订单服务
                    orderService.createOrder();
                }
            }).start();
        }

    }

}
