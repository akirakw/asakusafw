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
package com.asakusafw.runtime.stage.launcher;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities for this package.
 */
final class Util {

    static final Log LOG = LogFactory.getLog(Util.class);

    private Util() {
        return;
    }

    static void closeQuiet(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                LOG.warn(MessageFormat.format(
                        "Exception occurred while closing: {0}",
                        object), e);
            }
        }
    }

    static boolean delete(File file) {
        if (file.exists() == false) {
            return true;
        } else if (file.isDirectory()) {
            boolean deleteChildren = true;
            for (File child : list(file)) {
                deleteChildren &= delete(child);
            }
            if (deleteChildren == false) {
                return false;
            }
        }
        if (file.delete()) {
            return true;
        } else {
            LOG.warn(MessageFormat.format(
                    "Failed to delete a file: {0}",
                    file));
            return false;
        }
    }

    private static List<File> list(File file) {
        return Optional.ofNullable(file.listFiles())
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}
