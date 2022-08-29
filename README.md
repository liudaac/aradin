# ARADIN
**阿拉丁基础开发框架**
以SpringCloud及SpringCloud Alibaba为基础做上层通用功能包扩展，  
  * 规范依赖
  * 降低集成复杂度    
  * 扩充部分必要的能力     
  * 解决部分新旧组件交替淘汰时发生的兼容性问题

***
## 主框架版本说明
### 0.0.3.x （发布版）
<p>SpringCloud Hoxton.SR12</p>
<p>SpringBoot 2.3.12.RELEASE</p>
<p>SpringCloudAlibaba 2.2.6.RELEASE</p>

### 0.0.4.x （体验版）
<p>SpringCloud 2021.0.3</p>
<p>SpringBoot 2.7.0</p>
<p>SpringCloudAlibaba 2021.0.1.0</p>

### 推荐版本
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">0.0.3.19</a>
***
## 模块结构
### 1、aradin-spring
spring加强，面向线上使用场景，扩充协议文档、缓存、模板、心跳集成能力
+ **aradin-spring-core**
<p>基础能力模块</p>
<p>1.1 cn.aradin.spring.core.algo.* 算法包   目前只有SWRR负载均衡</p>
<p>1.2 cn.aradin.spring.core.bean.* BEAN工具  
<p>&nbsp;&nbsp;1.2.1 AradinBeanFactory BEAN工厂类支持对指定Class通过Prefix命名方式进行路由，常用于某个类需要按规则初始化数量大于1的有限个BEAN，比如分库逻辑，读写分离逻辑；</p>
<p>&nbsp;&nbsp;1.2.2 AradinPropertySourceFactory 支持对yml配置文件的加载，使用方式 </p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;@Configuration</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;@PropertySource(value = "classpath:config.yml", factory = AradinPropertySourceFactory.class)</p>
<p>&nbsp;&nbsp;方便灵活的布局配置文件</p>

+ **aradin-spring-acutator-starter**
<p>心跳组件模块，在spring原生actuator基础上增加inited,offline,online三个服务管理节点</p>
<p>1.1 /inited 查看服务的初始化状态</p>
<p>1.2 /online 持续集成发布时增加上线后的处理逻辑，比如容器应用启动应用后执行指定的脚本文件（如日志采集），也支持扩展Handler实现业务高度定制的启动逻辑</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOnlineHandler 会自动调用应用上下文中所有该类型的BEAN</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;此外还支持配置项 aradin.actuator.online.shell 配置启动脚本路径</p>
<p>1.3 /offline 方便下线时平滑关闭应用</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOfflineHandler 会在自动调用应用上下文所有该类型的BEAN后才去调用Spring上下文的close方法</p>
<p></p>
+ *aradin-spring-velocity-starter*
<p></p>
+ *aradin-spring-swagger-starter*
<p></p>
+ *aradin-spring-salarm-starter*
<p></p>
+ *aradin-spring-caffeine-starter*
<p></p>
+ *aradin-spring-redis-starter*
<p></p>
+ *aradin-spring-redisson-starter*
<p></p>

### 2、aradin-alibaba


### 3、aradin-mybatis


### 4、aradin-lucene


### 5、aradin-zookeeper


### 6、aradin-version

