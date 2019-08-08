package com.lucky.zkuse.util.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author xiaoran
 * @date 2019/08/08
 *
 *
 * curator创建连接方式
 *
 *
 */
public class CreateSessionSampleFluent {
    public static void main(String[] args) throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("172.23.7.9:2181,172.23.7.10:2181,172.23.7.12:2181")
                .sessionTimeoutMs(5000)
                //重试机制
                .retryPolicy(retryPolicy)
                //命名空间
                .namespace("curator")
                .build();

        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
