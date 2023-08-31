package cn.aradin.version.redis.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.spring.redis.starter.AradinRedisAutoConfiguration;
import cn.aradin.version.core.VersionConfiguration;

@Configuration
@Import(VersionConfiguration.class)
@AutoConfigureAfter({AradinRedisAutoConfiguration.class})
public class VersionRedisAutoConfiguration {
}
