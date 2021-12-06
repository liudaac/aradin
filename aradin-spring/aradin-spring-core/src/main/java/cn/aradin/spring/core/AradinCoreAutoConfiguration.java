package cn.aradin.spring.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.spring.core.bean.AradinBeanFactory;
import cn.aradin.spring.core.session.AradinSessionConfiguration;

/**
 * Configuration Door
 *
 */
@Configuration
@Import({AradinBeanFactory.class, AradinSessionConfiguration.class})
public class AradinCoreAutoConfiguration {
}