package com.mao.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver;
import org.apache.zookeeper.CreateMode;

/**
 * @author bigdope
 * @create 2020-01-09
 **/
public class UnFairLockInternalsDriver extends StandardLockInternalsDriver {

    private int randomNum;
    private final static int DEFAULT_RANDOM_NUM = 5;

    public UnFairLockInternalsDriver() {
        this(DEFAULT_RANDOM_NUM);
    }

    public UnFairLockInternalsDriver(int randomNum) {
        this.randomNum = randomNum;
    }

    @Override
    public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
        String newPath = path + getRandomNumStr();
        String ourPath;
        if (lockNodeBytes != null) {
            ourPath = (String)((ACLBackgroundPathAndBytesable)client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL)).forPath(newPath, lockNodeBytes);
        } else {
            ourPath = (String)((ACLBackgroundPathAndBytesable)client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL)).forPath(newPath);
        }

        return ourPath;
    }

    /**
     * 获取随机数字符串
     * @return
     */
    private String getRandomNumStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < randomNum; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
