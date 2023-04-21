package cn.aradin.spring.core.algo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;

/**
 * https://blog.csdn.net/yangbodong22011/article/details/73369426
 * SWWR负载均衡算法
 * @author liudaac
 *
 */
public class SWRRMethod {
	private List<SWRRObj> objs = Lists.newArrayList();
	private List<Integer> datas = Lists.newArrayList();
	private Integer sum = 0;
	private boolean hasNext = true;
	
	public SWRRMethod(final List<Integer> datas) {
		if (CollectionUtils.isNotEmpty(datas)) {
			//拷贝List防止外部引用发生变更
			this.datas = datas;
			//初始化算法对象
			for(int i=0; i<datas.size(); i++) {
				SWRRObj obj = new SWRRObj(i, datas.get(i));
				objs.add(obj);
			}
			sum = objs.stream().mapToInt(SWRRObj::getValue).sum();
		}
	}
	
	public Integer next() {
		if (hasNext) {
			if (CollectionUtils.isNotEmpty(objs)) {
				SWRRObj maxObj = Collections.max(objs, new Comparator<SWRRObj>() {
					@Override
					public int compare(SWRRObj o1, SWRRObj o2) {
						// TODO Auto-generated method stub
						return o1.value - o2.value;
					}
				});
				Integer result = datas.get(maxObj.getIndex());
				maxObj.setValue(maxObj.getValue() - sum);
				if (maxObj.getValue().equals(0)) {
					SWRRObj newMax = Collections.max(objs, new Comparator<SWRRObj>() {
						@Override
						public int compare(SWRRObj o1, SWRRObj o2) {
							// TODO Auto-generated method stub
							return o1.value - o2.value;
						}
					});
					if (newMax.getValue().equals(0)) {
						hasNext = false;
					}
				}
				objs.forEach(obj -> {
					obj.setValue(obj.getValue()+datas.get(obj.getIndex()));
				});
				return result;
			}
		}
		return null;
	}
	
	class SWRRObj {
		private Integer index;
		private Integer value;
		public SWRRObj(Integer index, Integer value) {
			this.index = index;
			this.value = value;
		}
		public Integer getIndex() {
			return index;
		}
		public void setIndex(Integer index) {
			this.index = index;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	public final static void main(String[] args) {
		List<Integer> datas = Lists.newArrayList();
		datas.add(1);
		datas.add(3);
		datas.add(5);
		datas.add(2);
		SWRRMethod method = new SWRRMethod(datas);
		Integer cursor = 0;
		while ((cursor = method.next()) != null) {
			System.out.println(cursor);
			System.out.println(JSONObject.toJSONString(method.objs));
		}
	}
}