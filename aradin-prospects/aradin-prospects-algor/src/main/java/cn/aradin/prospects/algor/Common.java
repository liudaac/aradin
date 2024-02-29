package cn.aradin.prospects.algor;

public class Common {

	public static void sort(Comparable[] a) {
		/* 请见算法2.1、算法2.2、算法2.3、算法2.4、算法2.5或算法2.7 */ }

	public static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	public static void exch(Comparable[] a, int i, int j) {
		Comparable t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	public static void show(Comparable[] a) { // 在单行中打印数组
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println();
	}

	public static boolean isSorted(Comparable[] a) { // 测试数组元素是否有序
		for (int i = 1; i < a.length; i++)
			if (less(a[i], a[i - 1]))
				return false;
		return true;
	}

	public static void main(String[] args) { // 从标准输入读取字符串，将它们排序并输出
		sort(args);
		assert isSorted(args);
		show(args);
	}
}