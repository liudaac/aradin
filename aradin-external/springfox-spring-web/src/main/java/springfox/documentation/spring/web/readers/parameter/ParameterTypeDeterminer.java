/*
 *
 *  Copyright 2017-2019 the original author or authors.
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
package springfox.documentation.spring.web.readers.parameter;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Set;

class ParameterTypeDeterminer {
  private ParameterTypeDeterminer() {
    throw new UnsupportedOperationException();
  }

  public static String determineScalarParameterType(Set<? extends MediaType> consumes, HttpMethod method) {
    String parameterType = "query";

    if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
        && method == HttpMethod.POST) {
      parameterType = "form";
    } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
        && method == HttpMethod.POST) {
      parameterType = "formData";
    }

    return parameterType;
  }
}
