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
package com.asakusafw.directio.hive.parquet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.asakusafw.directio.hive.serde.DataModelDescriptor;
import com.asakusafw.runtime.directio.Counter;
import com.asakusafw.runtime.io.ModelOutput;

import parquet.column.ParquetProperties;
import parquet.column.ParquetProperties.WriterVersion;
import parquet.hadoop.ParquetWriter;
import parquet.hadoop.api.WriteSupport;
import parquet.hadoop.metadata.CompressionCodecName;

/**
 * An implementation of {@link ModelOutput} for writing Parquet files.
 * @param <T> the data model type
 * @since 0.7.0
 * @version 0.7.2
 */
public class ParquetFileOutput<T> implements ModelOutput<T> {

    static final Log LOG = LogFactory.getLog(ParquetFileOutput.class);

    static final ParquetVersion LIBRARY_VERSION = ParquetVersion.getSupportedVersion();

    private final DataModelDescriptor descriptor;

    private final Configuration configuration;

    private final DataModelWriteSupport writeSupport;

    private final Path path;

    private final ParquetFileOutput.Options options;

    private final Counter counter;

    private ParquetWriter<T> currentWriter;

    /**
     * Creates a new instance.
     * @param descriptor the target data model descriptor
     * @param configuration the hadoop configuration
     * @param path the path to the target file
     * @param options the parquet file output options
     * @param counter the current counter
     */
    public ParquetFileOutput(
            DataModelDescriptor descriptor,
            Configuration configuration,
            Path path,
            ParquetFileOutput.Options options,
            Counter counter) {
        this.writeSupport = new DataModelWriteSupport(descriptor);
        this.descriptor = descriptor;
        this.configuration = configuration;
        this.path = path;
        this.options = options;
        this.counter = counter;
    }

    @Override
    public void write(T model) throws IOException {
        ParquetWriter<T> writer = prepareWriter();
        writer.write(model);

        // NOTE: only tell this is alive
        counter.add(0);
    }

    @SuppressWarnings("unchecked")
    private ParquetWriter<T> prepareWriter() throws IOException {
        ParquetWriter<T> writer = currentWriter;
        if (writer == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format(
                        Messages.getString("ParquetFileOutput.infoCreate"), //$NON-NLS-1$
                        descriptor.getDataModelClass().getSimpleName(),
                        path));
            }
            Options opts = options;
            writer = LIBRARY_VERSION.newInstance(
                    path,
                    (WriteSupport<T>) writeSupport,
                    opts,
                    configuration);
            currentWriter = writer;
        }
        return writer;
    }

    @Override
    public void close() throws IOException {
        if (currentWriter != null) {
            currentWriter.close();
            currentWriter = null;
            counter.add(Util.getFileSize(path, configuration));
        }
    }

    // CHECKSTYLE:OFF RedundantModifierCheck
    private static enum ParquetVersion {
    // CHECKSTYLE:ON RedundantModifierCheck

        // Note: put newer version on top

        V_15(10) {
            @Override
            <T> ParquetWriter<T> newInstance(
                    Path path,
                    WriteSupport<T> writeSupport,
                    ParquetFileOutput.Options options,
                    Configuration configuration) throws IOException {
                return new ParquetWriter<>(
                        path,
                        writeSupport,
                        options.getCompressionCodecName(),
                        options.getBlockSize(),
                        options.getDataPageSize(),
                        options.getDictionaryPageSize(),
                        options.isEnableDictionary(),
                        options.isEnableValidation(),
                        options.getWriterVersion(),
                        configuration);
            }
        },

        V_13(9) {
            @Override
            <T> ParquetWriter<T> newInstance(
                    Path path,
                    WriteSupport<T> writeSupport,
                    ParquetFileOutput.Options options,
                    Configuration configuration) throws IOException {
                return new ParquetWriter<>(
                        path,
                        writeSupport,
                        options.getCompressionCodecName(),
                        options.getBlockSize(),
                        options.getDataPageSize(),
                        options.getDictionaryPageSize(),
                        options.isEnableDictionary(),
                        options.isEnableValidation(),
                        options.getWriterVersion());
            }
        },

        V_12(8) {
            @Override
            <T> ParquetWriter<T> newInstance(
                    Path path,
                    WriteSupport<T> writeSupport,
                    ParquetFileOutput.Options options,
                    Configuration configuration) throws IOException {
                return new ParquetWriter<>(
                        path,
                        writeSupport,
                        options.getCompressionCodecName(),
                        options.getBlockSize(),
                        options.getDataPageSize(),
                        options.getDictionaryPageSize(),
                        options.isEnableDictionary(),
                        options.isEnableValidation());
            }
        },

        UNKNOWN(2) {
            @Override
            <T> ParquetWriter<T> newInstance(
                    Path path,
                    WriteSupport<T> writeSupport,
                    ParquetFileOutput.Options options,
                    Configuration configuration) throws IOException {
                return new ParquetWriter<>(path, writeSupport);
            }
        },
        ;

        private static final Class<?>[] PARAMETER_TYPES = {
            Path.class,                 //
            WriteSupport.class,         // 2: minimum
            CompressionCodecName.class, //
            int.class,                  //
            int.class,                  //
            int.class,                  //
            boolean.class,              //
            boolean.class,              // 8: v1.2
            ParquetProperties.WriterVersion.class, // 9: v1.3
            Configuration.class,        // 10 v1.5
        };

        final int parameterCount;

        private transient volatile Constructor<?> constructor;

        ParquetVersion(int parameterCount) {
            this.parameterCount = parameterCount;
        }

        private Constructor<?> resolve() {
            if (constructor != null) {
                return constructor;
            }
            Class<?>[] params = Arrays.copyOfRange(PARAMETER_TYPES, 0, parameterCount);
            try {
                constructor = ParquetWriter.class.getConstructor(params);
            } catch (Exception e) {
                LOG.trace(MessageFormat.format(
                        "Mismatch Parquet library version: {0} {1}", //$NON-NLS-1$
                        name(),
                        Arrays.toString(params)), e);
                return null;
            }
            return constructor;
        }

        static ParquetVersion getSupportedVersion() {
            for (ParquetVersion version : values()) {
                if (version.resolve() != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format(
                                "Detected Parquet library version: {0}", //$NON-NLS-1$
                                version));
                    }
                    return version;
                }
            }
            throw new IllegalStateException();
        }

        @SuppressWarnings("unchecked")
        <T> ParquetWriter<T> newInstance(
                Path path,
                WriteSupport<T> writeSupport,
                ParquetFileOutput.Options options,
                Configuration configuration) throws IOException {
            Object[] argumentsCandidate = {
                    path,
                    writeSupport,
                    options.getCompressionCodecName(),
                    options.getBlockSize(),
                    options.getDataPageSize(),
                    options.getDictionaryPageSize(),
                    options.isEnableDictionary(),
                    options.isEnableValidation(),
                    options.getWriterVersion(),
                    configuration,
            };
            Object[] arguments = Arrays.copyOfRange(argumentsCandidate, 0, parameterCount);
            try {
                return (ParquetWriter<T>) resolve().newInstance(arguments);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                } else if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof Error) {
                    throw (Error) cause;
                } else {
                    throw new IllegalStateException(e);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * The parquet file writing options.
     * @since 0.7.0
     */
    public static final class Options {

        private CompressionCodecName compressionCodecName = CompressionCodecName.SNAPPY;

        private int blockSize = ParquetWriter.DEFAULT_BLOCK_SIZE;

        private int dataPageSize = ParquetWriter.DEFAULT_PAGE_SIZE;

        private int dictionaryPageSize = ParquetWriter.DEFAULT_PAGE_SIZE;

        private boolean enableDictionary = true;

        private boolean enableValidation = false;

        private WriterVersion writerVersion = WriterVersion.PARQUET_1_0;

        /**
         * Returns the compression codec name.
         * @return the compression codec name
         */
        public CompressionCodecName getCompressionCodecName() {
            return compressionCodecName;
        }

        /**
         * Sets the compression codec name.
         * @param value the value
         */
        public void setCompressionCodecName(CompressionCodecName value) {
            this.compressionCodecName = value;
        }

        /**
         * Returns the block size (in bytes).
         * @return the block size
         */
        public int getBlockSize() {
            return blockSize;
        }

        /**
         * Sets the block size (in bytes).
         * @param value the value
         */
        public void setBlockSize(int value) {
            this.blockSize = value;
        }

        /**
         * Returns the data page size (in bytes).
         * @return the data page size
         */
        public int getDataPageSize() {
            return dataPageSize;
        }

        /**
         * Sets the data page size (in bytes).
         * @param value the value
         */
        public void setDataPageSize(int value) {
            this.dataPageSize = value;
        }

        /**
         * Returns the dictionary page size (in bytes).
         * @return the dictionary page size
         */
        public int getDictionaryPageSize() {
            return dictionaryPageSize;
        }

        /**
         * Sets the dictionary page size (in bytes).
         * @param value the value
         */
        public void setDictionaryPageSize(int value) {
            this.dictionaryPageSize = value;
        }

        /**
         * Returns whether the dictionary is enabled or not.
         * @return {@code true} if enabled, otherwise {@code false}
         */
        public boolean isEnableDictionary() {
            return enableDictionary;
        }

        /**
         * Sets whether the dictionary is enabled or not.
         * @param value the value
         */
        public void setEnableDictionary(boolean value) {
            this.enableDictionary = value;
        }

        /**
         * Returns whether the schema validation is enabled or not.
         * @return {@code true} if enabled, otherwise {@code false}
         */
        public boolean isEnableValidation() {
            return enableValidation;
        }

        /**
         * Sets whether the schema validation is enabled or not.
         * @param value the value
         */
        public void setEnableValidation(boolean value) {
            this.enableValidation = value;
        }

        /**
         * Returns the writer version.
         * @return the writer version
         */
        public WriterVersion getWriterVersion() {
            return writerVersion;
        }

        /**
         * Sets the writer version.
         * @param value the value
         */
        public void setWriterVersion(WriterVersion value) {
            this.writerVersion = value;
        }
    }
}
