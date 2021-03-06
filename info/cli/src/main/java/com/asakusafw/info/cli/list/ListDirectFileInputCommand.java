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
package com.asakusafw.info.cli.list;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asakusafw.info.JobflowInfo;
import com.asakusafw.info.cli.common.JobflowInfoParameter;
import com.asakusafw.info.directio.DirectFileInputInfo;
import com.asakusafw.info.directio.DirectFileIoAttribute;
import com.asakusafw.utils.jcommander.common.HelpParameter;
import com.asakusafw.utils.jcommander.common.OutputParameter;
import com.asakusafw.utils.jcommander.common.VerboseParameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

/**
 * A command for printing list of direct file inputs.
 * @since 0.10.0
 */
@Parameters(
        commandNames = "input",
        commandDescriptionKey = "command.generate-list-directio-input",
        resourceBundle = "com.asakusafw.info.cli.jcommander"
)
public class ListDirectFileInputCommand implements Runnable {

    static final Logger LOG = LoggerFactory.getLogger(ListDirectFileInputCommand.class);

    @ParametersDelegate
    final HelpParameter helpParameter = new HelpParameter();

    @ParametersDelegate
    final JobflowInfoParameter jobflowInfoParameter = new JobflowInfoParameter();

    @ParametersDelegate
    final VerboseParameter verboseParameter = new VerboseParameter();

    @ParametersDelegate
    final OutputParameter outputParameter = new OutputParameter();

    @Override
    public void run() {
        LOG.debug("starting {}", getClass().getSimpleName());
        try (PrintWriter writer = outputParameter.open()) {
            List<JobflowInfo> jobflows = jobflowInfoParameter.getJobflows();
            jobflows.stream()
                    .flatMap(jobflow -> jobflow.getAttributes().stream())
                    .filter(it -> it instanceof DirectFileIoAttribute)
                    .map(it -> (DirectFileIoAttribute) it)
                    .flatMap(it -> it.getInputs().stream())
                    .sorted(Comparator
                            .comparing(DirectFileInputInfo::getBasePath)
                            .thenComparing(DirectFileInputInfo::getResourcePattern))
                    .forEachOrdered(info -> {
                        if (verboseParameter.isRequired()) {
                            Map<String, Object> members = new LinkedHashMap<>();
                            members.put("base-path", info.getBasePath());
                            members.put("resource-pattern", info.getResourcePattern());
                            members.put("data-type", info.getDataType());
                            members.put("filter-class", info.getFilterClass());
                            members.put("format-class", info.getFormatClass());
                            members.put("optional", info.isOptional());
                            writer.printf("%s:%n", info.getDescriptionClass());
                            ListUtil.printBlock(writer, 4, members);
                        } else {
                            writer.printf("%s::%s%n", info.getBasePath(), info.getResourcePattern());
                        }
                    });
        }
    }
}
