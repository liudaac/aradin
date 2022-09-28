/*
 *
 *  Copyright 2015-2019 the original author or authors.
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

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.core.Ordered;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.builders.BuilderDefaults.*;

public class ParameterBuilder {
  private static final Collection<String> PARAMETER_TYPES_ALLOWING_EMPTY_VALUE =
      ImmutableList.of("query", "formData");
  private String name;
  private String description;
  private String defaultValue;
  private boolean required;
  private boolean allowMultiple;
  private AllowableValues allowableValues;
  private String paramType;
  private String paramAccess;
  private ResolvedType type;
  private ModelReference modelRef;
  private boolean hidden;
  private String pattern;
  private List<VendorExtension> vendorExtensions = newArrayList();
  private String collectionFormat = null;
  private Boolean allowEmptyValue;
  private int order = Ordered.LOWEST_PRECEDENCE;
  private Object scalarExample;
  private Multimap<String, Example> examples = LinkedListMultimap.create();

  /**
   * Copy builder
   *
   * @param other parameter to copy from
   * @return this
   */
  ParameterBuilder from(Parameter other) {
    return name(other.getName())
        .allowableValues(other.getAllowableValues())
        .allowMultiple(other.isAllowMultiple())
        .defaultValue(other.getDefaultValue())
        .description(other.getDescription())
        .modelRef(other.getModelRef())
        .parameterAccess(other.getParamAccess())
        .parameterType(other.getParamType())
        .required(other.isRequired())
        .type(other.getType().orNull())
        .hidden(other.isHidden())
        .allowEmptyValue(other.isAllowEmptyValue())
        .order(other.getOrder())
        .vendorExtensions(other.getVendorExtentions());
  }

  /**
   * Updates the parameter name
   *
   * @param name - name of the parameter
   * @return this
   */
  public ParameterBuilder name(String name) {
    this.name = defaultIfAbsent(name, this.name);
    return this;
  }

  /**
   * Updates the description of the parameter
   *
   * @param description - description
   * @return this
   */
  public ParameterBuilder description(String description) {
    this.description = defaultIfAbsent(description, this.description);
    return this;
  }

  /**
   * Updates the default value of the parameter
   *
   * @param defaultValue - default value
   * @return this
   */
  public ParameterBuilder defaultValue(String defaultValue) {
    this.defaultValue = defaultIfAbsent(defaultValue, this.defaultValue);
    return this;
  }

  /**
   * Updates if the parameter is required or optional
   *
   * @param required - flag to indicate if the parameter is required
   * @return this
   */
  public ParameterBuilder required(boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Updates if the parameter should allow multiple values
   *
   * @param allowMultiple - flag to indicate if the parameter supports multi-value
   * @return this
   */
  public ParameterBuilder allowMultiple(boolean allowMultiple) {
    this.allowMultiple = allowMultiple;
    return this;
  }

  /**
   * Updates if the parameter is bound by a range of values or a range of numerical values
   *
   * @param allowableValues - allowable values (instance of @see springfox.documentation.service.AllowableListValues
   *                        or @see springfox.documentation.service.AllowableRangeValues)
   * @return this
   */
  public ParameterBuilder allowableValues(AllowableValues allowableValues) {
    this.allowableValues = emptyToNull(allowableValues, this.allowableValues);
    return this;
  }

  /**
   * Updates the type of parameter
   *
   * @param paramType - Could be header, cookie, body, query etc.
   * @return this
   */
  public ParameterBuilder parameterType(String paramType) {
    this.paramType = defaultIfAbsent(paramType, this.paramType);
    return this;
  }

  /**
   * Updates the parameter access
   *
   * @param paramAccess - parameter access
   * @return this
   */
  public ParameterBuilder parameterAccess(String paramAccess) {
    this.paramAccess = defaultIfAbsent(paramAccess, this.paramAccess);
    return this;
  }

  /**
   * Updates the type of parameter
   *
   * @param type - represents the resolved type of the parameter
   * @return this
   */
  public ParameterBuilder type(ResolvedType type) {
    this.type = defaultIfAbsent(type, this.type);
    return this;
  }

  /**
   * Represents the convenience method to infer the model reference
   * Consolidate or figure out whats can be rolled into the other.
   *
   * @param modelRef model reference
   * @return this
   */
  public ParameterBuilder modelRef(ModelReference modelRef) {
    this.modelRef = defaultIfAbsent(modelRef, this.modelRef);
    return this;
  }

  /**
   * Updates if the parameter is hidden
   *
   * @param hidden - flag to indicate if the parameter is hidden
   * @return this
   */
  public ParameterBuilder hidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  /**
   * Updates the parameter extensions
   *
   * @param extensions - parameter extensions
   * @return this
   */
  public ParameterBuilder vendorExtensions(List<VendorExtension> extensions) {
    this.vendorExtensions.addAll(nullToEmptyList(extensions));
    return this;
  }

  /**
   * Updates the parameter extensions
   *
   * @param collectionFormat - parameter collection format
   * @return this
   * @since 2.8.0
   */
  public ParameterBuilder collectionFormat(String collectionFormat) {
    this.collectionFormat = defaultIfAbsent(collectionFormat, this.collectionFormat);
    return this;
  }

  /**
   * Updates the flag that allows sending empty values for this parameter
   * @param allowEmptyValue - true/false
   * @return this
   * @since 2.8.1
   */
  public ParameterBuilder allowEmptyValue(Boolean allowEmptyValue) {
    this.allowEmptyValue = defaultIfAbsent(allowEmptyValue, this.allowEmptyValue);
    return this;
  }

  /**
   * Updates default order of precedence of parameters
   * @param order - between {@link Ordered#HIGHEST_PRECEDENCE}, {@link Ordered#LOWEST_PRECEDENCE}
   * @return this
   * @since 2.8.1
   */
  public ParameterBuilder order(int order) {
    this.order = order;
    return this;
  }

  public ParameterBuilder pattern(String pattern) {
    this.pattern = defaultIfAbsent(pattern, this.pattern);
    return this;
  }

  /**
   * @since 2.8.1
   * @param scalarExample example for non-body parameters
   * @return this
   */
  public ParameterBuilder scalarExample(Object scalarExample) {
    this.scalarExample = defaultIfAbsent(scalarExample, this.scalarExample);
    return this;
  }
  /**
   * @since 2.8.1
   * @param examples example for body parameters
   * @return this
   */
  public ParameterBuilder complexExamples(Multimap<String, Example> examples) {
    this.examples.putAll(examples);
    return this;
  }

  public Parameter build() {
    if (!PARAMETER_TYPES_ALLOWING_EMPTY_VALUE.contains(paramType)) {
      allowEmptyValue = null;
    }
    return new Parameter(
        name,
        description,
        defaultValue,
        required,
        allowMultiple,
        allowEmptyValue,
        modelRef,
        Optional.fromNullable(type),
        allowableValues,
        paramType,
        paramAccess,
        hidden,
        pattern,
        collectionFormat,
        order,
        scalarExample,
        examples,
        vendorExtensions);
  }
}
