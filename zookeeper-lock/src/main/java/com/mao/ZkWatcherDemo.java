package com.mao;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author bigdope
 * @create 2019-01-03
 **/
public class ZkWatcherDemo {

    public static void main(String[] args) {
        // 创建一个zk客户端
        ZkClient client = new ZkClient("bigdope.aliyun:2181");
        client.setZkSerializer(new MyZkSerializer());

        client.subscribeDataChanges("/test", new IZkDataListener() {

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("------收到节点数据变化" + data + "---------");

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("------收到节点被删除了---------");
            }

        });

        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
