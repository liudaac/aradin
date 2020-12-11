package org.aradin.spring.core;

import org.aradin.spring.core.bean.AradinBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration Door
 *
 */
@Configuration
@Import(AradinBeanFactory.class)
public class AradinCoreAutoConfiguration {
}