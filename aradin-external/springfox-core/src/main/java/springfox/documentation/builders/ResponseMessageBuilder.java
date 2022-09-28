/*
 *
 *  Copyright 2017 the original author or authors.
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

package springfox.documentation.builders;

import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.*;
import static springfox.documentation.builders.BuilderDefaults.*;

public class ResponseMessageBuilder {
  private int code;
  private String message;
  private ModelReference responseModel;
  private Map<String, Header> headers = newTreeMap();
  private List<VendorExtension> vendorExtensions = newArrayList();

  /**
   * Updates the http response code
   *
   * @param code - response code
   * @return this
   */
  public ResponseMessageBuilder code(int code) {
    this.code = code;
    return this;
  }

  /**
   * Updates the response message
   *
   * @param message - message
   * @return this
   */
  public ResponseMessageBuilder message(String message) {
    this.message = defaultIfAbsent(message, this.message);
    return this;
  }

  /**
   * Updates the model the response represents
   *
   * @param responseModel - model reference
   * @return this
   */
  public ResponseMessageBuilder responseModel(ModelReference responseModel) {
    this.responseModel = defaultIfAbsent(responseModel, this.responseModel);
    return this;
  }

  /**
   * Updates the response headers
   *
   * @param headers header responses
   * @return this
   * @deprecated Use the {@link ResponseMessageBuilder#headersWithDescription} instead
   * @since 2.5.0
   */
  @Deprecated
  public ResponseMessageBuilder headers(Map<String, ModelReference> headers) {
    this.headers.putAll(transformEntries(nullToEmptyMap(headers), toHeaderEntry()));
    return this;
  }


  private EntryTransformer<String, ModelReference, Header> toHeaderEntry() {
    return new EntryTransformer<String, ModelReference, Header>() {
      @Override
      public Header transformEntry(String key, ModelReference value) {
        return new Header(key, "", value);
      }
    };
  }

  /**
   * Updates the response headers
   *
   * @param headers headers with description
   * @return this
   * @since 2.5.0
   */
  public ResponseMessageBuilder headersWithDescription(Map<String, Header> headers) {
    this.headers.putAll(nullToEmptyMap(headers));
    return this;
  }

  /**
   * Updates the response message extensions
   *
   * @param extensions - response message extensions
   * @return this
   * @since 2.5.0
   */
  public ResponseMessageBuilder vendorExtensions(List<VendorExtension> extensions) {
    this.vendorExtensions.addAll(nullToEmptyList(extensions));
    return this;
  }

  public ResponseMessage build() {
    return new ResponseMessage(code, message, responseModel, headers, vendorExtensions);
  }
}