# 1. Sparrow

sparrow 是一款以学习RPC原理为目的的轻量 RPC 框架。

本意是通过重复造轮子理解RPC原理，巩固对于自己所掌握的知识的运用。

# 2. 模块介绍

- sparrow-core：rpc框架的核心组件

- example-server ：服务器示例

- example-client ：客户端示例

# 3. 运行

## 3.1 Zookeeper的安装

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

## 3.2 基于socket通信的示例的运行

- 启动 `Zookeeper`

- 修改 `sparrow-core/src/main/java/github/pancras/config/ServerConfig.java`
  中的Zookeeper地址为自己的（不修改也行，默认会自动获取本机ip）

- 启动 `example-server/src/main/java/github/pancras/SocketServerMain.java`

- 启动 `example-client/src/main/java/github/pancras/SocketClientMain.java`

## 3.3 基于netty通信的示例的运行

- 启动 `Zookeeper`

- 修改 `sparrow-core/src/main/java/github/pancras/config/ServerConfig.java`
  中的Zookeeper地址为自己的（不修改也行，默认会自动获取本机ip）

- 启动 `example-server/src/main/java/github/pancras/NettyServerMain.java`

- 启动 `example-client/src/main/java/github/pancras/NettyClientMain.java`
