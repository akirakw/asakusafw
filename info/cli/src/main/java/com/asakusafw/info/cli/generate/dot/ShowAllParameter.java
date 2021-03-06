/**
 * Copyright 2011-2019 Asakusa Framework Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asakusafw.info.cli.generate.dot;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Provides verbose flag for draw group.
 * @since 0.10.0
 */
@Parameters(resourceBundle = "com.asakusafw.info.cli.jcommander")
public class ShowAllParameter {

    /**
     * Whether or not the help message is required.
     */
    @Parameter(
            names = { "-v", "--verbose", "--show-all", },
            descriptionKey = "parameter.show-all",
            required = false
    )
    public boolean required = false;

    /**
     * Returns the required.
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }
}
