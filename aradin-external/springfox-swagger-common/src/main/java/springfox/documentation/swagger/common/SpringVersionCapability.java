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
package springfox.documentation.swagger.common;

public class SpringVersionCapability {

  private static final Version FIVE_ZERO_ZERO = Version.parse("5.0.0.RELEASE");
  private static final Version FIVE_ZERO_FIVE = Version.parse("5.0.5.RELEASE");
  private static final Version FOUR_THREE_FIFTEEN = Version.parse("4.3.15.RELEASE");

  public SpringVersionCapability() {
    throw new UnsupportedOperationException();
  }

  public static boolean supportsXForwardPrefixHeader(String version) {
    Version parsed = Version.parse(version);

    return (parsed.isGreaterThanOrEqualTo(FOUR_THREE_FIFTEEN)
                && parsed.isLessThan(FIVE_ZERO_ZERO)) || 
        parsed.isGreaterThanOrEqualTo(FIVE_ZERO_FIVE);
  }
}
