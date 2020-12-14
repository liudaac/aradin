package org.aradin.spring.swagger.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aradin.spring.swagger.starter.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Slf4j
@EnableConfigurationProperties({SwaggerProperties.class})
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class AradinSwaggerAutoConfiguration {
	@Autowired
	private SwaggerProperties swaggerProperties;
	
	public AradinSwaggerAutoConfiguration() {
		log.debug("SwaggerAutoConfiguration 初始化");
	}
	
	@Bean
	public Docket api(){
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				//去除默认的状态码
				.useDefaultResponseMessages(swaggerProperties.getUseDefaultStatus())
				//是否启用
				.enable(swaggerProperties.getEnable());
		ApiSelectorBuilder builder = docket.select();
		//api过滤
		builder = builder.apis(Predicates.or(apisFilter()));
		//接口路径过滤
		if (StringUtils.isNotEmpty(swaggerProperties.getAntPath())) {
			builder = builder.paths(PathSelectors.ant(swaggerProperties.getAntPath()));
		}
		return builder.build();
	}
	
    private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerProperties.getTitle())
		        .description(swaggerProperties.getDescription())
		        .version(swaggerProperties.getVersion())
		        .licenseUrl(swaggerProperties.getLicenseUrl())
		        .contact(
		            new Contact(swaggerProperties.getContactName()
		            ,swaggerProperties.getContactUrl()
		            ,swaggerProperties.getContactEmail()))
		        .build();
	}
    
    @SuppressWarnings("deprecation")
	private List<Predicate<RequestHandler>> apisFilter() {
        List<Predicate<RequestHandler>> apis = new ArrayList<>();
        String basePackageStr = swaggerProperties.getBasePackage();
        //包过滤
        if (StringUtils.isNotEmpty(basePackageStr)) {
            //支持多个包
            String[] basePackages = basePackageStr.split(";");
            if (null != basePackages && basePackages.length > 0) {
                Predicate<RequestHandler> predicate = input -> {
                    // 按basePackage过滤
                    Class<?> declaringClass = input.declaringClass();
                    String packageName = declaringClass.getPackage().getName();
                    return Arrays.asList(basePackages).contains(packageName);
                };
                apis.add(predicate);
            }
        }
        return apis;
    }
    
    List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "认证权限");
		return Lists.newArrayList(
				new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
	}
}