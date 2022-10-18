package cn.aradin.external.xxljob.starter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import cn.aradin.external.xxljob.starter.properties.XxljobProperties;

@Configuration
@EnableConfigurationProperties({XxljobProperties.class})
public class AradinXxljobAutoConfiguration {
	
	@Bean
	public XxlJobSpringExecutor xxlJobExecutor(XxljobProperties xxljobProperties) {
		XxlJobSpringExecutor springExecutor = new XxlJobSpringExecutor();
		springExecutor.setAdminAddresses(xxljobProperties.getAdmin().getAddresses());
		springExecutor.setAppname(xxljobProperties.getExecutor().getAppname());
		if (StringUtils.isNotBlank(xxljobProperties.getExecutor().getAddress())) {
			springExecutor.setAddress(xxljobProperties.getExecutor().getAddress());
		}else {
			springExecutor.setIp(xxljobProperties.getExecutor().getIp());
			springExecutor.setPort(xxljobProperties.getExecutor().getPort());
		}
		springExecutor.setAccessToken(xxljobProperties.getAccessToken());
		springExecutor.setLogPath(xxljobProperties.getExecutor().getLogpath());
		springExecutor.setLogRetentionDays(xxljobProperties.getExecutor().getLogretentiondays());
		return springExecutor;
	}
}
