package cn.aradin.spring.redisson.starter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.ReflectionUtils;

import cn.aradin.spring.redis.starter.AradinRedisAutoConfiguration;

@Configuration
@EnableCaching
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureAfter(AradinRedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class AradinRedissonAutoConfiguration {
	@Autowired
    private RedisProperties redisProperties;
    
    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    	return new RedissonConnectionFactory(redisson);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean(destroyMethod="shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redisson() throws IOException {
    	Config config = null;
    	Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
    	Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
    	Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
    	int timeout;
    	if (null == timeoutValue) {
			timeout = 0;
		}else if (!(timeoutValue instanceof Integer)) {
			Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
			timeout = ((Long)ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
		}else {
			timeout = (Integer)timeoutValue;
		}
    	if (redisProperties.getSentinel() != null) {
            Method nodesMethod = ReflectionUtils.findMethod(Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());
            
            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = convert(Arrays.asList(((String)nodesValue).split(",")));
            } else {
                nodes = convert((List<String>)nodesValue);
            }
            
            config = new Config();
            config.useSentinelServers()
                .setMasterName(redisProperties.getSentinel().getMaster())
                .addSentinelAddress(nodes)
                .setDatabase(redisProperties.getDatabase())
                .setConnectTimeout(timeout)
                .setPassword(redisProperties.getPassword());
        } else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
            Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
            Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
            List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);
            
            String[] nodes = convert(nodesObject);
            
            config = new Config();
            config.useClusterServers()
                .addNodeAddress(nodes)
                .setConnectTimeout(timeout)
                .setPassword(redisProperties.getPassword());
        } else {
            config = new Config();
            String prefix = "redis://";
            Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
            if (method != null && (Boolean)ReflectionUtils.invokeMethod(method, redisProperties)) {
                prefix = "rediss://";
            }
            
            config.useSingleServer()
                .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setConnectTimeout(timeout)
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword());
        }
        
        return Redisson.create(config);
    }
    
    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                nodes.add("redis://" + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }
}
