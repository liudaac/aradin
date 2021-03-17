package cn.aradin.alibaba.sentinel.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;

import cn.aradin.alibaba.sentinel.starter.block.AradinHttpBlockExceptionHandler;

@Configuration
public class AradinSentinelAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(BlockExceptionHandler.class)
	public BlockExceptionHandler httpBlockExceptionHandler() {
		return new AradinHttpBlockExceptionHandler();
	}
}