# aradin
阿拉丁基础开发框架
以SpringCloud及SpringCloud Alibaba为基础做上层通用功能包扩展，  
  * 规范依赖
  * 降低集成复杂度    
  * 扩充部分必要的能力     
  * 解决部分新旧组件交替淘汰时发生的兼容性问题  

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
* skywalking集成（javaagent方式）
* salarm告警控制模板
* 分布式caffeine改造

# v0.0.4 依赖升级（进行中）
* springboot由2.3.x升级至2.6.2
* springcloud升级至2021.0.0
* 对应相应的spring版本升级
* 说明文档补充