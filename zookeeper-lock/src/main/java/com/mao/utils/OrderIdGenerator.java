package com.mao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bigdope
 * @create 2019-01-03
 **/
public class OrderIdGenerator {

    // 自增长序列
    private int i = 0;

    private String oldOrderCode = null;

    // 按照 "年-月-日-小时-分钟-秒-自增长序列"的规则生成订单编号
    public String getOrderCode() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String numStr = String.valueOf(++i);
        StringBuilder sb = new StringBuilder();
        if (numStr.length() == 1) {
            sb.append("0000");
        } else if (numStr.length() == 2) {
            sb.append("000");
        } else if (numStr.length() == 3) {
            sb.append("00");
        } else if (numStr.length() == 4) {
            sb.append("0");
        }
        sb.append(numStr);
        String orderCode = simpleDateFormat.format(now) + sb.toString();
        this.oldOrderCode = orderCode;
        return orderCode;
    }

    public String getOldOrderCode() {
        System.out.println("i === " + i);
        return this.oldOrderCode;
    }

    /**
     * 模拟生成10订单id
     * @param args
     */
    public static void main(String[] args) {
        OrderIdGenerator orderIdGenerator = new OrderIdGenerator();
        for (int i = 0; i < 10; i++) {
            System.out.println(orderIdGenerator.getOrderCode());
            
        }
    }

}
