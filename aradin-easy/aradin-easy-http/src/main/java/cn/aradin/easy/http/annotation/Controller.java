package cn.aradin.easy.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
	String value() default "";
	String param() default "";//-D支持
	int connectTimeoutMillis() default 1000*3;
    int readTimeoutMillis() default 1000*5;
}
