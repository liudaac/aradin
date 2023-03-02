package cn.aradin.spring.salarm.starter.notifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.collect.Lists;

import cn.aradin.spring.core.thread.AradinThreadManager;
import cn.aradin.spring.salarm.starter.enums.SalarmDuty;
import cn.aradin.spring.salarm.starter.enums.SalarmLevel;
import cn.aradin.spring.salarm.starter.handler.ISalarmHandler;
import cn.aradin.spring.salarm.starter.properties.SalarmProperties;

public class SalarmImpl implements ISalarm {
	
	private final static Logger log = LoggerFactory.getLogger(SalarmImpl.class);
	
	private SalarmProperties salarmProperties;
	
	private List<ISalarmHandler> handlers = Lists.newArrayList();
	
	private RedisTemplate<Object, Object> redisTemplate;
	
	private final static String FORMAT_STACK = "SALARM Position {} : {} : [ {} ]";
	
	public SalarmImpl(RedisTemplate<Object, Object> redisTemplate,
			SalarmProperties salarmProperties, 
			List<ISalarmHandler> handlers) {
		if (salarmProperties == null 
				|| salarmProperties.getTtl() == null 
				|| salarmProperties.getLevel() == null) {
			throw new RuntimeException("Unconfigured properties with aradin.salarm.*");
		}
		this.salarmProperties = salarmProperties;
		this.redisTemplate = redisTemplate;
		if (CollectionUtils.isNotEmpty(handlers)) {
			this.handlers.addAll(handlers);
		}
	}

	private String format(String format, Object... args) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
        return ft.getMessage();
    }
	
	private void send(SalarmLevel level, String type, String content) {
		for(ISalarmHandler handler:handlers) {
			if (handler.isSupported(level)) {
				handler.notify(level, type, content);
			}
		}
	}
	
	@Override
	public void salarm(SalarmLevel level, SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		if (level != null && salarmProperties.getLevel().compareTo(level) <= 0) {
			CompletableFuture.runAsync(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String type = typeOrFormat;
						if (params != null) {
							type = format(typeOrFormat, params);
						}
						if (redisTemplate.opsForValue().increment(type) > 1) {
							if (log.isDebugEnabled()) {
								log.debug("Repeat Alarm {}", type);
							}
							return;
						}else {
							redisTemplate.expire(type, salarmProperties.getTtl());
						}
						StringBuilder strBud = new StringBuilder();
						if (SalarmDuty.deve.equals(duty)) {
							StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
							if (stacks.length >= 3) {
								StackTraceElement stack = stacks[stacks.length-2];
								strBud.append(format(FORMAT_STACK, stack.getClassName(), stack.getMethodName(), stack.getLineNumber()));
								strBud.append("\n");
							}
						}
						if (params != null) {
							strBud.append(format(contentOrFormat, params));
						}else {
							strBud.append(contentOrFormat);
						}
						send(level, type, strBud.toString());
					} catch (Exception e) {
						// TODO: handle exception
						log.error("Salarm Failed For {} , {}", e.getMessage(), e.getCause());
					}
				}
			}, AradinThreadManager.getShortPool());
		}
	}

	@Override
	public void trace(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		salarm(SalarmLevel.trace, duty, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void info(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		salarm(SalarmLevel.info, duty, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void warn(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		salarm(SalarmLevel.warn, duty, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void error(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		salarm(SalarmLevel.error, duty, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void trace(String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		trace(SalarmDuty.user, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void info(String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		info(SalarmDuty.user, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void warn(String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		warn(SalarmDuty.user, typeOrFormat, contentOrFormat, params);
	}

	@Override
	public void error(String typeOrFormat, String contentOrFormat, Object... params) {
		// TODO Auto-generated method stub
		error(SalarmDuty.user, typeOrFormat, contentOrFormat, params);
	}
}
