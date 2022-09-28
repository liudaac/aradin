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
package springfox.documentation.spi.service.contexts;

import com.google.common.base.Optional;
import springfox.documentation.PathProvider;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;

import java.util.List;

import static com.google.common.collect.Lists.*;

@Incubating("2.1.0")
public class PathContext {

  private final RequestMappingContext parent;
  private final Optional<Operation> operation;

  public PathContext(RequestMappingContext parent, Optional<Operation> operation) {
    this.parent = parent;
    this.operation = operation;
  }

  public DocumentationContext documentationContext() {
    return parent.getDocumentationContext();
  }

  public PathProvider pathProvider() {
    return parent.getDocumentationContext().getPathProvider();
  }

  public List<Parameter> getParameters() {
    if (operation.isPresent()) {
      return operation.get().getParameters();
    }
    return newArrayList();
  }
}
