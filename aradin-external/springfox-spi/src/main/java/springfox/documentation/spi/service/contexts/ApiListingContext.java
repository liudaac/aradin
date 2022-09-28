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

import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;

public class ApiListingContext {
  private final DocumentationType documentationType;
  private final ResourceGroup resourceGroup;
  private final ApiListingBuilder apiListingBuilder;

  public ApiListingContext(
      DocumentationType documentationType,
      ResourceGroup resourceGroup,
      ApiListingBuilder apiListingBuilder) {

    this.documentationType = documentationType;
    this.resourceGroup = resourceGroup;
    this.apiListingBuilder = apiListingBuilder;
  }

  public DocumentationType getDocumentationType() {
    return documentationType;
  }

  public ResourceGroup getResourceGroup() {
    return resourceGroup;
  }

  public ApiListingBuilder apiListingBuilder() {
    return apiListingBuilder;
  }

}
