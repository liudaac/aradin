package cn.aradin.spring.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.spring.core.bean.AradinBeanFactory;

/**
 * Configuration Door
 *
 */
@Configuration
@Import(AradinBeanFactory.class)
public class AradinCoreAutoConfiguration {
}