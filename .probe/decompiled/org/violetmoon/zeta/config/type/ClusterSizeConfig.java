package org.violetmoon.zeta.config.type;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.violetmoon.zeta.config.Config;

public class ClusterSizeConfig implements IConfigType {

    @Config
    public DimensionConfig dimensions;

    @Config
    public IBiomeConfig biomes;

    @Config
    @Config.Min(0.0)
    public int rarity;

    @Config
    public int minYLevel;

    @Config
    public int maxYLevel;

    @Config
    @Config.Min(0.0)
    public int horizontalSize;

    @Config
    @Config.Min(0.0)
    public int verticalSize;

    @Config
    @Config.Min(0.0)
    public int horizontalVariation;

    @Config
    @Config.Min(0.0)
    public int verticalVariation;

    public ClusterSizeConfig(ClusterSizeConfig.Builder<?> builder) {
        this.dimensions = builder.dimensions;
        this.biomes = builder.biomes;
        this.rarity = builder.rarity;
        this.minYLevel = builder.minYLevel;
        this.maxYLevel = builder.maxYLevel;
        this.horizontalSize = builder.horizontalSize;
        this.verticalSize = builder.verticalSize;
        this.horizontalVariation = builder.horizontalVariation;
        this.verticalVariation = builder.verticalVariation;
    }

    public static <B extends ClusterSizeConfig.Builder<B>> ClusterSizeConfig.Builder<B> builder() {
        return new ClusterSizeConfig.Builder<>();
    }

    public static class Builder<B extends ClusterSizeConfig.Builder<B>> {

        protected DimensionConfig dimensions = DimensionConfig.overworld(false);

        protected IBiomeConfig biomes;

        protected int rarity;

        protected int minYLevel = 0;

        protected int maxYLevel = 64;

        protected int horizontalSize;

        protected int verticalSize;

        protected int horizontalVariation;

        protected int verticalVariation;

        public ClusterSizeConfig build() {
            return new ClusterSizeConfig(this);
        }

        public B dimensions(DimensionConfig dimensions) {
            this.dimensions = dimensions;
            return this.downcast();
        }

        public B biomes(IBiomeConfig biomes) {
            this.biomes = biomes;
            return this.downcast();
        }

        @SafeVarargs
        public final B biomeAllow(TagKey<Biome>... tags) {
            this.biomes = CompoundBiomeConfig.fromBiomeTags(false, tags);
            return this.downcast();
        }

        @SafeVarargs
        public final B biomeDeny(TagKey<Biome>... tags) {
            this.biomes = CompoundBiomeConfig.fromBiomeTags(true, tags);
            return this.downcast();
        }

        public B rarity(int rarity) {
            this.rarity = rarity;
            return this.downcast();
        }

        public B minYLevel(int minYLevel) {
            this.minYLevel = minYLevel;
            return this.downcast();
        }

        public B maxYLevel(int maxYLevel) {
            this.maxYLevel = maxYLevel;
            return this.downcast();
        }

        public B horizontalSize(int horizontalSize) {
            this.horizontalSize = horizontalSize;
            return this.downcast();
        }

        public B verticalSize(int verticalSize) {
            this.verticalSize = verticalSize;
            return this.downcast();
        }

        public B horizontalVariation(int horizontalVariation) {
            this.horizontalVariation = horizontalVariation;
            return this.downcast();
        }

        public B verticalVariation(int verticalVariation) {
            this.verticalVariation = verticalVariation;
            return this.downcast();
        }

        protected B downcast() {
            return (B) this;
        }
    }
}