package cn.aradin.easy.http.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Function;

@Retention(RUNTIME)
@Target(PARAMETER)
@Documented
public @interface RequestBody {
	Class<? extends Function<String, String>> encrypt() default NoEncrypt.class;
}
