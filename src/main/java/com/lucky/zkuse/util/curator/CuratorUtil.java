package com.lucky.zkuse.util.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author xiaoran
 * @date 2019/08/08
 */
public class CuratorUtil {

    CuratorFramework client;
    public CuratorUtil(String url) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(5000)
                //重试机制
                .retryPolicy(retryPolicy)
                //命名空间  可选
                .namespace("curator")
                .build();
        client.start();
    }

    /**
     * 创建节点，带有值
     *
     * @param path
     * @param value
     * @throws Exception
     */
    public void createNode(String path,String value) throws Exception{
        client.create()
                //若不存在父节点，即创建父节点   zk中非叶子节点必须是持久节点
                .creatingParentsIfNeeded()
                //节点类型
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,value.getBytes());
    }

    /**
     * 获取数据
     * @param path
     * @return
     * @throws Exception
     */
    public String getData(String path) throws Exception{
        Stat stat = new Stat();
        String value = new String(client.getData().storingStatIn(stat).forPath(path));
        return value;
    }

    /**
     * 删除数据
     * @param path
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception{
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        client.delete()
                //强制删除  可省略
                .guaranteed()
                .deletingChildrenIfNeeded()
                //强制指定版本
                .withVersion(stat.getVersion())
                .forPath(path);
    }

    /**
     * 自旋方式保证更新数据
     * @param path
     * @throws Exception
     */
    public void setData(String path) throws Exception{
        Stat stat = new Stat();
        String value = new String(client.getData().storingStatIn(stat).forPath(path));
        //自旋
        while (true){
            try{
                client.setData()
                        //乐观锁 version   cas方式
                        .withVersion(stat.getVersion())
                        .forPath(path);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws Exception{
        String url = "172.23.7.9:2181,172.23.7.10:2181,172.23.7.12:2181";
        CuratorUtil curatorUtil = new CuratorUtil(url);
        curatorUtil.createNode("/zoc","uuu");

        System.out.println(curatorUtil.getData("/zoc"));
        Thread.sleep(Integer.MAX_VALUE);
    }
}
