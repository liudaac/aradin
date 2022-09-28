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

import com.fasterxml.classmate.ResolvedType;
import org.springframework.core.Ordered;

import static springfox.documentation.schema.WildcardType.*;

public class AlternateTypeRule implements Ordered {
  protected final ResolvedType original;
  protected final ResolvedType alternate;
  protected final int order;

  /**
   * Instantiates a new Alternate type rule.
   *
   * @param original  the original type
   * @param alternate the alternate type
   */
  public AlternateTypeRule(ResolvedType original, ResolvedType alternate) {
    this(original, alternate, Ordered.HIGHEST_PRECEDENCE);
  }

  /**
   * Instantiates a new Alternate type rule.
   *
   * @param original  the original type
   * @param alternate the alternate type
   * @param order the order {@link Ordered} in which the rules are applied
   */
  public AlternateTypeRule(ResolvedType original, ResolvedType alternate, int order) {
    this.original = original;
    this.alternate = alternate;
    this.order = order;
  }

  /**
   * Provides alternate for supplier type.
   *
   * @param type the type
   * @return the alternate for the type
   */
  public ResolvedType alternateFor(ResolvedType type) {
    if (appliesTo(type)) {
      if (hasWildcards(original)) {
        return replaceWildcardsFrom(WildcardType.collectReplaceables(type, original), alternate);
      } else {
        return alternate;
      }
    }
    return type;
  }

  /**
   * Check if an alternate applies to type.
   *
   * @param type the source
   * @return the boolean
   */
  public boolean appliesTo(ResolvedType type) {
    return hasWildcards(original)
            && wildcardMatch(type, original)
            || exactMatch(original, type);
  }

  public int getOrder() {
    return order;
  }
}
