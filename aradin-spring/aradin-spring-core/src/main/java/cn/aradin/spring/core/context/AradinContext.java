package cn.aradin.spring.core.context;

import java.io.Serializable;
import java.util.Map;

/**
 * Aradin context interface
 * 
 * @author daliu
 *
 */
public interface AradinContext extends Serializable {
	
	/**
	 * Get params
	 * @return params
	 */
	Map<String, Object> getData();
	
	/**
	 * Get target key value
	 * @param key just key
	 * @return value
	 */
	Object getData(String key);
	
	/**
	 * Set key-value
	 * @param key key
	 * @param value value
	 */
	void putData(String key, Object value);
}
