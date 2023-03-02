package cn.aradin.spring.swagger.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.swagger")
public class SwaggerProperties {
	private String basePackage;//多个用;分隔
	private String antPath;//Path过滤，可以做版本号控制
    private String title = "HTTP API";
    private String description = "Swagger 自动生成接口文档";
    private String version;
    private Boolean enable = false;
    private String contactName;
    private String contactEmail;
    private String contactUrl;
    private String license;
    private String licenseUrl;
    private Boolean useDefaultStatus = true;//是否使用HTTP默认状态值
	public String getBasePackage() {
		return basePackage;
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	public String getAntPath() {
		return antPath;
	}
	public void setAntPath(String antPath) {
		this.antPath = antPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactUrl() {
		return contactUrl;
	}
	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getLicenseUrl() {
		return licenseUrl;
	}
	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}
	public Boolean getUseDefaultStatus() {
		return useDefaultStatus;
	}
	public void setUseDefaultStatus(Boolean useDefaultStatus) {
		this.useDefaultStatus = useDefaultStatus;
	}
}
