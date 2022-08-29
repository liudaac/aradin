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

***
+ **aradin-spring-core**
<p>&nbsp;基础能力模块</p>
<p>&nbsp;1.1 cn.aradin.spring.core.algo.* 算法包   目前只有SWRR负载均衡</p>
<p>&nbsp;1.2 cn.aradin.spring.core.bean.* BEAN工具  
<p>&nbsp;&nbsp;&nbsp;1.2.1 AradinBeanFactory BEAN工厂类支持对指定Class通过Prefix命名方式进行路由，常用于某个类需要按规则初始化数量大于1的有限个BEAN，比如分库逻辑，读写分离逻辑；</p>
<p>&nbsp;&nbsp;&nbsp;1.2.2 AradinPropertySourceFactory 支持对yml配置文件的加载，使用方式 </p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@Configuration</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertySource(value = "classpath:config.yml", factory = AradinPropertySourceFactory.class)</p>
<p>&nbsp;&nbsp;&nbsp;方便灵活的布局配置文件</p>
<p>&nbsp;1.3 cn.aradin.spring.core.context.* 上下文运行时变量 支持三种模式，参考SpringSecurity源码中该功能实现方式</p> 
&nbsp;&nbsp;&nbsp;MODE_THREADLOCAL;</br>
&nbsp;&nbsp;&nbsp;MODE_INHERITABLETHREADLOCAL;</br>
&nbsp;&nbsp;&nbsp;MODE_GLOBAL;</p>
&nbsp;默认为MODE_THREADLOCAL当前线程本地变量，支持配置入口 aradin.context.strategy</p>
&nbsp;1.4 cn.aradin.spring.core.enums.* 常量包，字面理解</p>
&nbsp;1.5 cn.aradin.spring.core.net.http.* HTTP工具包，使用入口 HttpClientUtils</p>
&nbsp;1.6 cn.aradin.spring.core.queue.* 轻量级内存队列，可用于低可靠性要求的使用场景，参考AradinQueue构造方法可以注入生产者及消费者</p>
&nbsp;1.7 cn.aradin.spring.core.thread.* 线程池包，使用入口AradinThreadManager</p>
&nbsp;1.8 cn.aradin.spring.core.session.* Session配置，需要搭配@EnableSpringSession或者@EnableRedisHttpSession使用，用于替换webserver容器的默认session机制</p>
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
<p>&nbsp;1.1 /inited 查看服务的初始化状态</p>
<p>&nbsp;1.2 /online 持续集成发布时增加上线后的处理逻辑，比如容器应用启动应用后执行指定的脚本文件（如日志采集），也支持扩展Handler实现业务高度定制的启动逻辑</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOnlineHandler 会自动调用应用上下文中所有该类型的BEAN</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此外还支持配置项 aradin.actuator.online.shell 配置启动脚本路径</p>
<p>&nbsp;1.3 /offline 方便下线时平滑关闭应用</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cn.aradin.spring.actuator.starter.extension.IOfflineHandler 会在自动调用应用上下文所有该类型的BEAN后才去调用Spring上下文的close方法</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;集成方式</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;management.endpoints.web.exposure.include: inited,offline,online #开启需要的endpoints

***
+ **aradin-spring-velocity-starter**
<p>&nbsp;spring2.x以后不再支持velocity的集成，考虑到旧项目模板代码迁移的复杂性，特别提供velocity的兼容包</p>
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


## JOIN US
