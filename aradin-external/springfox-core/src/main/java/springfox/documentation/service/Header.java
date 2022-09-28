/*
 *
 *  Copyright 2016 the original author or authors.
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
package springfox.documentation.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import springfox.documentation.schema.ModelReference;

public class Header {
  private final String name;
  private final ModelReference modelReference;
  private final String description;

  public Header(String name, String description, ModelReference modelReference) {
    this.name = name;
    this.modelReference = modelReference;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public ModelReference getModelReference() {
    return modelReference;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Header header = (Header) o;
    return Objects.equal(name, header.name) &&
        Objects.equal(modelReference, header.modelReference) &&
        Objects.equal(description, header.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, modelReference, description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("modelReference", modelReference)
        .add("description", description)
        .toString();
  }
}
