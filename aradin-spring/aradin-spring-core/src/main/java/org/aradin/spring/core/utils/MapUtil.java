package org.aradin.spring.core.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
	
	/**
	 * Map key 排序
	 * @param map
	 * @return
	 */
	public static Map<String,String> order(Map<String, String> map){
		HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(	map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});

		for (int i = 0; i < infoIds.size(); i++) {
			Map.Entry<String, String> item = infoIds.get(i);
			tempMap.put(item.getKey(), item.getValue());
		}
		return tempMap;
	}


	/**
	 * 转换对象为map
	 * @param object
	 * @param ignore
	 * @return
	 */
	public static Map<String,String> objectToMap(Object object,String... ignore){
		Map<String,String> tempMap = new LinkedHashMap<String, String>();
		for(Field f : object.getClass().getDeclaredFields()){
			if(!f.isAccessible()){
				f.setAccessible(true);
			}
			boolean ig = false;
			if(ignore!=null&&ignore.length>0){
				for(String i : ignore){
					if(i.equals(f.getName())){
						ig = true;
						break;
					}
				}
			}
			if(ig){
				continue;
			}else{
				Object o = null;
				try {
					o = f.get(object);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				tempMap.put(f.getName(), o==null?"":o.toString());
			}
		}
		return tempMap;
	}

	/**
	 * url 参数串连
	 * @param map
	 * @param keyLower
	 * @param valueUrlencode
	 * @return
	 */
	public static String mapJoin(Map<String, String> map,boolean keyLower,boolean valueUrlencode){
		StringBuilder stringBuilder = new StringBuilder();
		for(String key :map.keySet()){
			if(map.get(key)!=null&&!"".equals(map.get(key))){
				try {
					String temp = (key.endsWith("_")&&key.length()>1)?key.substring(0,key.length()-1):key;
					stringBuilder.append(keyLower?temp.toLowerCase():temp)
								 .append("=")
								 .append(valueUrlencode?URLEncoder.encode(map.get(key),"utf-8").replace("+", "%20"):map.get(key))
								 .append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if(stringBuilder.length()>0){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		return stringBuilder.toString();
	}

	/**
	 * 参数串联 打包成md5签名
	 * @param map
	 * @param keyLower
	 * @param valueUrlencode
	 * @return
	 */
	public static String md5Join(Map<String, String> map,boolean keyLower,boolean valueUrlencode){
		String origin = mapJoin(map, keyLower, valueUrlencode);
		return Md5Util.getMd5(origin.getBytes());
	}
}
