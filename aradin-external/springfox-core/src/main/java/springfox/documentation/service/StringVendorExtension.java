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
package springfox.documentation.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class StringVendorExtension implements VendorExtension<String> {
  private String name;
  private String value;

  public StringVendorExtension(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringVendorExtension that = (StringVendorExtension) o;
    return Objects.equal(name, that.name) &&
        Objects.equal(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("value", value)
        .toString();
  }
}
