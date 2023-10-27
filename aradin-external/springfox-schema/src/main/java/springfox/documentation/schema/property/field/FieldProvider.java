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

package springfox.documentation.schema.property.field;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.collect.Lists.*;

@Component
public class FieldProvider {
  private final TypeResolver typeResolver;

  @Autowired
  public FieldProvider(TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  public Iterable<ResolvedField> in(ResolvedType resolvedType) {
    MemberResolver memberResolver = new MemberResolver(typeResolver);
    if (resolvedType.getErasedType() == Object.class) {
      return newArrayList();
    }
    ResolvedTypeWithMembers resolvedMemberWithMembers = memberResolver.resolve(resolvedType, null, null);
    return newArrayList(resolvedMemberWithMembers.getMemberFields());
  }
}
