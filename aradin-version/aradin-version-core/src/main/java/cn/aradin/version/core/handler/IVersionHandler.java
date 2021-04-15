package cn.aradin.version.core.handler;

public interface IVersionHandler {
	
	public String get(String group, String key);
	
	public boolean support(String group, String key);
	
	public void version(String group, String key, String version);
	
	public void changed(String group, String key, String version);
}
