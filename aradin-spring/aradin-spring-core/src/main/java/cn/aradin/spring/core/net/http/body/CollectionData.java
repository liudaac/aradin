package cn.aradin.spring.core.net.http.body;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
@Data
public class CollectionData<T> implements Serializable {
	
	private long total;
    private Collection<T> records = Lists.newArrayList();
	
	public CollectionData(long total) {
		this.total = total;
	}
	
	public CollectionData(long total, Collection<T> records) {
		this.total = total<=0?records.size():total;
		this.records = records;
	}
    
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
