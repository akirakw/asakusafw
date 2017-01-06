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
package com.asakusafw.utils.java.model.syntax;

/**
 * An interface which represents method reference expressions only for constructors.
 * <ul>
 *   <li> Specified In: <ul>
 *     <li> {@code [JLS8:15.13] Method Reference Expressions} </li>
 *   </ul> </li>
 * </ul>
 * @since 0.9.1
 */
public interface ConstructorReferenceExpression
        extends MethodOrConstructorReferenceExpression {

    /**
     * Returns the owner type.
     * @return the owner type
     */
    @Override
    Type getQualifier();
}
