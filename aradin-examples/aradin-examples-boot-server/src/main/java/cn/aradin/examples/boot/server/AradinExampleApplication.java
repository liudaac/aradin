package cn.aradin.examples.boot.server;

import java.util.Arrays;

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
		args = Arrays.copyOf(args, args.length + 1);
		args[args.length - 1] = "--spring.cloud.bootstrap.enabled=true";
		SpringApplication.run(AradinExampleApplication.class, args);
	}
}
