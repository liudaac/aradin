/*
 *
 *  Copyright 2015 the original author or authors.
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

package springfox.documentation.swagger.web;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.Set;

import static com.google.common.base.Optional.*;
import static com.google.common.base.Strings.*;
import static com.google.common.collect.FluentIterable.*;
import static com.google.common.collect.Sets.*;
import static org.springframework.core.annotation.AnnotationUtils.*;
import static org.springframework.util.StringUtils.*;
import static springfox.documentation.spring.web.paths.Paths.*;

@Component
@Deprecated
public class ClassOrApiAnnotationResourceGrouping implements ResourceGroupingStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(ClassOrApiAnnotationResourceGrouping.class);

  @Override
  public String getResourceDescription(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
    Class<?> controllerClass = handlerMethod.getBeanType();
    String className = splitCamelCase(controllerClass.getSimpleName(), " ");

    return fromNullable(
        emptyToNull(
          stripSlashes(extractAnnotation(controllerClass, descriptionOrValueExtractor())
              .or(""))))
        .or(className);
  }

  @Override
  public Integer getResourcePosition(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
    Class<?> controllerClass = handlerMethod.getBeanType();
    Api apiAnnotation = findAnnotation(controllerClass, Api.class);
    if (null != apiAnnotation && hasText(apiAnnotation.value())) {
      return apiAnnotation.position();
    }
    return 0;
  }

  @Override
  public Set<ResourceGroup> getResourceGroups(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
    return from(groups(handlerMethod)).transform(toResourceGroup(requestMappingInfo, handlerMethod)).toSet();
  }

  private Set<String> groups(HandlerMethod handlerMethod) {
    Class<?> controllerClass = handlerMethod.getBeanType();
    String group = splitCamelCase(controllerClass.getSimpleName(), " ");
    String apiValue = fromNullable(findAnnotation(controllerClass, Api.class))
        .transform(toApiValue()).or("");
    return Strings.isNullOrEmpty(apiValue) ? newHashSet(normalize(group)) : newHashSet(normalize(apiValue));
  }

  private String normalize(String tag) {
    return tag.toLowerCase()
        .replaceAll(" ", "-")
        .replaceAll("/", "");
  }

  private Function<String, ResourceGroup> toResourceGroup(
      final RequestMappingInfo requestMappingInfo,
      final HandlerMethod handlerMethod) {

    return new Function<String, ResourceGroup>() {
      @Override
      public ResourceGroup apply(String group) {
        LOG.info("Group for method {} was {}", handlerMethod.getMethod().getName(), group);
        Integer position = getResourcePosition(requestMappingInfo, handlerMethod);
        return new ResourceGroup(group, handlerMethod.getBeanType(), position);
      }
    };
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return SwaggerPluginSupport.pluginDoesApply(delimiter);
  }

  private <T> T extractAnnotation(Class<?> controllerClass,
                                  Function<Api, T> annotationExtractor) {

    Api apiAnnotation = findAnnotation(controllerClass, Api.class);
    return annotationExtractor.apply(apiAnnotation);
  }

  private Function<Api, Optional<String>> descriptionOrValueExtractor() {
    return new Function<Api, Optional<String>>() {
      @Override
      public Optional<String> apply(Api input) {
        //noinspection ConstantConditions
        return descriptionExtractor().apply(input).or(valueExtractor().apply(input));
      }
    };
  }

  private Function<Api, String> toApiValue() {
    return new Function<Api, String>() {
      @Override
      public String apply(Api input) {
        return normalize(input.value());
      }
    };
  }

  private Function<Api, Optional<String>> descriptionExtractor() {
    return new Function<Api, Optional<String>>() {
      @Override
      public Optional<String> apply(Api input) {
        if (null != input) {
          return fromNullable(emptyToNull(input.description()));
        }
        return Optional.absent();
      }
    };
  }

  private Function<Api, Optional<String>> valueExtractor() {
    return new Function<Api, Optional<String>>() {
      @Override
      public Optional<String> apply(Api input) {
        if (null != input) {
          return fromNullable(emptyToNull(input.value()));
        }
        return Optional.absent();
      }
    };
  }
}
