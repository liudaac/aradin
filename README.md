# ARADIN
**阿拉丁基础开发框架**
以SpringCloud及<a href="https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E">SpringCloud Alibaba</a>为基础做上层通用功能包扩展，已发布至https://mvnrepository.com/artifact/cn.aradin
  * 规范依赖
  * 降低集成复杂度    
  * 扩充能力以更好的满足线上场景 
  * 解决部分新旧组件交替淘汰时发生的兼容性问题
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;钉群</p>
<img style="width:100px;" src="https://blogsolo.oss-cn-qingdao.aliyuncs.com/592503b6-0f88-4ee3-b003-da5cc7384c1d.jpg"/></p>

***
## 主框架版本说明
### 1.0.2 (发布版，推荐使用)
<p>SpringCloud 2021.0.8</p>
<p>SpringBoot 2.7.15</p>
<p>SpringCloudAlibaba 2021.0.4.0</p>
<p>Dubbo 3.1.11</p>

### 历史版本
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">0.0.3.25(springcloud.Hoxton.SR12+springboot2.3.12.RELEASE+dubbo2.7)</a><br>
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">0.0.3.26(springcloud.Hoxton.SR12+springboot2.3.12.RELEASE+dubbo3.1)</a><br>
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">0.0.4.2(springboot2.7.5+dubbo3.1)</a><br>
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">1.0.0(springboot2.7.10+dubbo3.1)</a><br>
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">1.0.1(springboot2.7.12+dubbo3.1)</a><br>
*RELEASE版* <a href="https://mvnrepository.com/artifact/cn.aradin">1.0.2(springboot2.7.15+dubbo3.1)</a><br>

***
<p>依赖管理</p>
&nbsp;&nbsp;&nbsp;&lt;dependencyManagement&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;dependencies&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;dependency&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;groupId&gt;cn.aradin&lt;/groupId&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;aradin&lt;/artifactId&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;version&gt;${aradin.version}&lt;/version&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;type&gt;pom&lt;/type&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;scope&gt;import&lt;/scope&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/dependency&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/dependencies&gt;<br>
&nbsp;&nbsp;&nbsp;&lt;/dependencyManagement&gt;<br>

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
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@Configuration<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertySource(value = "classpath:config.yml", factory = AradinPropertySourceFactory.class)<br>
<p>&nbsp;&nbsp;&nbsp;方便灵活的布局配置文件</p>
<p>&nbsp;③ cn.aradin.spring.core.context.* 上下文运行时变量 支持三种模式，借鉴SpringSecurity源码中该功能实现方式</p> 
&nbsp;&nbsp;&nbsp;MODE_THREADLOCAL;<br>
&nbsp;&nbsp;&nbsp;MODE_INHERITABLETHREADLOCAL;<br>
&nbsp;&nbsp;&nbsp;MODE_GLOBAL;</p>
&nbsp;默认为MODE_THREADLOCAL当前线程本地变量，支持配置入口 aradin.context.strategy</p>
<p>&nbsp;④ cn.aradin.spring.core.enums.* 常量包，字面理解</p>
<p>&nbsp;⑤ cn.aradin.spring.core.net.http.* HTTP工具包，使用入口 HttpClientUtils</p>
<p>&nbsp;⑥ cn.aradin.spring.core.queue.* 轻量级内存队列，可用于低可靠性要求的使用场景，参考AradinQueue构造方法可以注入生产者及消费者</p>
<p>&nbsp;⑦ cn.aradin.spring.core.thread.* 线程池包，使用入口AradinThreadManager</p>
<p>&nbsp;⑧ cn.aradin.spring.core.session.* Session配置，需要搭配@EnableSpringSession、@EnableRedisHttpSession或者@EnableAradinHttpSession使用，用于替换webserver容器的默认session机制</p>
<p>&nbsp;&nbsp;&nbsp;参考配置</p>
&nbsp;&nbsp;&nbsp;spring:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cookie:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name: ${COOKIE_NAME}<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max-age: 3600<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;http-only: false<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;security: false #若指定domain与当前域名不一致需要设置为true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;same-site: None #若指定domain与当前域名不一致需要设置<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;domain: #若domain与当前域名不一致需要设置<br>

***
+ **aradin-spring-acutator-starter**
<p>&nbsp;心跳组件模块，在spring原生actuator基础上增加inited,offline,online三个服务管理节点</p>
<p>&nbsp;① /inited 查看服务的初始化状态</p>
<p>&nbsp;② /online 持续集成发布时增加上线后的处理逻辑，比如容器应用启动应用后执行指定的脚本文件（如日志采集），也支持扩展Handler实现业务高度定制的启动逻辑</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOnlineHandler 会自动调用应用上下文中所有该类型的BEAN</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此外还支持配置项 aradin.actuator.online.shell 配置启动脚本路径</p>
<p>&nbsp;③ /offline 方便下线时平滑关闭应用</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOfflineHandler 会在自动调用应用上下文所有该类型的BEAN后才去调用Spring上下文的close方法</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平滑下线处理逻辑中包含Kafka消费端注销、Rabbit消费端注销、Dubbo服务端下线、SpringCloud服务下线（以各项只有存在对应的依赖时才会执行对应的下线动作）</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平滑下线增加等待时间配置，即在上述注销动作结束后会Sleep配置的等待时间才会执行context的destroy动作</p>
<p>&nbsp;④ /state 运行状态检查</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;offline调用后会实时变更状态，替换原生的/health</p>
<p>&nbsp;集成方式</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;management.endpoints.web.exposure.include: inited,offline,online,state #开启需要的endpoints
<p>&nbsp;补充配置</p>
&nbsp;&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;acutator:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;online:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;shell: #定制启动脚本<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;offline:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;shut-wait: #优雅停机配置，服务注销和销毁context间的时间间隔，单位ms<br>

***
+ **aradin-spring-velocity-starter**
<p>&nbsp;spring2.x之后不支持velocity的集成，考虑到旧项目模板代码迁移的复杂性，特别提供velocity的兼容包</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;配置方式与原velocity1.x版本一致，例如</p>
&nbsp;spring:<br>
&nbsp;&nbsp;&nbsp;velocity:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true #设定是否允许mvc使用velocity<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache: true #是否开启模板缓存<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;check-template-location: true #是否检查模板路径是否存在<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;prefix: null<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;suffix: .vm #设定velocity模板的后缀<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;date-tool-attribute: null<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;number-tool-attribute: null<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;resource-loader-path: classpath:/template/ #设定模板路径，默认为:classpath:/templates/<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;toolbox-config-location: classpath:/toolbox.xml #设定Velocity Toolbox配置文件的路径，比如 /WEB-INF/toolbox.xml.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;prefer-file-system-access: true #是否优先从文件系统加载模板以支持热加载，默认为true<br>

***
+ **aradin-spring-swagger-starter**
<p>&nbsp;Swagger-bootstrap-ui集成组件，选型的原因是相对原生UI更加友好</p>
<p>&nbsp;① cn.aradin.spring.swagger.starter.dto。Resp 标准返回结构</p>
<p>&nbsp;② 配置方式</p>
&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;swagger:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base-package: pk1;pkg2;pk3<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ant-path: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;title: 协议标题<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;description: 协议描述<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;version: 1.0.0<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enable: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-name: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-email: liudaax@126.com<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;contact-url: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;license:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;license-url: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-default-status: false<br>

***
+ **aradin-spring-caffeine-starter**
<p>&nbsp;使用Caffeineson类包装内存缓存Caffeine，实现对应的CacheManager，支持spring-cache注解方式集成</p>
<p>&nbsp;&nbsp;&nbsp;一般情况下，对于内存缓存的更新机制也分为两种：</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;① 利用Caffeine缓存自身的超时机制进行延时Evict，在触发之前持久层对应的数据可能已经发生变更，</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所以对于更新的数据一定时间内存在不一致情况，此种使用方式更适合对一致性要求不严格或者不可变数据的缓存处理，</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;比如SessionID上的使用可以有效降低Redis调用损耗；题目的缓存；热点商品信息的缓存;</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;② 利用分布式中间件，比如nacos，zookeeper进行变更的通知，以实现各节点内存缓存的同步更新;cachename级别的版本变更控制，需要搭配**aradin-version**模块使用，**aradin-version-zookeeper-starter**和**aradin-version-nacos-starter**均提供了配置样例</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所以该模块对应支持普通模式以及基于版本管理机制的分布式更新模式：</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对应配置如下：</p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;caffeine:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="green">clean-interval: PT20M</font> #1.0.1之后增加定时执行cleanUp逻辑，此处为执行间隔，主要原因是Caffeine的缓存过期逻辑为惰性清理，可能造成内存无法及时释放，线上场景建议按照实际需求进行适配<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults: #默认缓存配置<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 1200000 #访问后过期时间，单位毫秒<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 1800000 #写入后过期时间，单位毫秒<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100 #初始化大小<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 10000 #最大缓存对象个数，超过此数量时之前放入的缓存将失效<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true #是否允许空值<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true #是否启用软引用<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;record-stats: true #是否开启状态统计<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs: #自定义cacheName对应的缓存配置<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base: #具体的cache名,与springcache配合使用<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 3600000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 3600000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;soft: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;record-stats: true #是否开启状态统计<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 7200000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 7200000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;soft: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;record-stats: true #是否开启状态统计，可以配合management.endpoints.web.exposure.include: caffeineson 来使用</p>
<p>&nbsp;&nbsp;&nbsp;使用方式如下几种： </p>
<p>&nbsp;&nbsp;&nbsp;① 直接引用Caffeineson Bean，配置参考aradin.cache.caffeine.defaults</p>
<p>&nbsp;&nbsp;&nbsp;② 引用CaffeinesonCacheManager Bean 按CacheName获取Caffeineson实例</p>
<p>&nbsp;&nbsp;&nbsp;③ 搭配@EnableCache注解，使用方式@Cachable(cacheManager=CaffeinesonConfiguration.CACHE_MANAGER)</p>
<p>&nbsp;&nbsp;&nbsp;④ 提供Endpoint入口查询Caffeine状态 caffeineson，参考record-stats配置说明，可以按照aradin-spring-acutator-starter的配置方式进行开放</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监控项参考 https://github.com/liudaac/aradin/blob/main/aradin-spring/aradin-spring-caffeine/src/main/java/cn/aradin/spring/caffeine/manager/stats/CaffeinesonStatsService.java</p>
<p>&nbsp;&nbsp;&nbsp;注：各配置项的设置可以参考https://github.com/ben-manes/caffeine/wiki/Memory-overhead-zh-CN中的内存占用指标</p>

***
+ **aradin-spring-redis-starter**
<p>这个模块目前实现了两个重点功能：</p>
<p>&nbsp;一、针对于spring-cache注解的使用优化</p>
<p>&nbsp;替换掉了spring-boot-starter-data-redis中默认CacheManager实现（由于原生实现不支持多种缓存方式共存），对于各CacheName的初始化使用自定义配置项<br>
&nbsp;配置方式如下<br>
&nbsp;&nbsp;首先是原生配置<br>
&nbsp;&nbsp;&nbsp;spring:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redis:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;database: 0 #Redis数据库索引（默认为0）<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;host:  #Redis服务器地址<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;port: 6379 #Redis服务器连接端口<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password:  #Redis服务器连接密码（默认为空）<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;pool: #连接池配置，不再详细列出</p>
&nbsp;&nbsp;然后是通过自定义配置来定制各个cachename的属性</p>
&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;cache:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redis:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT60M #https://blog.csdn.net/huang007guo/article/details/81625061<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: default_<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT12H<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: base_<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;client:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT4H<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cache-null-values: true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key-prefix: client_<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;use-key-prefix: true</p>

<p>&nbsp;二、针对于redis大集合分桶操作的支持(v1.0.2+)</p>
<p>&nbsp;&nbsp;面向实际业务场景时，随着数据量的增加，对于hashmap和set的操作往往伴随着大key问题，这时候常用的策略都是对数据进行分桶，但目前spring针对于redis的实现并未支持，需要集成方在上层手动进行，并不方便。<br>
<br>
&nbsp;&nbsp;该模块做的，就是提出了RedisBucketTemplate的概念，并提供了BucketHashOperations，BucketSetOperations（单机模式），ClusterBucketSetOperations（集群模式）操作类，来统一托管分桶逻辑以及桶聚合操作。<br>
<br>
&nbsp;&nbsp;对于Set的操作类之所以提供了两个Operations，是由于set提供的api存在跨Key操作，单机可以完全在redis中进行，而集群模式下由于key的分布在不同的slot中，跨key操作的支持需要转到内存中进行。<br>
</p>

***
+ **aradin-spring-redisson-starter**
<p>&nbsp;RedissonClient实例初始化，配置方式与spring-redis配置一致，无需额外配置项</p>

***
+ **aradin-spring-session-starter(v1.0.2+)**
<p>&nbsp;spring-session-data-redis的实现依赖spring.redis.*配置，这会造成session用的redis和业务redis高度绑定，在实际使用场景中，存在多服务共享session但不愿共享redis的场景。spring-session-data-redis中RedisConnectFactory引入方式虽然提供了@SpringSessionRedisConnectionFactory扩展槽来定制redis，但会造成默认RedisConnectFactory实例不再初始化，所以并不实用。</p>
<p>&nbsp;该模块实现了对默认redissession实现的替换，使用aradin.session.redis.*来定制session用的redis实例，同时使用@EnableAradinHttpSession注解实现对@EnableRedisHttpSession注解的替换。</p>
<p>&nbsp;配置方式如下</p>
&nbsp;&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;redis:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;database: 0 #Redis数据库索引（默认为0）<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;host:  #Redis服务器地址<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;port: 6379 #Redis服务器连接端口<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password:  #Redis服务器连接密码（默认为空）<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;pool: #连接池配置，不再详细列出</p>

***
+ **aradin-spring-salarm-starter**
<p>&nbsp;告警通知模块，类似log4j的API，实现Content的组装和发送，发送目标默认只提供控制台输出</p>
<p>&nbsp;&nbsp;&nbsp;① cn.aradin.spring.salarm.starter.handler.ISalarmHandler 实现该接口Bean实例，自定义各个通知渠道逻辑</p>
<p>&nbsp;&nbsp;&nbsp;② cn.aradin.spring.salarm.starter.notifier.ISalarm 调用入口，负责告警消息的拼装，去重以及全局ISalarmHandler的分发，引用方式为Bean引用</p>
<p>&nbsp;&nbsp;&nbsp;③ 配置项</p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;salarm:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ttl: PT1H # Min interval for sending a same alarm<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;level: warn # Min level to send<br>

***
+ **aradin-spring-xxljob-starter**
<p>&nbsp;Xxljob自动配置，配置项与官方建议一致</p>
<p>&nbsp;&nbsp;&nbsp;配置项</p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xxl:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;job:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;admin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;addresses:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;access-token:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;executor:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;appname:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ip:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;port:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;logpath:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;logretentiondays:<br>

***
### 2、aradin-alibaba
合并了必要的依赖项和配置样例，并未做额外开发
+ **aradin-alibaba-nacos-starter**

+ **aradin-alibaba-sentinel-starter**

***
### 3、aradin-mybatis
+ **aradin-mybatis-plus-starter**
<p>&nbsp;增加了开启分页的配置，由于默认情况下Page查询是必要的，无需下沉到项目实现中再开启</p>

***
### 4、aradin-lucene
+ **aradin-lucene-solr-starter**
<p>&nbsp;使用自定义配置初始化CloudSolrClient Bean</p>
&nbsp;配置如下</p>
&nbsp;&nbsp;&nbsp;spring:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;solr:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zk-hosts:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.1:2181<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.2:2181<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- 192.168.1.3:2181<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;chroot: /solrcloud</p>

***
### 5、aradin-zookeeper
+ **aradin-zookeeper-boot-starter**
<p>&nbsp;可同时配置多个ZK集群，并绑定事件路由机制</p>
<p>&nbsp;&nbsp;支持的使用方式如下</p>
<p>&nbsp;&nbsp;① 事件接收方式 实现cn.aradin.zookeeper.boot.starter.handler.INodeHandler Bean实例 通过support方法进行事件过滤，handler方法实现事件的处理</p>
<p>&nbsp;&nbsp;② ZK集群配置方式</p>
&nbsp;&nbsp;&nbsp;&nbsp;aradin:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zookeeper:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;addresses: #支持多组<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;id: CUSTOMIZED-ZOOKEEPER-ADDRESS-ID<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181/CHROOT</p>
<p>&nbsp;&nbsp;③ ZKClient获取方式 ZookeeperClientManager.getClient(String id)</p>

***
### 6、aradin-version
版本分发引擎，建立标准的版本控制分发机制，并支持选择性集成zookeeper，nacos中间件
+ **aradin-version-core**
<p>&nbsp;版本分发通用模块，对版本分发的上层逻辑进行抽象，并提供分布式一致性中间件的扩展入口，目前支持zookeeper、nacos</p>
  <figure>
  <p>① cn.aradin.version.core.dispatcher.VersionDispatcher 唤起所有的IVersionHandler(Bean)进行版本变更的发布</p>
  <p>② cn.aradin.version.core.gentor.IVersionGentor 新版本号生成器，提供默认实现</p>
  <p>③ cn.aradin.version.core.handler.IVersionBroadHandler 版本发布逻辑，默认实现只打印至控制台，需要根据不同中间件对应不同具体实现</p>
   <figure>
     aradin-version-zookeeper-starter.VersionZookeeperBroadHandler<br>
     aradin-version-nacos-starter.VersionNacosBroadHandler
   </figure>
  <p>④ cn.aradin.version.core.properties.VersionProperties 相关配置项 **aradin.version**<br></p>
  
***
+ **aradin-version-zookeeper-starter**
<p>
<figure>
     ① cn.aradin.version.zookeeper.starter.handler.VersionsNodeHandler<br> 
     *接收ZK事件并使用VersionDispatcher(Bean)进行分发，接收方为所有的cn.aradin.version.core.handler.IVersionHandler(Bean)*<br>
     <br>
     ② cn.aradin.version.zookeeper.starter.handler.VersionZookeeperBroadHandler<br>
     *ZK的版本广播触发入口，方便人工触发版本变更事件*<br>
     <br>
     ③ 配置样例<br>
     aradin:<br>
	 &nbsp;&nbsp;version:<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;zookeeper:<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address-id: CUSTOMIZED-ID<br>
	 &nbsp;&nbsp;zookeeper:<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;enable: true #default true<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;session-timeout: 5000 #default 5000<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;connection-timeout: 5000 #default 5000<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;addresses:<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- id: CUSTOMIZED-ID<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address:
</figure>
</p>

***
+ **aradin-version-nacos-starter(v1.0.0+)**
<p>
<figure>
	<p>
	① cn.aradin.version.nacos.starter.listener.VersionNacosConfigListener<br> 
	*接收Nacos事件，并使用VersionDispatcher(Bean)进行分发，接收方为所有的cn.aradin.version.core.handler.IVersionHandler(Bean)*<br>
	<br>
	② cn.aradin.version.nacos.starter.handler.VersionNacosBroadHandler<br>
	*Nacos的版本广播触发入口，方便人工触发版本变更事件，另外初始化时同时为指定的group data-id绑定listener*<br>
	<br>
	③ 配置样例</p>
	aradin:<br>
	&nbsp;&nbsp;version:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;server-addr: #若不配置与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: #若不配置与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: #若不配置与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;namespace: #若不配置与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: #必填<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data-ids: #必填，data-id列表<br>
	spring:<br>
	&nbsp;&nbsp;cloud:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;config:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;server-addr: 192.168.1.1:8888,192.168.1.2:8888,192.168.1.3:8888<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;namespace: d78b658c-182a-420a-9005-e8e8f36a1e7d<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group-id: aradin.version.nacos.group<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data-id: aradin.version.nacos.data-id<br>
</figure>
</p>

***
+ **aradin-version-caffeine-starter整合aradin-version-zookeeper-starter实现分布式内存缓存**
<p>&nbsp;aradin-version-caffeine-starter中实现了位于VersionDispatcher(Bean)下游的IVersionHandler（cn.aradin.spring.caffeine.manager.version.CaffeinesonVersionHandler）实现内存信息的版本淘汰机制<br>
<p>&nbsp;① 相关配置如下：可以参考复用至nacos集成</p>
	aradin:<br>
	&nbsp;&nbsp;version:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;zookeeper:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address-id: CUSTOMID<br>
	&nbsp;&nbsp;zookeeper:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;addresses:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- id: ${aradin.version.zookeeper-address-id}<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181/chroot<br>
	&nbsp;&nbsp;cache:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;caffeine:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults: #默认缓存配置<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 1200000 #访问后过期时间，单位毫秒<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 1800000 #写入后过期时间，单位毫秒<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100 #初始化大小<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 10000 #最大缓存对象个数，超过此数量时之前放入的缓存将失效<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true #是否允许空值<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true #是否启用软引用<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs: #自定义cacheName对应的缓存配置<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base: #具体的cache名,与springcache配合使用<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 3600000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 3600000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;versioned: true #标识当前cache是否开启分布式更新，默认为false<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 7200000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 7200000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</p>
<p>&nbsp;② 缓存失效的手动触发</p>
	&nbsp;**IVersionBroadHandler(Bean).broadcast(String group, String key);**<br> 
	&nbsp;group为aradin.cache.caffeine.group，key为cacheName，对应的cache将被清空达到被动更新的目的<br>

***
+ **aradin-version-caffeine-starter整合aradin-version-nacos-starter实现分布式内存缓存**
<p>&nbsp;aradin-version-caffeine-starter中实现了位于VersionDispatcher(Bean)下游的IVersionHandler（cn.aradin.spring.caffeine.manager.version.CaffeinesonVersionHandler）实现内存信息的版本淘汰机制<br>
<p>&nbsp;① 相关配置如下：可以参考复用至nacos集成</p>
	aradin:<br>
	&nbsp;&nbsp;version:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: #选填，默认与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: #选填，默认与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;server-addr: #选填，默认与spring.cloud.nacos一致<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;namespace: #选填，建议与项目配置做隔离<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: #必填<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;data-ids: #必填，data-id列表，需要管理的cacheName加进来即可<br>
	&nbsp;&nbsp;cache:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;caffeine:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaults: #默认缓存配置<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 1200000 #访问后过期时间，单位毫秒<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 1800000 #写入后过期时间，单位毫秒<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100 #初始化大小<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 10000 #最大缓存对象个数，超过此数量时之前放入的缓存将失效<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true #是否允许空值<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true #是否启用软引用<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;configs: #自定义cacheName对应的缓存配置<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;base: #具体的cache名,与springcache配合使用<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 3600000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 3600000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;versioned: true #标识当前cache是否开启分布式更新，默认为false<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;session:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-access: 7200000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;expire-after-write: 7200000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;initial-capacity: 100<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maximum-size: 100000<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;allow-null-values: true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is-soft: true</p>
<p>&nbsp;② 缓存失效的手动触发</p>
	&nbsp;**IVersionBroadHandler(Bean).broadcast(String group, String key);**<br> 
	&nbsp;group为aradin.cache.caffeine.group，key为cacheName，对应的cache将被清空达到被动更新的目的<br>

***
### 7、aradin-cluster

+ **aradin-cluster-core**
<p>&nbsp;集群模块，可以借助zookeeper，nacos实现集群节点的注册和节点列表的获取，该模块通过IClusterNodeManager对集群信息进行托管，依赖zk或nacos模块实现集群信息的更新。此外，该模块还提供了集群的通用注册配置</p>
<p>&nbsp;① 相关配置如下：</p>
	aradin:<br>
	&nbsp;&nbsp;cluster:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;node-name: #节点注册名<br>
	&nbsp;&nbsp;&nbsp;&nbsp;register: true #当前节点是否注册到集群中，默认为true<br>
	&nbsp;&nbsp;&nbsp;&nbsp;prefer-ip-address: #节点注册名是否偏向于ip地址，当node-name不指定时，可以生成默认名<br>
	&nbsp;&nbsp;&nbsp;&nbsp;max-node: #当前集群支持的最大节点数<br>

+ **aradin-cluster-zookeeper-starter**
<p>&nbsp;基于ZK实现集群节点的注册和同步</p>
<p>&nbsp;① 相关配置如下：</p>
	aradin:<br>
	&nbsp;&nbsp;cluster:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;zookeeper: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;address-id: #和aradin-zookeeper-boot-starter的配置关联<br>

+ **aradin-cluster-nacos-starter**
<p>&nbsp;基于nacos实现集群节点的注册和同步，原理是借助cluster独占的方式来确定各节点的序号，注意的是发布建议逐节点滚动发布，原因是目前nacos注册+注销操作频繁可能会有数据一致性问题，</p>
<p>&nbsp;① 相关配置如下：</p>
	aradin:<br>
	&nbsp;&nbsp;cluster:<br>
	&nbsp;&nbsp;&nbsp;&nbsp;nacos: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;username: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;password: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;server-addr: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;namespace: #建议和服务注册发现的命名空间分离，防止有干扰<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;group: <br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;service-name: <br>

***
### 8、aradin-external
<p>&nbsp;外部模块重写，目前包含springfox-swagger，原因是swagger近两年未做更新，与spring新版有兼容问题，所以做了本地化适配修改，0.0.4.x以后，兼容springboot2.7.X</p>

***
+ **springfox-swagger**
<p>&nbsp;swagger近两年未做更新，与spring新版有兼容问题，所以做了本地化适配修改，0.0.4.x+版本兼容springboot2.7.x，可直接引用aradin-spring-swagger-starter</p>

***
+ **xxl-job-core**
<p>&nbsp;原版core包在log4j和spring-context存在强依赖，导致jar包出现包冲突，本地化适配修改，可直接引用aradin-spring-xxljob-starter</p>

***
### 9、aradin-easy
<p>&nbsp;方便客户端调用方的构造，目前先对Http各种请求模式进行了支持，</p>

***
+ **aradin-easy-http**
<p>&nbsp;参考springmvc注解方式提供Http接口类的注解及执行代理，同时做到对spring无依赖，新旧项目均能接入，基于jdk动态代理实现对Http接口类的代理</p>

***
+ **aradin-easy-http-buddy**
<p>&nbsp;对aradin-easy-http中代理实现的构造进行了重写，基于ByteBuddy实现对Http接口类的代理</p>
<p>接口类可以参考 https://github.com/liudaac/aradin/blob/main/aradin-easy/aradin-easy-http-buddy/src/test/java/cn/aradin/easy/http/compare/result/NcClient.java </p>
<p>代理类构造，使用工厂模式 EasyBuilder.ins().service(NcClient.class)</p>
<p>请求方法支持GET、POST，请求体支持QueryParam, Formdata, JSON, String，同时支持加密方法的注册，实现传参前的编码</p>

<p>在Http接口类的代理构造上，对jdk动态代理和bytebuddy字节码增强进行了对比，参考https://github.com/liudaac/aradin/issues/6</p>

***
## 进展阶段
<p>&nbsp;目前迭代至1.0.2版本，经过大量的线上场景验证和磨合，已基本满足日常项目快速搭建需求，且已经普遍运行于线上环境。</p>
<p>&nbsp;JVM兼容jdk8至17，当前支持springboot2.7.15，后面等spring官方2023年11月停止2.7.x维护后，aradin框架将升级至1.1.x并开始支持springboot3.x，保持跟进SpringCloud及Alibaba全家桶的生态升级</p>

***
## JOIN US
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们致力于为Javaer提供更加快捷的项目搭建途径，所以无论对该项目有任何的见解，都欢迎来交流，如果有其他需要补充的功能或者对现有模块的Fix，也欢迎不吝提交你的issue，审核通过即可加入本项目开发 </p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开发者邮箱 liudaax@126.com </p>
