package cn.aradin.cache.redis.starter.core.annotation;

import cn.aradin.cache.redis.starter.core.enums.RedisModel;

public @interface NotSupport {
	RedisModel[] value() default { RedisModel.SINGLE };
}
