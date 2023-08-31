package cn.aradin.spring.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Default context impl
 * @author daliu
 *
 */
public class AradinContextImpl implements AradinContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9093095285749631903L;
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	@Override
	public Map<String, Object> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void putData(String key, Object value) {
		// TODO Auto-generated method stub
		data.put(key, value);
	}

	@Override
	public Object getData(String key) {
		// TODO Auto-generated method stub
		return data.get(key);
	}

}
