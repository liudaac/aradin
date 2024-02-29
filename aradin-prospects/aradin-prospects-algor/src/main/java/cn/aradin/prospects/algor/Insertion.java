package cn.aradin.prospects.algor;

import static cn.aradin.prospects.algor.Common.*;

public class Insertion {
	public static void sort(Comparable[] a) { // 将a[]按升序排列
		int N = a.length;
		for (int i = 1; i < N; i++) { // 将 a[i] 插入到 a[i-1]、a[i-2]、a[i-3]……之中
			for (int j = i; j > 0 && less(a[j], a[j - 1]); j--)
				exch(a, j, j - 1);
		}
	}
}