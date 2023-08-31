package cn.aradin.spring.core.net.http.code;

import java.util.Arrays;
import java.util.Optional;

public interface CodedEnum {
	
	/**
	 * Get error code as int
	 * @return error code
	 */
	int getCode();

    static <E extends Enum<?> & CodedEnum> Optional<E> codeOf(Class<E> enumClass, int code) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getCode() == code).findAny();
    }
}
