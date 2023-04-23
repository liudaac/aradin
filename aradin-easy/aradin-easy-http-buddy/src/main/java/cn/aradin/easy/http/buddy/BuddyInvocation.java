package cn.aradin.easy.http.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;

public class BuddyInvocation {

	@SuppressWarnings("unused")
	public <T> T create(Class<T> interfaceClazz) {
		ByteBuddy buddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
		DynamicType.Unloaded<?> dynamicType = 
				buddy.subclass(interfaceClazz).name(getNewClassName(interfaceClazz))
				.method(ElementMatchers.isDeclaredBy(interfaceClazz))
				.intercept(null).make();
		return null;
	}
	
	private static String getNewClassName(Class<?> clazz) {
        return clazz.getSimpleName() + "Impl";
    }
}
