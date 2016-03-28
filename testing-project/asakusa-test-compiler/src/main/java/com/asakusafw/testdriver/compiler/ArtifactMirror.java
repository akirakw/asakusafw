/**
 * Copyright 2011-2016 Asakusa Framework Team.
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
package com.asakusafw.testdriver.compiler;

import java.io.File;

/**
 * Represents a compiled artifact.
 * @since 0.8.0
 */
public interface ArtifactMirror {

    /**
     * Returns the batch application mirror.
     * @return the batch application mirror
     */
    BatchMirror getBatch();

    /**
     * Returns the compiled contents.
     * The contents may be disabled after the parent {@link CompilerSession} was closed.
     * @return the compiled contents
     */
    File getContents();
}