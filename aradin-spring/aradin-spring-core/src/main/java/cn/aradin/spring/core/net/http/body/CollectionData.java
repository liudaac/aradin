package cn.aradin.spring.core.net.http.body;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;

public class CollectionData<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7209013470857174379l;
	private long total;
    private Collection<T> records = Lists.newArrayList();
	
    public CollectionData() {
    	this.total = 0l;
    }
    
	public CollectionData(long total) {
		this.total = total;
	}
	
	public CollectionData(long total, Collection<T> records) {
		this.total = total<=0?records.size():total;
		this.records = records;
	}
    
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Collection<T> getRecords() {
		return records;
	}

	public void setRecords(Collection<T> records) {
		this.records = records;
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
