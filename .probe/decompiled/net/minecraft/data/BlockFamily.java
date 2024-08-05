package net.minecraft.data;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class BlockFamily {

    private final Block baseBlock;

    final Map<BlockFamily.Variant, Block> variants = Maps.newHashMap();

    FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    boolean generateModel = true;

    boolean generateRecipe = true;

    @Nullable
    String recipeGroupPrefix;

    @Nullable
    String recipeUnlockedBy;

    BlockFamily(Block block0) {
        this.baseBlock = block0;
    }

    public Block getBaseBlock() {
        return this.baseBlock;
    }

    public Map<BlockFamily.Variant, Block> getVariants() {
        return this.variants;
    }

    public Block get(BlockFamily.Variant blockFamilyVariant0) {
        return (Block) this.variants.get(blockFamilyVariant0);
    }

    public boolean shouldGenerateModel() {
        return this.generateModel;
    }

    public boolean shouldGenerateRecipe(FeatureFlagSet featureFlagSet0) {
        return this.generateRecipe && this.requiredFeatures.isSubsetOf(featureFlagSet0);
    }

    public Optional<String> getRecipeGroupPrefix() {
        return Util.isBlank(this.recipeGroupPrefix) ? Optional.empty() : Optional.of(this.recipeGroupPrefix);
    }

    public Optional<String> getRecipeUnlockedBy() {
        return Util.isBlank(this.recipeUnlockedBy) ? Optional.empty() : Optional.of(this.recipeUnlockedBy);
    }

    public static class Builder {

        private final BlockFamily family;

        public Builder(Block block0) {
            this.family = new BlockFamily(block0);
        }

        public BlockFamily getFamily() {
            return this.family;
        }

        public BlockFamily.Builder button(Block block0) {
            this.family.variants.put(BlockFamily.Variant.BUTTON, block0);
            return this;
        }

        public BlockFamily.Builder chiseled(Block block0) {
            this.family.variants.put(BlockFamily.Variant.CHISELED, block0);
            return this;
        }

        public BlockFamily.Builder mosaic(Block block0) {
            this.family.variants.put(BlockFamily.Variant.MOSAIC, block0);
            return this;
        }

        public BlockFamily.Builder cracked(Block block0) {
            this.family.variants.put(BlockFamily.Variant.CRACKED, block0);
            return this;
        }

        public BlockFamily.Builder cut(Block block0) {
            this.family.variants.put(BlockFamily.Variant.CUT, block0);
            return this;
        }

        public BlockFamily.Builder door(Block block0) {
            this.family.variants.put(BlockFamily.Variant.DOOR, block0);
            return this;
        }

        public BlockFamily.Builder customFence(Block block0) {
            this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE, block0);
            return this;
        }

        public BlockFamily.Builder fence(Block block0) {
            this.family.variants.put(BlockFamily.Variant.FENCE, block0);
            return this;
        }

        public BlockFamily.Builder customFenceGate(Block block0) {
            this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE_GATE, block0);
            return this;
        }

        public BlockFamily.Builder fenceGate(Block block0) {
            this.family.variants.put(BlockFamily.Variant.FENCE_GATE, block0);
            return this;
        }

        public BlockFamily.Builder sign(Block block0, Block block1) {
            this.family.variants.put(BlockFamily.Variant.SIGN, block0);
            this.family.variants.put(BlockFamily.Variant.WALL_SIGN, block1);
            return this;
        }

        public BlockFamily.Builder slab(Block block0) {
            this.family.variants.put(BlockFamily.Variant.SLAB, block0);
            return this;
        }

        public BlockFamily.Builder stairs(Block block0) {
            this.family.variants.put(BlockFamily.Variant.STAIRS, block0);
            return this;
        }

        public BlockFamily.Builder pressurePlate(Block block0) {
            this.family.variants.put(BlockFamily.Variant.PRESSURE_PLATE, block0);
            return this;
        }

        public BlockFamily.Builder polished(Block block0) {
            this.family.variants.put(BlockFamily.Variant.POLISHED, block0);
            return this;
        }

        public BlockFamily.Builder trapdoor(Block block0) {
            this.family.variants.put(BlockFamily.Variant.TRAPDOOR, block0);
            return this;
        }

        public BlockFamily.Builder wall(Block block0) {
            this.family.variants.put(BlockFamily.Variant.WALL, block0);
            return this;
        }

        public BlockFamily.Builder dontGenerateModel() {
            this.family.generateModel = false;
            return this;
        }

        public BlockFamily.Builder dontGenerateRecipe() {
            this.family.generateRecipe = false;
            return this;
        }

        public BlockFamily.Builder featureLockedBehind(FeatureFlag... featureFlag0) {
            this.family.requiredFeatures = FeatureFlags.REGISTRY.subset(featureFlag0);
            return this;
        }

        public BlockFamily.Builder recipeGroupPrefix(String string0) {
            this.family.recipeGroupPrefix = string0;
            return this;
        }

        public BlockFamily.Builder recipeUnlockedBy(String string0) {
            this.family.recipeUnlockedBy = string0;
            return this;
        }
    }

    public static enum Variant {

        BUTTON("button"),
        CHISELED("chiseled"),
        CRACKED("cracked"),
        CUT("cut"),
        DOOR("door"),
        CUSTOM_FENCE("custom_fence"),
        FENCE("fence"),
        CUSTOM_FENCE_GATE("custom_fence_gate"),
        FENCE_GATE("fence_gate"),
        MOSAIC("mosaic"),
        SIGN("sign"),
        SLAB("slab"),
        STAIRS("stairs"),
        PRESSURE_PLATE("pressure_plate"),
        POLISHED("polished"),
        TRAPDOOR("trapdoor"),
        WALL("wall"),
        WALL_SIGN("wall_sign");

        private final String name;

        private Variant(String p_176019_) {
            this.name = p_176019_;
        }

        public String getName() {
            return this.name;
        }
    }
}