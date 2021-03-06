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
package com.asakusafw.runtime.io.text.value;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.asakusafw.runtime.io.text.TextUtil;
import com.asakusafw.runtime.io.text.driver.FieldAdapter;
import com.asakusafw.runtime.value.DecimalOption;

/**
 * An implementation of {@link FieldAdapter} which accepts {@link DecimalOption}.
 * @since 0.9.1
 */
public final class DecimalOptionFieldAdapter extends NumericOptionFieldAdapter<DecimalOption> {

    /**
     * The default {@link OutputStyle}.
     */
    public static final OutputStyle DEFAULT_OUTPUT_STYLE = OutputStyle.SCIENTIFIC;

    private final OutputStyle outputStyle;

    DecimalOptionFieldAdapter(String nullFormat, DecimalFormat decimalFormat, OutputStyle outputStyle) {
        super(nullFormat, decimalFormat);
        this.outputStyle = outputStyle;
    }

    /**
     * Returns a new builder.
     * @return the created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected Number get(DecimalOption property) {
        return property.get();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void set(BigDecimal value, DecimalOption property) {
        property.modify(value);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void doParseDefault(CharSequence contents, DecimalOption property) {
        property.modify(TextUtil.parseDecimal(contents, 0, contents.length()));
    }

    @Override
    protected void doEmitDefault(DecimalOption property, StringBuilder output) {
        switch (outputStyle) {
        case PLAIN:
            output.append(property.get().toPlainString());
            break;
        case ENGINEERING:
            output.append(property.get().toEngineeringString());
            break;
        case SCIENTIFIC:
            output.append(property.get().toString());
            break;
        default:
            throw new AssertionError(output);
        }
    }

    /**
     * A builder of {@link DecimalOptionFieldAdapter}.
     * @since 0.9.1
     */
    public static class Builder extends NumericBuilderBase<Builder, DecimalOptionFieldAdapter> {

        private OutputStyle outputStyle = DEFAULT_OUTPUT_STYLE;

        /**
         * Sets the output style.
         * @param newValue the output style
         * @return this
         */
        public Builder withOutputStyle(OutputStyle newValue) {
            this.outputStyle = newValue;
            return this;
        }

        @Override
        public DecimalOptionFieldAdapter build() {
            return new DecimalOptionFieldAdapter(getNullFormat(), getDecimalFormat(), outputStyle);
        }
    }

    /**
     * Represents an output style of {@link DecimalOption}.
     * @since 0.9.1
     */
    public enum OutputStyle {

        /**
         * Output without exponential notation.
         * @see BigDecimal#toPlainString()
         */
        PLAIN,

        /**
         * Output with scientific notation.
         * @see BigDecimal#toString()
         */
        SCIENTIFIC,

        /**
         * Output with engineering notation.
         * @see BigDecimal#toEngineeringString()
         */
        ENGINEERING,
    }
}
