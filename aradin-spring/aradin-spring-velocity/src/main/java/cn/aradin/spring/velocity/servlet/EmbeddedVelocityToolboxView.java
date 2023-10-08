package cn.aradin.spring.velocity.servlet;

import java.io.InputStream;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.io.ClassPathResource;

import cn.aradin.spring.velocity.view.VelocityToolboxView;

/**
 * Extended version of {@link VelocityToolboxView} that can load toolbox locations from
 * the classpath as well as the servlet context. This is useful when running in an
 * embedded web server.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @since 1.2.5
 */
public class EmbeddedVelocityToolboxView extends VelocityToolboxView {

	@Override
	protected Context createVelocityContext(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ViewToolContext context = new ViewToolContext(getVelocityEngine(), request, response, getToolboxConfigFileAwareServletContext());
		//IF NEED SET TOOL
		return context;
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