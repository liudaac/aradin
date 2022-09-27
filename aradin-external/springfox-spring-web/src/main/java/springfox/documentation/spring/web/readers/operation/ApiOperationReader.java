/*
 *
 *  Copyright 2015-2016 the original author or authors.
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

package springfox.documentation.spring.web.readers.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.OperationNameGenerator;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.*;
import static java.util.Arrays.asList;

@Component
@Qualifier("default")
public class ApiOperationReader implements OperationReader {

  private static final Set<RequestMethod> allRequestMethods
      = new LinkedHashSet<RequestMethod>(asList(RequestMethod.values()));
  private final DocumentationPluginsManager pluginsManager;
  private final OperationNameGenerator nameGenerator;

  @Autowired
  public ApiOperationReader(DocumentationPluginsManager pluginsManager, OperationNameGenerator nameGenerator) {
    this.pluginsManager = pluginsManager;
    this.nameGenerator = nameGenerator;
  }

  @Override
//  @Cacheable(value = "operations", keyGenerator = OperationsKeyGenerator.class)
  public List<Operation> read(RequestMappingContext outerContext) {

    List<Operation> operations = newArrayList();

    Set<RequestMethod> requestMethods = outerContext.getMethodsCondition();
    Set<RequestMethod> supportedMethods = supportedMethods(requestMethods);

    //Setup response message list
    Integer currentCount = 0;
    for (RequestMethod httpRequestMethod : supportedMethods) {
      OperationContext operationContext = new OperationContext(new OperationBuilder(nameGenerator),
          httpRequestMethod,
          outerContext,
          currentCount);

      Operation operation = pluginsManager.operation(operationContext);
      if (!operation.isHidden()) {
        operations.add(operation);
        currentCount++;
      }
    }
    Collections.sort(operations, outerContext.operationOrdering());

    return operations;
  }

  private Set<RequestMethod> supportedMethods(Set<RequestMethod> requestMethods) {
    return requestMethods == null || requestMethods.isEmpty()
           ? allRequestMethods
           : requestMethods;
  }

}
