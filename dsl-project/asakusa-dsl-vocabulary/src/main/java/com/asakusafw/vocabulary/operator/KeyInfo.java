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
package com.asakusafw.vocabulary.operator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.asakusafw.vocabulary.model.Key;

/**
 * A meta-data for each operator input which has {@code Key} information.
 * Clients should not be use this annotation directly.
 * @since 0.5.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KeyInfo {

    /**
     * Grouping strategy.
     * @see Key#group()
     */
    Group[] group();

    /**
     * Ordering strategy.
     * @see Key#order()
     */
    Order[] order();

    /**
     * Represents an grouping strategy element.
     * @since 0.5.0
     */
    @Target({ })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Group {

        /**
         * The grouping expression.
         */
        String expression();
    }

    /**
     * Represents an ordering strategy element.
     * @since 0.5.0
     */
    @Target({ })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Order {

        /**
         * The ordering direction.
         */
        Direction direction();

        /**
         * The ordering expression.
         */
        String expression();
    }

    /**
     * Ordering direction.
     * @since 0.5.0
     */
    enum Direction {

        /**
         * Ascendant.
         */
        ASC,

        /**
         * Descendant.
         */
        DESC,
    }
}
