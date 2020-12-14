package org.aradin.spring.webwall.starter;

import org.aradin.spring.webwall.starter.xss.editor.XssStringEditor;
import org.aradin.spring.webwall.starter.xss.filter.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
@Import(XssFilter.class)
public class AradinWebwallAutoConfiguration {
	@Autowired
	public void setWebBindingInitializer(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
		requestMappingHandlerAdapter.setWebBindingInitializer(new XssStringEditor());
	}
}