package cn.aradin.version.core.gentor;

public class DefaultVersionGentor implements IVersionGentor {

	@Override
	public String nextVersion(String group) {
		// TODO Auto-generated method stub
		return String.valueOf(System.currentTimeMillis());
	}
}
