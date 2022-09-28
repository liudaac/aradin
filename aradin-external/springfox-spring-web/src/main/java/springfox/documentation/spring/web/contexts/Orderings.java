package springfox.documentation.spring.web.contexts;

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;

import com.google.common.collect.Ordering;

import springfox.documentation.RequestHandler;

public class Orderings {

	public static Ordering<RequestHandler> byOperationName() {
		return Ordering.from(new Comparator<RequestHandler>() {
			@Override
			public int compare(RequestHandler first, RequestHandler second) {
				return first.getName().compareTo(second.getName());
			}
		});
	}

	public static Ordering<RequestHandler> byPatternsCondition() {
		return Ordering.from(new Comparator<RequestHandler>() {
			@Override
			public int compare(RequestHandler first, RequestHandler second) {
				return patternsCondition(first).toString()
						.compareTo(patternsCondition(second).toString());
			}
		});
	}

	public static Comparator<RequestHandler> byPathPatternsCondition() {
		return Comparator.comparing(requestHandler -> sortedPaths(requestHandler.getPathPatternsCondition()));
	}

	static String sortedPaths(PathPatternsRequestCondition patternsCondition) {
		TreeSet<String> paths = new TreeSet<>(patternsCondition.getPatternValues());
		return paths.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
	}

	public static PathPatternsRequestCondition patternsCondition(RequestHandler handler) {
		return handler.getPathPatternsCondition();
	}
}
