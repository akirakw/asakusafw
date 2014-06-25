/**
 * Copyright 2011-2014 Asakusa Framework Team.
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
package com.asakusafw.directio.hive.tools.cli;

import com.asakusafw.directio.hive.common.HiveTableInfo;

/**
 * Provides string from {@link HiveTableInfo}.
 * @since 0.7.0
 */
public interface Stringnizer {

    /**
     * Always returns {@code null}.
     */
    Stringnizer NULL = new Stringnizer() {
        @Override
        public String toString(HiveTableInfo table) {
            return null;
        }
    };

    /**
     * Returns text for the table.
     * @param table the target table
     * @return the related text, or {@code null} to 'nothing'
     */
    String toString(HiveTableInfo table);
}
