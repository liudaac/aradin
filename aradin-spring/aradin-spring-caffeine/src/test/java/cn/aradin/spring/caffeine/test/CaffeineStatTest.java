package cn.aradin.spring.caffeine.test;

import com.alibaba.fastjson2.JSONObject;

import cn.aradin.spring.caffeine.cache.Caffeineson;
import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;

public class CaffeineStatTest {

	public static void main(String[] args) {
		CaffeinesonConfig config = new CaffeinesonConfig(20000l, 20000l, 0, 100, 100l, 0, true, true, true, false);
		Caffeineson caffeineson = new Caffeineson("test", config, null);
		for(int i=0; i<100; i++) {
			caffeineson.put(i, i);
		}
		while (!Thread.interrupted()) {
			for (int i=0; i<100; i++) {
				System.out.print(caffeineson.get(i) != null?caffeineson.get(i).get():0);
			}
			System.out.println("");
			System.out.println(JSONObject.toJSONString(caffeineson.stats()));
			try {
				Thread.sleep(5000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
