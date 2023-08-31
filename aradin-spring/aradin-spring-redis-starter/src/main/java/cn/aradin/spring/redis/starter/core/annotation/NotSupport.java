package cn.aradin.spring.redis.starter.core.annotation;

import cn.aradin.spring.redis.starter.core.enums.RedisModel;

public @interface NotSupport {
	RedisModel[] value() default { RedisModel.SINGLE };
}
