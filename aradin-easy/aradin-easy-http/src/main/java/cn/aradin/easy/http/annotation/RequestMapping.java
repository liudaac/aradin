package cn.aradin.easy.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.aradin.easy.http.support.RequestMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	String value() default "";
	RequestMethod method() default RequestMethod.GET;
	String contentType() default "";
	int retries() default 0;
}
