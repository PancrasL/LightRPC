# LightTCC

LightTCC是基于LightRPC实现的简易TCC框架

## 1.1 实现思路

1. 创建全局唯一ID能够让RPC中各个服务感知是同一个TCC
2. 通过注册事务同步器在事务提交成功后，将confirm和cancel方法注册到注册中心
3. 当所有try方法都执行成功后，获取注册中心中所有的confirm方法，通过RPC调用具体的confirm方法
4. 当有try方法执行失败时，获取注册中心中所有的cancel方法，通过RPC调用具体的cancel方法
5. 如果有confirm和cancel方法失败则需要进行日志记录（目前仅实现了日志记录，未来可添加事务重试、人工介入等方式进行处理）

## 1.2 Quick Start

### （1）启动数据库

默认使用h2内存数据库构建，详细配置见application.yaml文件
> Reference: https://blog.csdn.net/zhanglf02/article/details/74502582

### （2）添加数据库

### （3）启动测试样例

> Reference：
>
> [1] https://blog.csdn.net/a294634473/article/details/117081743