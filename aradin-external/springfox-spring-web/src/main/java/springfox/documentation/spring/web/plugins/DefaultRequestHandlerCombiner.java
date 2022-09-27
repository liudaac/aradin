/*
 *
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package springfox.documentation.spring.web.plugins;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerCombiner;
import springfox.documentation.spring.web.WebMvcRequestHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.builders.BuilderDefaults.*;
import static springfox.documentation.spring.web.contexts.Orderings.*;

class DefaultRequestHandlerCombiner implements RequestHandlerCombiner {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestHandlerCombiner.class);
	private static final PathAndParametersEquivalence EQUIVALENCE = new PathAndParametersEquivalence();

	public List<RequestHandler> combine(List<RequestHandler> source) {
		List<RequestHandler> combined = new ArrayList<RequestHandler>();
		Multimap<String, RequestHandler> byPath = LinkedListMultimap.create();
		LOGGER.warn("Total number of request handlers {}", nullToEmptyList(source).size());
		LOGGER.error("TEST LINE .............");
		for (RequestHandler each : nullToEmptyList(source)) {
			WebMvcRequestHandler mvcRequestHandler = (WebMvcRequestHandler) each;
			LOGGER.info("Adding key: {}, {}", patternsCondition(mvcRequestHandler).toString(), each.toString());
			byPath.put(patternsCondition(mvcRequestHandler).toString(), each);
		}
		for (String key : byPath.keySet()) {
			combined.addAll(combined(byPath.get(key)));
		}
		LOGGER.debug("Combined number of request handlers {}", combined.size());
		return byPatternsCondition().sortedCopy(combined);
	}

	private Collection<? extends RequestHandler> combined(Collection<RequestHandler> requestHandlers) {
		List<RequestHandler> source = newArrayList(requestHandlers);
		if (source.size() == 0 || source.size() == 1) {
			return requestHandlers;
		}
		ListMultimap<Equivalence.Wrapper<RequestHandler>, RequestHandler> groupByEquality = safeGroupBy(source);
		List<RequestHandler> combined = newArrayList();
		for (Equivalence.Wrapper<RequestHandler> path : wrapperComparator().sortedCopy(groupByEquality.keySet())) {
			List<RequestHandler> handlers = groupByEquality.get(path);

			RequestHandler toCombine = path.get();
			if (handlers.size() > 1) {
				for (RequestHandler each : sortedByPathAndName(handlers)) {
					if (each.equals(toCombine)) {
						continue;
					}
					// noinspection ConstantConditions
					LOGGER.debug("Combining {} and {}", toCombine.toString(), each.toString());
					toCombine = combine(toCombine, each);
				}
			}
			combined.add(toCombine);
		}
		return combined;
	}

	private List<RequestHandler> sortedByPathAndName(List<RequestHandler> handlers) {
		return byPatternsCondition().compound(byOperationName()).sortedCopy(handlers);
	}

	private Ordering<Equivalence.Wrapper<RequestHandler>> wrapperComparator() {
		return Ordering.from(new Comparator<Equivalence.Wrapper<RequestHandler>>() {
			@Override
			public int compare(Equivalence.Wrapper<RequestHandler> first, Equivalence.Wrapper<RequestHandler> second) {
				return byPatternsCondition().compound(byOperationName()).compare(first.get(), second.get());
			}
		});
	}

	private ImmutableListMultimap<Equivalence.Wrapper<RequestHandler>, RequestHandler> safeGroupBy(
			List<RequestHandler> source) {
		try {
			return Multimaps.index(source, equivalenceAsKey());
		} catch (Exception e) {
			LOGGER.error("Unable to index request handlers {}. Request handlers with issues{}", e.getMessage(),
					keys(source));
			return ImmutableListMultimap.<Equivalence.Wrapper<RequestHandler>, RequestHandler>builder().build();
		}
	}

	private String keys(List<RequestHandler> source) {
		final StringBuffer sb = new StringBuffer("Request Handlers with duplicate keys {");
		for (int i = 0; i < source.size(); i++) {
			sb.append('\t').append(i).append(". ").append(source.get(i).key());
		}
		sb.append('}');
		return sb.toString();
	}

	private Function<RequestHandler, Equivalence.Wrapper<RequestHandler>> equivalenceAsKey() {
		return new Function<RequestHandler, Equivalence.Wrapper<RequestHandler>>() {
			@Override
			public Equivalence.Wrapper<RequestHandler> apply(RequestHandler input) {
				return EQUIVALENCE.wrap(input);
			}
		};
	}

	private RequestHandler combine(RequestHandler first, RequestHandler second) {
		return new CombinedRequestHandler(first, second);
	}
}
