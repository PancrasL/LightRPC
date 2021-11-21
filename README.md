# 1. LightRPC

LightRPC 是一款以学习RPC原理为目的的轻量级 RPC 框架。

本意是通过重复造轮子理解RPC原理，巩固对于自己所掌握的知识的运用。

## 1.1 特性

- 支持Netty和Socket两种传输方式

- 支持zookeeper、redis两种注册中心

- 支持java序列化、kryo、protostuff三种序列化机制

- 基于SPI机制实现了多负载均衡策略：

- 服务端的IO线程和业务线程分离，优化执行效率

- 重用客户端Channel，避免重复连接服务器

- 使用CompletableFuture包装客户端返回结果

- 异常处理：netty连接断开时直接关闭Channel

- 使用关闭钩子释放资源

# 2. 模块介绍

- sparrow-core：rpc框架的核心组件

- example-server ：服务器示例

- example-client ：客户端示例

# 3. 运行

## 3.1 Zookeeper的本地启动

- 下载zookeeper安装包并解压：https://downloads.apache.org/zookeeper/zookeeper-3.6.3/

- 进入解压目录，复制 `conf` 下的 `zoo_sample.cfg` 为 `zoo.cfg` ，并配置文件中的 `dataDir` 字段为 `dataDir=../data`
  ，同时在解压目录下创建data文件夹

- 启动zookeeper

```bash
$ cd bin
$ zkServer.cmd
...
some info
...
```

- zookeeper会占用8080端口，通过在 `zoo.cfg` 中添加 `admin.serverPort=8888` 解除8080端口的占用

## 3.2 Zookeeper的容器启动

```bash
$ docker run -d --name=zookeeper -p 2181:2181 zookeeper:3.6.3
```

## 3.3 基于socket通信的示例的运行

- 启动 `Zookeeper`

- 启动 `example-server/src/main/java/github/pancras/api/SocketServerMain.java`

- 启动 `example-client/src/main/java/github/pancras/api/SocketClientMain.java`

## 3.4 基于netty通信的示例的运行

- 启动 `Zookeeper`

- 启动 `example-server/src/main/java/github/pancras/api/NettyServerMain.java`

- 启动 `example-client/src/main/java/github/pancras/api/NettyClientMain.java`

## 3.5 注解方式启动
- 启动 `Zookeeper`

- 启动 `example-server/src/main/java/github/pancras/spring/NettyServerApplication.java`

- 启动 `example-client/src/main/java/github/pancras/api/NettyClientApplication.java`


> Reference:
> 
> [1] [guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework)
>
> [2] [客户端池化](https://blog.csdn.net/a294634473/article/details/89710187)