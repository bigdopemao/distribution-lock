package com.mao;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * https://blog.csdn.net/sun_wangdong/article/details/77461108
 * @author bigdope
 * @create 2019-01-04
 **/
public class ZkClientDemo {

    // zkServers，zookeeper服务器地址，用“,”分隔。
    private static String zkServers = "bigdope.aliyun:2181";

    // sessionTimeout，会话超时时间，单位毫秒，默认为30000ms。
    private static int sessionTimeOut = 10000;

    // connectionTimeout，连接超时时间。
    private static int connectionTimeout = 10000;

    private static ZkClient client = null;

    private static String userNodePath = "/testUserNode";

    /**
     * 创建客户端连接
     */
    @Before
    public void createClient() {
//        client = new ZkClient(zkServers);
//        client.setZkSerializer(new SerializableSerializer());
        client = new ZkClient(zkServers, sessionTimeOut, connectionTimeout, new SerializableSerializer());
        System.out.println("------client创建成功------");
    }

    /**
     * 创建节点
     */
    @Test
    public void createNode() {
        User user = new User();
        user.setName("hello");
        user.setAge(10);

        /**
         * userNodePath : 节点地址
         * user : 数据的对象
         * CreateMode : 创建的节点类型
         * CreateMode.PERSISTENT : 持久节点
         * CreateMode.PERSISTENT_SEQUENTIAL : 持久顺序节点
         * CreateMode.EPHEMERAL : 临时节点
         * CreateMode.EPHEMERAL_SEQUENTIAL : 临时顺序节点
         */
        String path = client.create(userNodePath, user, CreateMode.PERSISTENT);

        System.out.println("createPaht: " + path);
    }

    /**
     * 获取节点数据
     */
    @Test
    public void getNodeData() {
        Stat stat = new Stat();
        User user = client.readData(userNodePath, stat);
        System.out.println("user, name: " + user.getName() + " , age: " + user.getAge());
        System.out.println("stat: " + stat);
    }

    /**
     * 判断节点是否存在
     * 返回 true表示节点存在 ，false表示不存在
     */
    @Test
    public void existNode() {
        boolean exists = client.exists(userNodePath);
        System.out.println("节点是否存在: " + exists);

    }

    /**
     * 删除节点
     * 返回 true表示节点删除成功 ，false表示节点删除失败
     */
    @Test
    public void deleteNode() {
        // 删除单独一个节点，返回true表示成功
//        boolean userNode = client.delete(userNodePath);
//        System.out.println("删除userNode: " + userNode);
        // 删除含有子节点的节点
        boolean childUserNode = client.deleteRecursive(userNodePath);
        System.out.println("删除childUserNode: " + childUserNode);
    }

    /**
     * 更新节点数据
     */
    @Test
    public void updateNodeData() {
        User user = new User();
        user.setName("hi");
        user.setAge(20);

        client.writeData(userNodePath, user);
    }

    /**
     * 订阅节点的信息改变（创建节点，删除节点，添加子节点，删除子节点）
     */
    @Test
    public void watcherNodeAndChildNode() {
        /**
         * "userNodePath : 监听的节点，可以是现在存在的也可以是不存在的
         */
        client.subscribeChildChanges(userNodePath, new IZkChildListener() {
            /**
             * handleChildChange： 用来处理服务器端发送过来的通知
             * parentPath：对应的父节点的路径
             * currentChilds：子节点的相对路径
             */
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath);
                System.out.println(currentChilds.toString());
            }
        });

        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 订阅节点数据内容变化
     */
    @Test
    public void watcherNodeData() {
        /**
         * "userNodePath : 监听的节点，可以是现在存在的也可以是不存在的
         */
        client.subscribeDataChanges(userNodePath, new IZkDataListener() {

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
