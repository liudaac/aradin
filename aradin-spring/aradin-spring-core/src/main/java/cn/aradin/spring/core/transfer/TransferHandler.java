package cn.aradin.spring.core.transfer;

/**
 * Abstract Handler Used For Transferring A To B
 * @author liudaac
 *
 * @param <A> source clazz
 * @param <B> target clazz
 */
public abstract class TransferHandler<A, B> {
	
	protected Object[] objects;
	
	public TransferHandler(Object... objects){
		this.objects = objects;
	}
	
	public abstract B transTo(A a);
	
	public abstract B transToFill(A a, B b);
}
