package cn.aradin.external.xxljob.starter.properties;

public class XxljobExecutor {

	private String appname;//执行器心跳注册分组依据；为空则关闭自动注册
	private String address;//优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址
	private String ip;//默认为空表示自动获取IP，多网卡时可手动设置指定IP
	private Integer port;//小于等于0则自动获取；默认端口为9999
	private String logpath;//执行器运行日志文件存储磁盘路径
	private Integer logretentiondays;//执行器日志文件保存天数
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLogpath() {
		return logpath;
	}
	public void setLogpath(String logpath) {
		this.logpath = logpath;
	}
	public Integer getLogretentiondays() {
		return logretentiondays;
	}
	public void setLogretentiondays(Integer logretentiondays) {
		this.logretentiondays = logretentiondays;
	}
}
