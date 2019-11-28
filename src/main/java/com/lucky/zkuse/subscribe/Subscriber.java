package com.lucky.zkuse.subscribe;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 数据订阅者
 *
 * @author xiaoran
 * @date 2019/11/28
 */
public class Subscriber implements Watcher {
    //为保证连接正常后，zk对象才创建成功
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static String path ="/publish";
    private static Stat stat = new Stat();
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper("172.23.7.9,172.23.7.10,172.23.7.12:2181",2000,new Subscriber());
        countDownLatch.await();
        byte[] temp = zooKeeper.getData(path,true,stat);
        System.out.println("首次订阅消息："+new String(temp));
        while(true){
            Thread.sleep(Integer.MAX_VALUE);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && event.getPath() == null){
                countDownLatch.countDown();
            }else if(event.getType()  == Event.EventType.NodeDataChanged){
                //Clinet需要去拉取最新的数据信息
                try {
                    byte[] newByte = zooKeeper.getData(event.getPath(),true,stat);
                    System.out.println("订阅的消息到达--------->>>> path:"+event.getPath()+"\t数据改动\t 新数据 :"+ new String(newByte));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
