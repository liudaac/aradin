package cn.aradin.version.core.handler;

public interface IVersionBroadHandler {
	public void broadcast(String group, String key);
	public void broadcast(String group, String key, String version);
}
