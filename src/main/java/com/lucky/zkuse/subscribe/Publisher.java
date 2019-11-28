package com.lucky.zkuse.subscribe;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 数据发布者
 *
 * @author xiaoran
 * @date 2019/11/28
 */
public class Publisher implements Watcher {
    //为保证连接正常后，zk对象才创建成功
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String path ="/publish";

    public static void main(String[] args) throws Exception {

        ZooKeeper zooKeeper = new ZooKeeper("172.23.7.9,172.23.7.10,172.23.7.12:2181",2000,new Publisher());
        countDownLatch.await();
        int i=0;
        while(true){
            System.out.println( "发布消息------------>>>>:"+i);
            zooKeeper.setData(path,String.valueOf(i).getBytes(),-1);
            Thread.sleep(1000L);
            i++;
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getState() == Event.KeeperState.SyncConnected){
            System.out.println("发布端连接成功!!!"+event.getState());
            countDownLatch.countDown();
        }
    }
}
