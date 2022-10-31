package cn.aradin.examples.boot.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Example
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableDubbo
public class AradinExampleApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AradinExampleApplication.class, args);
	}
}
