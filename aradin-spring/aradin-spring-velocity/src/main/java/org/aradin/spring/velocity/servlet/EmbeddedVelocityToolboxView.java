package org.aradin.spring.velocity.servlet;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.aradin.spring.velocity.tools.ServletToolboxManager;
import org.aradin.spring.velocity.view.VelocityToolboxView;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Extended version of {@link VelocityToolboxView} that can load toolbox locations from
 * the classpath as well as the servlet context. This is useful when running in an
 * embedded web server.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 1.2.5
 */
@SuppressWarnings("deprecation")
public class EmbeddedVelocityToolboxView extends VelocityToolboxView {

	@Override
	protected Context createVelocityContext(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		org.apache.velocity.tools.view.context.ChainedContext context = new org.apache.velocity.tools.view.context.ChainedContext(
				new VelocityContext(model), getVelocityEngine(), request, response,
				getServletContext());
		if (getToolboxConfigLocation() != null) {
			setContextToolbox(context);
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	private void setContextToolbox(
			org.apache.velocity.tools.view.context.ChainedContext context) {
//		org.apache.velocity.tools.view.ToolboxManager toolboxManager = org.apache.velocity.tools.view.servlet.ServletToolboxManager
//				.getInstance(getToolboxConfigFileAwareServletContext(),
//						getToolboxConfigLocation());
		org.apache.velocity.tools.view.ToolboxManager toolboxManager = ServletToolboxManager
				.getInstance(getToolboxConfigFileAwareServletContext(),
						getToolboxConfigLocation());
		Map<String, Object> toolboxContext = toolboxManager.getToolbox(context);
		context.setToolbox(toolboxContext);
	}

	private ServletContext getToolboxConfigFileAwareServletContext() {
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(getServletContext());
		factory.addAdvice(new GetResourceMethodInterceptor(getToolboxConfigLocation()));
		return (ServletContext) factory.getProxy(getClass().getClassLoader());
	}

	/**
	 * {@link MethodInterceptor} to allow the calls to getResourceAsStream() to resolve
	 * the toolboxFile from the classpath.
	 */
	private static class GetResourceMethodInterceptor implements MethodInterceptor {

		private final String toolboxFile;

		GetResourceMethodInterceptor(String toolboxFile) {
			if (toolboxFile != null && !toolboxFile.startsWith("/")) {
				toolboxFile = "/" + toolboxFile;
			}
			this.toolboxFile = toolboxFile;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			if (invocation.getMethod().getName().equals("getResourceAsStream")
					&& invocation.getArguments()[0].equals(this.toolboxFile)) {
				InputStream inputStream = (InputStream) invocation.proceed();
				if (inputStream == null) {
					try {
						inputStream = new ClassPathResource(this.toolboxFile,
								Thread.currentThread().getContextClassLoader())
										.getInputStream();
					}
					catch (Exception ex) {
						// Ignore
					}
				}
				return inputStream;
			}
			return invocation.proceed();
		}

	}

}