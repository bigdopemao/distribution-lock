package com.mao;

import com.mao.utils.OrderIdGenerator;

/**
 * @author bigdope
 * @create 2020-01-09
 **/
public class Test {

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        TestOrder testOrder = new TestOrder();
        OrderIdGenerator orderIdGenerator = testOrder.getOrderIdGenerator();
        System.out.println(orderIdGenerator.getOrderCode());
        System.out.println(orderIdGenerator.getOldOrderCode());
    }

    private static void test2() {
        TestOrder testOrder = new TestOrder();
        OrderIdGenerator orderIdGenerator = testOrder.getOrderIdGenerator();
//        System.out.println(orderIdGenerator.getOrderCode());
        System.out.println(orderIdGenerator.getOldOrderCode());
    }

    private static class TestOrder {
        private static OrderIdGenerator orderIdGenerator = new OrderIdGenerator();

        public static OrderIdGenerator getOrderIdGenerator() {
            return orderIdGenerator;
        }

        public static void setOrderIdGenerator(OrderIdGenerator orderIdGenerator) {
            TestOrder.orderIdGenerator = orderIdGenerator;
        }
    }
}
