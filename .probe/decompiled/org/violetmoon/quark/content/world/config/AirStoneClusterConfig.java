package org.violetmoon.quark.content.world.config;

import org.violetmoon.zeta.config.Config;

public class AirStoneClusterConfig extends BigStoneClusterConfig {

    @Config
    public boolean generateInAir;

    public AirStoneClusterConfig(AirStoneClusterConfig.Builder<? extends AirStoneClusterConfig.Builder<?>> builder) {
        super(builder);
        this.generateInAir = builder.generateInAir;
    }

    public static <B extends AirStoneClusterConfig.Builder<B>> AirStoneClusterConfig.Builder<B> airStoneBuilder() {
        return new AirStoneClusterConfig.Builder<>();
    }

    public static class Builder<B extends AirStoneClusterConfig.Builder<B>> extends BigStoneClusterConfig.Builder<B> {

        boolean generateInAir = true;

        public B generateInAir(boolean generateInAir) {
            this.generateInAir = generateInAir;
            return this.downcast();
        }

        public AirStoneClusterConfig build() {
            return new AirStoneClusterConfig(this);
        }
    }
}