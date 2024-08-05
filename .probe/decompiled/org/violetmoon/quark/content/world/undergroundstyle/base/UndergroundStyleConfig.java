package org.violetmoon.quark.content.world.undergroundstyle.base;

import org.violetmoon.zeta.config.type.ClusterSizeConfig;

public class UndergroundStyleConfig extends ClusterSizeConfig {

    public final UndergroundStyle style;

    public UndergroundStyleConfig(UndergroundStyleConfig.Builder<?> builder) {
        super(builder);
        this.style = builder.style;
    }

    public static <B extends UndergroundStyleConfig.Builder<B>> UndergroundStyleConfig.Builder<B> styleBuilder() {
        return new UndergroundStyleConfig.Builder<>();
    }

    public static class Builder<B extends UndergroundStyleConfig.Builder<B>> extends ClusterSizeConfig.Builder<B> {

        protected UndergroundStyle style;

        public UndergroundStyleConfig.Builder<B> style(UndergroundStyle style) {
            this.style = style;
            return this;
        }

        public UndergroundStyleConfig build() {
            return new UndergroundStyleConfig(this);
        }
    }
}