package cn.aradin.spring.lts.starter;

import org.springframework.context.annotation.Configuration;

import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;

@Configuration
@EnableTaskTracker
@EnableJobClient
public class AradinLtsAutoConfiguration {
	
}
