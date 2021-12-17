package cn.aradin.spring.core.context.strategy;

import org.springframework.util.Assert;

import cn.aradin.spring.core.context.AradinContext;
import cn.aradin.spring.core.context.AradinContextImpl;

public class GlobalAradinContextHolderStrategy implements AradinContextHolderStrategy {

	private static AradinContext contextHolder;
	
	@Override
	public void clearContext() {
		// TODO Auto-generated method stub
		contextHolder = null;
	}

	@Override
	public AradinContext getContext() {
		// TODO Auto-generated method stub
		if (contextHolder == null) {
			contextHolder = new AradinContextImpl();
		}

		return contextHolder;
	}

	@Override
	public void setContext(AradinContext context) {
		// TODO Auto-generated method stub
		Assert.notNull(context, "Only non-null AradinContext instances are permitted");
		contextHolder = context;
	}

	@Override
	public AradinContext createEmptyContext() {
		// TODO Auto-generated method stub
		return new AradinContextImpl();
	}

}
