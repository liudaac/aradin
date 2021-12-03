package cn.aradin.spring.core.context;

import java.io.Serializable;
import java.util.Map;

public interface AradinContext extends Serializable {
	
	Map<String, Object> getData();
	
	Object getData(String key);
	
	void putData(String key, Object value);
}
