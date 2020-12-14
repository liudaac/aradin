package org.aradin.spring.swagger.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "aradin.swagger")
@Data
public class SwaggerProperties {
	private String basePackage;//多个用;分隔
	private String antPath;//Path过滤，可以做版本号控制
    private String title = "HTTP API";
    private String description = "Swagger 自动生成接口文档";
    private String version;
    private Boolean enable;
    private Boolean auth;
    private String contactName;
    private String contactEmail;
    private String contactUrl;
    private String license;
    private String licenseUrl;
    private Boolean useDefaultStatus = true;
}
