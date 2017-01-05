/**
 * Copyright 2011-2017 Asakusa Framework Team.
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
package com.asakusafw.directio.hive.orc;

import com.asakusafw.directio.hive.serde.DataModelDescriptor;
import com.asakusafw.runtime.directio.DataFormat;

/**
 * An implementation of {@link DataFormat} for ORCFile.
 * @param <T> the data model type
 * @since 0.7.0
 */
public class OrcFileFormat<T> extends AbstractOrcFileFormat<T> {

    private final String tableName;

    private final OrcFormatConfiguration configuration;

    private final DataModelDescriptor descriptor;

    /**
     * Creates a new instance.
     * @param tableName the target table name
     * @param configuration the format configuration
     * @param descriptor the data model descriptor
     */
    public OrcFileFormat(String tableName, OrcFormatConfiguration configuration, DataModelDescriptor descriptor) {
        this.tableName = tableName;
        this.descriptor = descriptor;
        this.configuration = configuration;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public DataModelDescriptor getDataModelDescriptor() {
        return descriptor;
    }

    @Override
    public OrcFormatConfiguration getFormatConfiguration() {
        return configuration;
    }
}
