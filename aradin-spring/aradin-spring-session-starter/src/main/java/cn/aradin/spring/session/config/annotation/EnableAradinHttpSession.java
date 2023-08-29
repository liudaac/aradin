package cn.aradin.spring.session.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import cn.aradin.spring.session.config.AradinHttpSessionConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AradinHttpSessionConfiguration.class)
@Configuration(proxyBeanMethods = false)
public @interface EnableAradinHttpSession {

}
