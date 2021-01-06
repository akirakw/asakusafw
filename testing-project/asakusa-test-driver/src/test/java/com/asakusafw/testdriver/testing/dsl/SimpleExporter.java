/**
 * Copyright 2011-2021 Asakusa Framework Team.
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
package com.asakusafw.testdriver.testing.dsl;

import com.asakusafw.testdriver.testing.model.Simple;
import com.asakusafw.testdriver.testing.moderator.MockExporterDescription;

/**
 * An exporter description for {@link Simple} model.
 * @since 0.2.0
 */
public class SimpleExporter extends MockExporterDescription {

    static final String DIRECTORY = "target/testing/testdriver/output";

    static final String OUTPUT_PREFIX = DIRECTORY + "/data-";

    @Override
    public Class<?> getModelType() {
        return Simple.class;
    }

    @Override
    public String getGlob() {
        return OUTPUT_PREFIX + '*';
    }
}
