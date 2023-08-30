# v0.0.1 基础能力 (已完成) 
  * 基础结构
  * 健康检查
  * caffeine缓存集成
  * lts集成
  * redis集成
  * redisson集成
  * swagger集成
  * velocity兼容
  * 一般性web防护
  * Mybatis及Mybatis Plus集成

# v0.0.2 能力扩充（已完成）
* alibaba dubbo
* alibaba nacos
* alibaba sentinel
* zookeeper监听集成
  * 基于zookeeper版本同步
  * 基于zookeeper集群注册
* 分布式内存缓存
* 分库分表方案，支持分割算法扩展

# v0.0.3 权限扩充（已完成）
* security集成
* salarm告警控制模板
* 分布式caffeine改造

# v0.0.4 依赖升级（已完成）
* springboot由2.3.x升级至2.7.5
* springcloud升级至2021.0.4
* 对应相应的spring版本升级
* 说明文档补充

# v1.0.1 （已完成）
* 新增aradin-easy-http模块（反射）+aradin-easy-http-buddy模块（bytebuddy字节码增强），实现Http客户端注解代理，完全模仿springmvc的Controller，类Feign功能的纯净版HttpClient构造器，不依赖spring，提供调用环境隔离方案和body加密扩展槽（FINISH）
* aradin-spring-caffeine 增加定时cleanUp机制，避免过度依赖Caffiene的使用时过期检测机制导致大量过期Key堆积的问题。并支持配置项
* aradin-spring-actuator-starter 增加KafkaListener的注销触发，增强平滑下线的实现
* aradin-spring-core HttpError增加instance()静态构造方法
* 升级相关基础依赖版本, springboot升级至2.7.12, dubbo升级至3.1.10

# v1.0.2 （已完成）
* 对于session的redis扩展包spring-session-data-redis，将session用的redis与业务redis进行分离，用于解决服务间共享登录状态时的redis捆绑问题
* redis-starter增加随机时间偏移的配置，解决生产环境下的缓存雪崩问题，相关配置也向spring-data-redis提交了PR
* redis-starter增加分桶RedisBucketTemplate，解决hash，set的大key问题
* cluster模式下中间件依赖增加nacos支持
* version模块版本状态机配置简化，降低caffeine等配置复杂度，提高易读性
* 升级相关基础依赖版本, springboot升级至2.7.15, dubbo升级至3.1.11

# v1.0.3 (开发中)