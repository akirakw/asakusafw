/**
 * Copyright 2011 Asakusa Framework Team.
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
package com.asakusafw.dmdl.java.emitter.driver;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.asakusafw.dmdl.java.emitter.EmitContext;
import com.asakusafw.dmdl.java.spi.JavaDataModelDriver;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;
import com.ashigeru.lang.java.model.syntax.Annotation;
import com.ashigeru.lang.java.model.syntax.Expression;
import com.ashigeru.lang.java.model.syntax.MethodDeclaration;
import com.ashigeru.lang.java.model.syntax.ModelFactory;
import com.ashigeru.lang.java.model.syntax.Type;
import com.ashigeru.lang.java.model.util.AttributeBuilder;
import com.ashigeru.lang.java.model.util.Models;

/**
 * Attaches a <code>&#64;Generated</code> annotation.
 * @since 0.2.0
 */
public class GeneratorInfoDriver implements JavaDataModelDriver {

    private static final String GENERATOR_IDENTIFIER = "DMDL";

    @Override
    public List<Type> getInterfaces(EmitContext context, ModelDeclaration model) {
        return Collections.emptyList();
    }

    @Override
    public List<MethodDeclaration> getMethods(EmitContext context, ModelDeclaration model) {
        return Collections.emptyList();
    }

    @Override
    public List<Annotation> getTypeAnnotations(EmitContext context, ModelDeclaration model) {
        ModelFactory f = context.getModelFactory();
        return new AttributeBuilder(f)
            .annotation(context.resolve(Generated.class),
                "value", f.newArrayInitializer(new Expression[] {
                        Models.toLiteral(f, GENERATOR_IDENTIFIER),
                }))
            .toAnnotations();
    }

    @Override
    public List<Annotation> getMemberAnnotations(EmitContext context, PropertyDeclaration property) {
        return Collections.emptyList();
    }
}