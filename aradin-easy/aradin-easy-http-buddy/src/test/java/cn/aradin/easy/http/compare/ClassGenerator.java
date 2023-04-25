package cn.aradin.easy.http.compare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import cn.aradin.easy.http.EasyBuilder;
import cn.aradin.easy.http.annotation.Controller;
import cn.aradin.easy.http.buddy.BuddyBuilder;
import cn.aradin.easy.http.buddy.BuddyInvocation;
import cn.aradin.easy.http.compare.result.NcClient;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
//import sun.misc.ProxyGenerator;

/**
 * 生成双代理模式的Class *Http(JDK动态代理模式) *Buddy(Bytebuddy动态类)
 * 借助java -jar .\procyon-decompiler-0.6-prerelease.jar D:\test\NcClientHttp.class进行反编译对比
 * @author daliu
 *
 */
public class ClassGenerator {

	public static void main( String[] args )
    {
    	Controller s = NcClient.class.getAnnotation(Controller.class);
		String domain = null;
		if (s != null) {
			if (StringUtils.isNotBlank(s.param())) {
				domain = System.getProperty(s.param());
			}
			if (StringUtils.isBlank(domain)) {
				domain = s.value();
			}
		}
		if (StringUtils.isBlank(domain)) {
			throw new RuntimeException("@Controller参数不全，value or param");
		}
		ByteBuddy buddy = new ByteBuddy();
		try {
			buddy.subclass(NcClient.class).name(getNewClassName(NcClient.class))
					.method(ElementMatchers.any())
					.intercept(MethodDelegation.to(new BuddyInvocation(domain, null)))
					.make()
					.load(BuddyBuilder.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
					.saveIn(new File("D:\\test"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveClassFile(EasyBuilder.ins().service(NcClient.class).getClass(), "NcClientHttp");
    }
    
    public static void saveClassFile(Class<? extends NcClient> clazz,String proxyName) {
	    //生成class的字节数组，此处生成的class与proxy.newProxyInstance中生成的class除了代理类的名字不同，其它内容完全一致
    	// 需要JRE环境
//    	byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces());
		String path = "D:\\test\\";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path + proxyName + ".class");
//			fos.write(classFile);//保存到磁盘
			fos.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fos.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    private static String getNewClassName(Class<?> clazz) {
        return clazz.getSimpleName() + "Buddy";
    }
}
