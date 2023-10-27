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

package springfox.documentation.schema.property.bean;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.schema.property.BaseModelProperty;
import springfox.documentation.spi.schema.AlternateTypeProvider;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.schema.property.bean.Accessors.*;


public class BeanModelProperty extends BaseModelProperty {
  private static final Logger LOG = LoggerFactory.getLogger(BeanModelProperty.class);
  private final ResolvedMethod method;
  private TypeResolver typeResolver;


  public BeanModelProperty(
      String propertyName,
      ResolvedMethod method,
      TypeResolver typeResolver,
      AlternateTypeProvider alternateTypeProvider,
      BeanPropertyDefinition jacksonProperty) {
    super(propertyName, typeResolver, alternateTypeProvider, jacksonProperty);

    this.method = method;
    this.typeResolver = typeResolver;
  }

  private static ResolvedType adjustedToClassmateBug(TypeResolver typeResolver, ResolvedType resolvedType) {
    if (resolvedType.getErasedType().getTypeParameters().length > 0) {
      List<ResolvedType> typeParms = newArrayList();
      for (ResolvedType each : resolvedType.getTypeParameters()) {
        typeParms.add(adjustedToClassmateBug(typeResolver, each));
      }
      return typeResolver.resolve(resolvedType, typeParms.toArray(new Type[typeParms.size()]));
    } else {
      return typeResolver.resolve(resolvedType.getErasedType());
    }
  }

  public static ResolvedType paramOrReturnType(TypeResolver typeResolver, ResolvedMethod input) {
    if (maybeAGetter(input.getRawMember())) {
      LOG.debug("Evaluating unwrapped getter for member {}", input.getRawMember().getName());
      return adjustedToClassmateBug(typeResolver, input.getReturnType());
    } else {
      LOG.debug("Evaluating unwrapped setter for member {}", input.getRawMember().getName());
      return adjustedToClassmateBug(typeResolver, input.getArgumentType(0));
    }
  }

  @Override
  protected ResolvedType realType() {
    return paramOrReturnType(typeResolver, method);
  }
}
