# ARADIN
**阿拉丁基础开发框架**
以SpringCloud及SpringCloud Alibaba为基础做上层通用功能包扩展，已发布至https://mvnrepository.com/artifact/cn.aradin
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

### 当前推荐版本
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">0.0.3.20</a>
***
## 模块结构
### 1、aradin-spring
spring加强，面向线上使用场景，扩充协议文档、缓存、模板、心跳集成能力

***
+ **aradin-spring-core**
<p>&nbsp;基础能力模块</p>
<p>&nbsp;① cn.aradin.spring.core.algo.* 算法包   目前只有SWRR负载均衡</p>
<p>&nbsp;② cn.aradin.spring.core.bean.* BEAN工具  
<p>&nbsp;&nbsp;&nbsp;AradinBeanFactory BEAN工厂类支持对指定Class通过Prefix命名方式进行路由，常用于某个类需要按规则初始化数量大于1的有限个BEAN，比如分库逻辑，读写分离逻辑；</p>
<p>&nbsp;&nbsp;&nbsp;AradinPropertySourceFactory 支持对yml配置文件的加载，使用方式 </p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@Configuration</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertySource(value = "classpath:config.yml", factory = AradinPropertySourceFactory.class)</br>
<p>&nbsp;&nbsp;&nbsp;方便灵活的布局配置文件</p>
<p>&nbsp;③ cn.aradin.spring.core.context.* 上下文运行时变量 支持三种模式，借鉴SpringSecurity源码中该功能实现方式</p> 
&nbsp;&nbsp;&nbsp;MODE_THREADLOCAL;</br>
&nbsp;&nbsp;&nbsp;MODE_INHERITABLETHREADLOCAL;</br>
&nbsp;&nbsp;&nbsp;MODE_GLOBAL;</p>
&nbsp;默认为MODE_THREADLOCAL当前线程本地变量，支持配置入口 aradin.context.strategy</p>
&nbsp;④ cn.aradin.spring.core.enums.* 常量包，字面理解</p>
&nbsp;⑤ cn.aradin.spring.core.net.http.* HTTP工具包，使用入口 HttpClientUtils</p>
&nbsp;⑥ cn.aradin.spring.core.queue.* 轻量级内存队列，可用于低可靠性要求的使用场景，参考AradinQueue构造方法可以注入生产者及消费者</p>
&nbsp;⑦ cn.aradin.spring.core.thread.* 线程池包，使用入口AradinThreadManager</p>
&nbsp;⑧ cn.aradin.spring.core.session.* Session配置，需要搭配@EnableSpringSession或者@EnableRedisHttpSession使用，用于替换webserver容器的默认session机制</p>
&nbsp;&nbsp;&nbsp;参考配置</p>
&nbsp;&nbsp;&nbsp;spring:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cookie:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name: ${COOKIE_NAME}</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max-age: 3600</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;http-only: false</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;security: false</br>

***
+ **aradin-spring-acutator-starter**
<p>&nbsp;心跳组件模块，在spring原生actuator基础上增加inited,offline,online三个服务管理节点</p>
<p>&nbsp;① /inited 查看服务的初始化状态</p>
<p>&nbsp;② /online 持续集成发布时增加上线后的处理逻辑，比如容器应用启动应用后执行指定的脚本文件（如日志采集），也支持扩展Handler实现业务高度定制的启动逻辑</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOnlineHandler 会自动调用应用上下文中所有该类型的BEAN</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此外还支持配置项 aradin.actuator.online.shell 配置启动脚本路径</p>
<p>&nbsp;③ /offline 方便下线时平滑关闭应用</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOfflineHandler 会在自动调用应用上下文所有该类型的BEAN后才去调用Spring上下文的close方法</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;集成方式</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;management.endpoints.web.exposure.include: inited,offline,online #开启需要的endpoints

***
+ **aradin-spring-velocity-starter**
<p>&nbsp;spring2.x之后不支持velocity的集成，考虑到旧项目模板代码迁移的复杂性，特别提供velocity的兼容包</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;配置方式与原velocity1.x版本一致，例如</p>
&nbsp;spring:</br>
&nbsp;&nbsp;&nbsp;velocity:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true #设定是否允许mvc使用velocity</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache: true #是否开启模板缓存</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;check-template-location: true #是否检查模板路径是否存在</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;prefix: null</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;suffix: .vm #设定velocity模板的后缀</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;date-tool-attribute: null</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;number-tool-attribute: null</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;resource-loader-path: classpath:/template/ #设定模板路径，默认为:classpath:/templates/</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;toolbox-config-location: classpath:/toolbox.xml #设定Velocity Toolbox配置文件的路径，比如 /WEB-INF/toolbox.xml.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;prefer-file-system-access: true #是否优先从文件系统加载模板以支持热加载，默认为true</br>

***
+ **aradin-spring-swagger-starter**
<p>&nbsp;Swagger-bootstrap-ui集成组件，选型的原因是相对原生UI更加友好</p>
<p>&nbsp;① cn.aradin.spring.swagger.starter.dto。Resp 标准返回结构</p>
<p>&nbsp;② 配置方式</p>
&nbsp;&nbsp;aradin:</br>
&nbsp;&nbsp;&nbsp;&nbsp;swagger:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base-package: pk1;pkg2;pk3</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ant-path: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;title: 协议标题</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;description: 协议描述</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;version: 1.0.0</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enable: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-name: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-email: liudaax@126.com</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-url: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;license:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;license-url: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-default-status: false</br>

***
+ **aradin-spring-caffeine-starter**
<p>&nbsp;使用Caffeineson类包装内存缓存Caffeine，实现对应的CacheManager，支持spring-cache注解方式集成</p>
<p>&nbsp;&nbsp;&nbsp;一般情况下，对于内存缓存的更新机制也分为两种：</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;① 利用Caffeine缓存自身的超时机制进行延时Evict，在触发之前持久层对应的数据可能已经发生变更，</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所以对于更新的数据一定时间内存在不一致情况，此种使用方式更适合对一致性要求不严格或者不可变数据的缓存处理，</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;比如SessionID上的使用可以有效降低Redis调用损耗；题目的缓存；热点商品信息的缓存;</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;② 利用分布式中间件，比如nacos，zookeeper，consul等进行变更的通知，以实现各节点内存缓存的同步更新;</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所以该模块对应支持普通模式以及基于版本管理机制的分布式更新模式：</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对应配置如下：</p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aradin:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;caffeine:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: caffeine #默认caffeine</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">versioned: false</font> #为true时启用cachename级别的版本变更控制，需要搭配**aradin-version**模块使用，**aradin-version-zookeeper-starter**部分提供了配置样例</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults: #默认缓存配置</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 1200000 #访问后过期时间，单位毫秒</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 1800000 #写入后过期时间，单位毫秒</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100 #初始化大小</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 10000 #最大缓存对象个数，超过此数量时之前放入的缓存将失效</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true #是否允许空值</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true #是否启用软引用</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs: #自定义cacheName对应的缓存配置</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base: #具体的cache名,与springcache配合使用</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 3600000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 3600000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 7200000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 7200000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</p>
&nbsp;&nbsp;&nbsp;使用方式如下几种：</p>
&nbsp;&nbsp;&nbsp;① 直接引用Caffeineson Bean，配置参考aradin.cache.caffeine.defaults</p>
&nbsp;&nbsp;&nbsp;② 引用CaffeinesonCacheManager Bean 按CacheName获取Caffeineson实例</p>
&nbsp;&nbsp;&nbsp;③ 搭配@EnableCache注解，使用方式@Cachable(cacheManager=CaffeinesonConfiguration.CACHE_MANAGER)</p>
&nbsp;&nbsp;&nbsp;④ 提供Endpoint入口查询Caffeine状态 /caffeineson 可以按照aradin-spring-acutator-starter的配置方式进行开放

***
+ **aradin-spring-redis-starter**
<p>&nbsp;替换spring-boot-starter-data-redis中默认CacheManager实现（由于原生实现不支持多种缓存方式共存），对于各CacheName的初始化使用自定义配置项</p>
&nbsp;配置方式如下</p>
&nbsp;&nbsp;首先是原生配置</p>
&nbsp;&nbsp;&nbsp;spring:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redis:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;database: 0 #Redis数据库索引（默认为0）</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;host:  #Redis服务器地址</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;port: 6379 #Redis服务器连接端口</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password:  #Redis服务器连接密码（默认为空）</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;pool: #连接池配置，不再详细列出</p>
&nbsp;&nbsp;然后是自定义配置</p>
&nbsp;&nbsp;aradin:</br>
&nbsp;&nbsp;&nbsp;&nbsp;cache:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redis:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT60M #https://blog.csdn.net/huang007guo/article/details/81625061</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: default_</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT12H</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: base_</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;client:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT4H</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: client_</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true</p>

***
+ **aradin-spring-redisson-starter**
<p>&nbsp;RedissonClient实例初始化，配置方式与spring-redis配置一致，无需额外配置项</p>

***
+ **aradin-spring-salarm-starter**
<p>&nbsp;告警通知模块，类似log4j的API，实现Content的组装和发送，发送目标默认只提供控制台输出</p>
&nbsp;&nbsp;&nbsp;① cn.aradin.spring.salarm.starter.handler.ISalarmHandler 实现该接口Bean实例，自定义各个通知渠道逻辑</p>
&nbsp;&nbsp;&nbsp;② cn.aradin.spring.salarm.starter.notifier.ISalarm 调用入口，负责告警消息的拼装，去重以及全局ISalarmHandler的分发，引用方式为Bean引用</p>
&nbsp;&nbsp;&nbsp;③ 配置项</p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aradin:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;salarm:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT1H # Min interval for sending a same alarm</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;level: warn # Min level to send</br>

### 2、aradin-alibaba
合并了必要的依赖项和配置样例，并未做额外开发
+ **aradin-alibaba-nacos-starter**

+ **aradin-alibaba-nacos-starter**

***
### 3、aradin-mybatis
+ **aradin-mybatis-plus-starter**
<p>&nbsp;增加了开启分页的配置，由于默认情况下Page查询是必要的，无需下沉到项目实现中再开启</p>

***
### 4、aradin-lucene
+ **aradin-lucene-solr-starter**
<p>&nbsp;使用自定义配置初始化CloudSolrClient Bean</p>
&nbsp;配置如下</p>
&nbsp;&nbsp;&nbsp;spring:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;solr:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zk-hosts:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.1:2181</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.2:2181</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.3:2181</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;chroot: /solrcloud</p>

***
### 5、aradin-zookeeper
+ **aradin-zookeeper-boot-starter**
<p>&nbsp;可同时配置多个ZK集群，并绑定事件路由机制</p>
&nbsp;&nbsp;支持的使用方式如下</p>
&nbsp;&nbsp;① 事件接收方式 实现cn.aradin.zookeeper.boot.starter.handler.INodeHandler Bean实例 通过support方法进行事件过滤，handler方法实现事件的处理</p>
&nbsp;&nbsp;② ZK集群配置方式</p>
&nbsp;&nbsp;&nbsp;&nbsp;aradin:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zookeeper:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;addresses: #支持多组</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;id: ${customized-zookeeper-address-id}</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181/${chroot}</p>
&nbsp;&nbsp;③ ZKClient获取方式 ZookeeperClientManager.getClient(String id)</p>

***
### 6、aradin-version
版本分发引擎，建立标准的版本控制分发机制，并支持选择性集成zookeeper，nacos中间件
+ **aradin-version-core**
<p>&nbsp;版本分发通用模块，对版本分发的上层逻辑进行抽象，并提供分布式一致性中间件的扩展入口，目前支持zookeeper、nacos</p>
  <figure>
  ① cn.aradin.version.core.dispatcher.VersionDispatcher 唤起所有的IVersionHandler(Bean)进行版本变更的发布</p>
  ② cn.aradin.version.core.gentor.IVersionGentor 新版本号生成器，提供默认实现</p>
  ③ cn.aradin.version.core.handler.IVersionBroadHandler 版本发布逻辑，默认实现只打印至控制台，需要根据不同中间件对应不同具体实现
   <figure>
     aradin-version-zookeeper-starter.VersionZookeeperBroadHandler</br>
     aradin-version-nacos-starter.VersionNacosBroadHandler
   </figure>
  ④ cn.aradin.version.core.properties.VersionProperties 相关配置项 **aradin.version**</br>
  
***
+ **aradin-version-zookeeper-starter**
<p>
<figure>
     ① cn.aradin.version.zookeeper.starter.handler.VersionsNodeHandler</br> 
     *接收ZK事件并使用VersionDispatcher(Bean)进行分发，接收方为所有的cn.aradin.version.core.handler.IVersionHandler(Bean)*</br>
     </br>
     ② cn.aradin.version.zookeeper.starter.handler.VersionZookeeperBroadHandler</br>
     *ZK的版本广播触发入口，方便人工触发版本变更事件*</br>
     </br>
     ③ 配置样例</br>
     aradin:</br>
	 &nbsp;&nbsp;version:</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;zookeeper:</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address-id: ${customid}</br>
	 &nbsp;&nbsp;zookeeper:</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;enable: true #default true</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;session-timeout: 5000 #default 5000</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;connection-timeout: 5000 #default 5000</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;addresses:</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- id: ${customid}</br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address:
</figure>
</p>

***
+ **aradin-version-nacos-starter**
<p>
<figure>
	<p>
	① cn.aradin.version.nacos.starter.listener.VersionNacosConfigListener</br> 
	*接收Nacos事件，并使用VersionDispatcher(Bean)进行分发，接收方为所有的cn.aradin.version.core.handler.IVersionHandler(Bean)*</br>
	</br>
	② cn.aradin.version.nacos.starter.handler.VersionNacosBroadHandler</br>
	*Nacos的版本广播触发入口，方便人工触发版本变更事件，另外初始化时同时为指定的group data-id绑定listener*</br>
	</br>
	③ 配置样例</p>
	aradin:</br>
	&nbsp;&nbsp;version:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: </br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data-id: </br>
	spring:</br>
	&nbsp;&nbsp;cloud:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: </br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: </br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;config:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;server-addr: 192.168.1.1:8888,192.168.1.2:8888,192.168.1.3:8888</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;namespace: d78b658c-182a-420a-9005-e8e8f36a1e7d</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group-id: ${aradin.version.nacos.group}</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data-id: ${aradin.version.nacos.data-id}</br>
</figure>
</p>

***
+ **aradin-version-caffeine-starter整合aradin-version-zookeeper-starter实现分布式内存缓存**
<p>&nbsp;aradin-version-caffeine-starter中实现了位于VersionDispatcher(Bean)下游的IVersionHandler（cn.aradin.spring.caffeine.manager.version.CaffeinesonVersionHandler）实现内存信息的版本淘汰机制</br>
<p>&nbsp;① 相关配置如下：可以参考复用至nacos集成</p>
	aradin:</br>
	&nbsp;&nbsp;version:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;zookeeper:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address-id: ${customid}</br>
	&nbsp;&nbsp;zookeeper:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;addresses:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- id: ${aradin.version.zookeeper-address-id}</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181/chroot</br>
	&nbsp;&nbsp;cache:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;caffeine:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: caffeine #默认caffeine</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">versioned: true</font> #为true时启用cachename级别的版本变更控制，需要搭配**aradin-version**模块使用，**aradin-version-zookeeper-starter**部分提供了配置样例</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults: #默认缓存配置</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 1200000 #访问后过期时间，单位毫秒</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 1800000 #写入后过期时间，单位毫秒</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100 #初始化大小</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 10000 #最大缓存对象个数，超过此数量时之前放入的缓存将失效</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true #是否允许空值</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true #是否启用软引用</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs: #自定义cacheName对应的缓存配置</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base: #具体的cache名,与springcache配合使用</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 3600000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 3600000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 7200000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 7200000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true</br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</p>
<p>&nbsp;② 缓存失效的手动触发</p>
	&nbsp;**IVersionBroadHandler(Bean).broadcast(String group, String key);**</br> 
	&nbsp;group为aradin.cache.caffeine.group，key为cacheName，对应的cache将被清空达到被动更新的目的</br>

***
### 7、aradin-cluster
<p>&nbsp;集群模块，可以借助zookeeper实现集群节点的注册和节点列表的获取</p>

+ **aradin-cluster-core**

+ **aradin-cluster-zookeeper-starter**

***
## JOIN US
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们致力于为Javaer提供更加快捷的项目搭建途径，所以无论对该项目有任何的见解，都欢迎来交流，如果有其他需要补充的功能或者对现有模块的Fix，也欢迎不吝提交你的issue，审核通过即可加入本项目开发 </p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开发者邮箱 liudaax@126.com </p>