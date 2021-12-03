package cn.aradin.spring.core.context.strategy;

import org.springframework.util.Assert;

import cn.aradin.spring.core.context.AradinContext;
import cn.aradin.spring.core.context.AradinContextImpl;

public class ThreadLocalAradinContextHolderStrategy implements AradinContextHolderStrategy {

	private static final ThreadLocal<AradinContext> contextHolder = new ThreadLocal<>();
	
	@Override
	public void clearContext() {
		// TODO Auto-generated method stub
		contextHolder.remove();
	}

	@Override
	public AradinContext getContext() {
		// TODO Auto-generated method stub
		AradinContext ctx = contextHolder.get();

		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}

	@Override
	public void setContext(AradinContext context) {
		// TODO Auto-generated method stub
		Assert.notNull(context, "Only non-null AradinContext instances are permitted");
		contextHolder.set(context);
	}

	@Override
	public AradinContext createEmptyContext() {
		// TODO Auto-generated method stub
		return new AradinContextImpl();
	}

}
