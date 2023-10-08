package cn.aradin.spring.core.rowmap;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class Row extends HashMap<Object, Object> {

	protected List<Object> ordering = Lists.newArrayList(); //

	protected Map<String, String> functionMap = null; //

	@SuppressWarnings("rawtypes")
	private final Map<Object, List> _fields = new HashMap<Object, List>();

	public Row() {
	}

	public Row(Map<?, ?> map) {
		super(map);
		for (Object obj : map.keySet()) {
			ordering.add(obj);
		}
	}

	public String gets(Object name) {
		try {
			if (get(name) != null)
				return get(name).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String gets(Object name, String defaultValue) {
		try {
			if (get(name) != null)
				return get(name).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	public String gets(Object name, String enc, String defaultValue) {
		try {
			if (get(name) != null)
				return URLDecoder.decode(get(name).toString(), enc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	public Integer getInt(Object name) {
		Object o = get(name);
		if (o != null) {
			try {
				return Integer.parseInt(o.toString());
			} catch (Exception e) {
			}
		}
		return null;
	}

	public int getInt(Object name, int defaultValue) {
		Object o = get(name);
		if (o != null) {
			try {
				return Integer.parseInt(o.toString());
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	public boolean getBoolean(Object name, boolean defaultValue) {
		Object o = get(name);
		if (o != null) {
			try {
				return Boolean.parseBoolean(o.toString());
			} catch (Exception e) {

			}
		}
		return defaultValue;
	}

	public int getInt(int which, int defaultValue) {
		Object key = ordering.get(which);
		return getInt(key, defaultValue);
	}

	public float getFloat(Object name) {
		return Float.valueOf(get(name).toString()).floatValue();
	}

	public float getFloat(Object name, float defaultValue) {
		Object o = get(name);
		if (o != null)
			try {
				return Float.valueOf(o.toString()).floatValue();
			} catch (Exception e) {
			}
		return defaultValue;
	}

	public float getFloat(int which, float defaultValue) {
		Object key = ordering.get(which);
		return getFloat(key, defaultValue);
	}

	public Long getLong(Object name) {
		Object o = get(name);
		if (o != null)
			try {
				return Long.valueOf(o.toString()).longValue();
			} catch (Exception e) {
			}
		return null;
	}

	public long getLong(Object name, long defaultValue) {
		Object o = get(name);
		if (o != null)
			try {
				return Long.valueOf(o.toString()).longValue();
			} catch (Exception e) {
			}
		return defaultValue;
	}

	public Object get(int which) {
		Object key = ordering.get(which);
		return get(key);
	}

	public Object getKey(int which) {
		Object key = ordering.get(which);
		return key;
	}

	public String[] getKeys() {
		Set<Object> keys = this.keySet();
		Iterator<Object> iter = keys.iterator();
		String[] strs = new String[keys.size()];
		int i = 0;
		while (iter.hasNext()) {
			strs[i] = iter.next().toString();
			i++;
		}
		return strs;
	}

	public void dump() {
		for (Iterator<?> e = keySet().iterator(); e.hasNext();) {
			String name = (String) e.next();
			Object value = get(name);
			System.out.println(name + "=" + value + ", ");
		}
	}

	public String dumpToString() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<?> e = keySet().iterator(); e.hasNext();) {
			String name = (String) e.next();
			Object value = get(name);
			sb.append(value).append(",");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object name, Object value) {
		if (!containsKey(name)) {
			ordering.add(name); // ���������
		}
		super.put(name, value);
		if (functionMap != null && functionMap.containsKey(name))
			functionMap.remove(name);
		return value;
	}

	public int putInt(Object name, int value) {
		super.put(name, Integer.valueOf(value));
		return value;
	}

	public float putFloat(Object name, float value) {
		super.put(name, Float.valueOf(value));
		return value;
	}

	public String putFunction(String name, String value) {
		if (functionMap == null)
			functionMap = new HashMap<String, String>();
		if (this != null && this.containsKey(name)) {
			// ordering.remove(name);
			remove(name);
		}
		functionMap.put(name, value);
		return value;
	}

	public String getFunction(String name) {
		return functionMap.get(name);
	}

	public Map<String, String> getFunctionMap() {
		return this.functionMap;
	}

	public void setFunctionMap(HashMap<String, String> fmap) {
		if (fmap != null && fmap.size() > 0) {
			if (functionMap == null)
				functionMap = new HashMap<String, String>();
			this.functionMap.putAll(fmap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<?, ?> otherMap) {
		Set<?> keySet = otherMap.keySet();
		for (Object name : keySet)
			ordering.add(name);
		super.putAll(otherMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object name) {
		if (ordering.remove(name))
			return super.remove(name);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		super.clear();
		ordering.clear();
	}

	public int length() {
		return size();
	}

	@SuppressWarnings("unchecked")
	public void append(Object key, Object value) {
		if (!containsKey(key)) {
			ordering.add(key);
			super.put(key, value);
			List<Object> list = new ArrayList<Object>();
			list.add(value);
			_fields.put(key, list);
		} else {
			_fields.get(key).add(value);
			super.put(key, _fields.get(key));
		}
	}

	public List<?> getList(Object key) {
		Object obj = get(key);
		if (obj != null) {
			return (List<?>) obj;
		} else {
			return null;
		}
	}
}
