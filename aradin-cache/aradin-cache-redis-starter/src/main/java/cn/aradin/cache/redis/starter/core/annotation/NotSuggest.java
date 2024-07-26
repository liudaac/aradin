package cn.aradin.cache.redis.starter.core.annotation;

import cn.aradin.cache.redis.starter.core.enums.RedisModel;

public @interface NotSuggest {
	RedisModel[] value() default { RedisModel.SINGLE, RedisModel.MASTER_SLAVE, RedisModel.CLUSTER };
}
