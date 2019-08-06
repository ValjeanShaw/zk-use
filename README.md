# zookeeper

## zk-shell基本操作
```shell
创建   create
更新   set
查询   get
删除   delete
```

## zk的四种节点类型
1. 持久节点
2. 持久顺序节点
3. 临时节点
4. 临时顺序节点

### 1.持久节点
> 数据节点创建后，一直存在，直到有删除操作主动清除

创建方式：`create /zk-node data`

### 2.持久顺序节点
> 节点一直存在，zk自动追加数字后缀做节点名，后缀上限 MAX(int)

创建方式：`create -s /zk-node data`

### 3.临时节点
> 生命周期和会话相同，客户端会话失效，则临时节点被清除

创建方式：`create -e /zk-node-temp data`

### 4.临时顺序节点
> 临时节点+顺序节点后缀

创建方式：`create -s -e /zk-node-temp data`