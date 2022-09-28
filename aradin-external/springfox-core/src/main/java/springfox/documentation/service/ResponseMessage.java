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

import springfox.documentation.schema.ModelReference;

import java.util.List;
import java.util.Map;

public class ResponseMessage {
  private final int code;
  private final String message;
  private final ModelReference responseModel;
  private final Map<String, Header> headers;
  private final List<VendorExtension> vendorExtensions;

  public ResponseMessage(
      int code,
      String message,
      ModelReference responseModel,
      Map<String, Header> headers,
      List<VendorExtension> vendorExtensions) {
    this.code = code;
    this.message = message;
    this.responseModel = responseModel;
    this.headers = headers;
    this.vendorExtensions = vendorExtensions;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public ModelReference getResponseModel() {
    return responseModel;
  }

  public Map<String, Header> getHeaders() {
    return headers;
  }

  public List<VendorExtension> getVendorExtensions() {
    return vendorExtensions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResponseMessage that = (ResponseMessage) o;

    return code == that.code;

  }

  @Override
  public int hashCode() {
    return code;
  }
}
