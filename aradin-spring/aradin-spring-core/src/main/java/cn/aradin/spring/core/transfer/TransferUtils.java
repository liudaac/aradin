package cn.aradin.spring.core.transfer;

import java.util.Collection;

/**
 * Util Class
 * A To B OR Collection(A) To Collection(B)
 * @author liudaac
 *
 */
public class TransferUtils {
	
	public static <A, B> Collection<B> transArray(Collection<A> source, Collection<B> target, TransferHandler<A, B> trans) {
		if (source != null) {
			for(A a : source) {
				B b = trans.transTo(a);
				target.add(b);
			}
		}
		return target;
	}
	
	public static <A, B> B transObject(A source, TransferHandler<A, B> trans) {
		B b = trans.transTo(source);
		return b;
	}
	
	public static <A, B> void transObjectFill(A source, B target, TransferHandler<A, B> trans) {
		trans.transToFill(source, target);
	}
}
