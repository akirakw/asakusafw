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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asakusafw.dmdl.java.emitter.EmitContext;
import com.asakusafw.dmdl.java.spi.JavaDataModelDriver;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.dmdl.semantics.ModelSymbol;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;
import com.asakusafw.dmdl.semantics.trait.ProjectionsTrait;
import com.ashigeru.lang.java.model.syntax.Annotation;
import com.ashigeru.lang.java.model.syntax.MethodDeclaration;
import com.ashigeru.lang.java.model.syntax.Type;

/**
 * Enables models projectable.
 */
public class ProjectionDriver implements JavaDataModelDriver {

    @Override
    public List<Type> getInterfaces(EmitContext context, ModelDeclaration model) {
        ProjectionsTrait trait = model.getTrait(ProjectionsTrait.class);
        if (trait == null) {
            return Collections.emptyList();
        }
        List<Type> results = new ArrayList<Type>();
        for (ModelSymbol projection : trait.getProjections()) {
            results.add(context.resolve(projection));
        }
        return results;
    }

    @Override
    public List<MethodDeclaration> getMethods(EmitContext context, ModelDeclaration model) {
        return Collections.emptyList();
    }

    @Override
    public List<Annotation> getTypeAnnotations(EmitContext context, ModelDeclaration model) {
        return Collections.emptyList();
    }

    @Override
    public List<Annotation> getMemberAnnotations(EmitContext context, PropertyDeclaration property) {
        return Collections.emptyList();
    }
}
