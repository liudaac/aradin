package cn.aradin.version.core.handler;

public interface IVersionBroadHandler {
	public void broadcast(String key);
	public void broadcast(String key, String version);
}
