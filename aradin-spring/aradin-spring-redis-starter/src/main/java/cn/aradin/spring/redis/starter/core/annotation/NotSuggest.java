package cn.aradin.spring.redis.starter.core.annotation;

import cn.aradin.spring.redis.starter.core.enums.RedisModel;

public @interface NotSuggest {
	RedisModel[] value() default { RedisModel.SINGLE, RedisModel.MASTER_SLAVE, RedisModel.CLUSTER };
}
