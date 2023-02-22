package cn.aradin.spring.core.session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class AradinSessionConfiguration {
	
	@Value("${spring.session.cookie.name:SESSION}")
    private String cookieName;
	
	@Value("${spring.session.cookie.max-age:1800}")
	private Integer maxAge;
	
	@Value("${spring.session.cookie.domain:}")
	private String domain;
	
	@Value("${spring.session.cookie.http-only:true}")
	private Boolean httpOnly;
	
	@Value("${spring.session.cookie.security:true}")
	private Boolean security;
	
	@Value("${spring.session.cookie.same-site:Lax}")
	private String sameSite;
	
	/**
	 * 参考 SpringHttpSessionConfiguration
	 * @return
	 */
	@Bean
	public CookieSerializer createDefaultCookieSerializer() {
		DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
		if (StringUtils.isNotBlank(cookieName)) {
			cookieSerializer.setCookieName(cookieName);
			if (maxAge != null && maxAge > 0) {
				cookieSerializer.setCookieMaxAge(maxAge);
			}
			if (StringUtils.isNotBlank(domain)) {
				cookieSerializer.setDomainName(domain);
			}
			cookieSerializer.setUseHttpOnlyCookie(httpOnly);
			cookieSerializer.setSameSite(sameSite);
			if ("None".equals(sameSite)) {
				cookieSerializer.setUseSecureCookie(true);
			}else {
				cookieSerializer.setUseSecureCookie(security);
			}
		}
		return cookieSerializer;
	}
}
