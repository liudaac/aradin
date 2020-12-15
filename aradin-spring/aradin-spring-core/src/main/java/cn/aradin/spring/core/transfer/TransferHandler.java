package cn.aradin.spring.core.transfer;

/**
 * Abstract Handler Used For Transferring A To B
 * @author daliu
 *
 * @param <A>
 * @param <B>
 */
public abstract class TransferHandler<A, B> {
	
	protected Object[] objects;
	
	public TransferHandler(Object... objects){
		this.objects = objects;
	}
	
	public abstract B transTo(A a);
}
