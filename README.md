# 1. LightRPC

LightRPC是一款使用Java语言编写，基于Netty和Zookeeper构建的以学习RPC原理为目的的轻量 RPC 框架。

## 1.1 特性

- 支持Netty和Socket两种传输方式

- 支持zookeeper、redis两种注册中心

  - Zookeeper作为注册中心时使用监听器方式更新服务地址

  - Redis作为注册中心时使用定时任务拉取方式更新服务地址

- 支持java序列化、kryo、protostuff三种序列化机制

- 基于SPI机制实现了多负载均衡策略：轮询负载均衡、权重轮询负载均衡、随机负载均衡、一致性哈希负载均衡

- 服务端的IO线程和业务线程分离，优化执行效率

- 基于Netty FixedChannelPool实现了客户端池化，加快调用速度

- 使用CompletableFuture包装客户端返回结果

- 异常处理：netty连接断开时直接关闭Channel

- 使用关闭钩子释放资源

- 使用LengthFieldBasedFrameDecoder解决TCP粘包、拆包

# 2. 模块介绍

- lightrpc-core：rpc框架的核心组件

- lighttcc: 基于LightRPC实现的简易TCC框架

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