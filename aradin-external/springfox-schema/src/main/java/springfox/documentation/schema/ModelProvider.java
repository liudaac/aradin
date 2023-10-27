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

package springfox.documentation.schema;


import springfox.documentation.annotations.Cacheable;
import springfox.documentation.spi.schema.contexts.ModelContext;

public interface ModelProvider {
  @Cacheable(value = "models")
  com.google.common.base.Optional<Model> modelFor(ModelContext modelContext);

  java.util.Map<String, Model> dependencies(ModelContext modelContext);
}
