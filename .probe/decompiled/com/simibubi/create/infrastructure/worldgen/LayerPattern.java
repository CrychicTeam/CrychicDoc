package com.simibubi.create.infrastructure.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.util.NonNullConsumer;

public class LayerPattern {

    public static final Codec<LayerPattern> CODEC = Codec.list(LayerPattern.Layer.CODEC).xmap(LayerPattern::new, pattern -> pattern.layers);

    public final List<LayerPattern.Layer> layers;

    public LayerPattern(List<LayerPattern.Layer> layers) {
        this.layers = layers;
    }

    public LayerPattern.Layer rollNext(@Nullable LayerPattern.Layer previous, RandomSource random) {
        int totalWeight = 0;
        for (LayerPattern.Layer layer : this.layers) {
            if (layer != previous) {
                totalWeight += layer.weight;
            }
        }
        int rolled = random.nextInt(totalWeight);
        for (LayerPattern.Layer layerx : this.layers) {
            if (layerx != previous) {
                rolled -= layerx.weight;
                if (rolled < 0) {
                    return layerx;
                }
            }
        }
        return null;
    }

    public static LayerPattern.Builder builder() {
        return new LayerPattern.Builder();
    }

    public static class Builder {

        private final List<LayerPattern.Layer> layers = new ArrayList();

        private boolean netherMode;

        public LayerPattern.Builder inNether() {
            this.netherMode = true;
            return this;
        }

        public LayerPattern.Builder layer(NonNullConsumer<LayerPattern.Layer.Builder> builder) {
            LayerPattern.Layer.Builder layerBuilder = new LayerPattern.Layer.Builder();
            layerBuilder.netherMode = this.netherMode;
            builder.accept(layerBuilder);
            this.layers.add(layerBuilder.build());
            return this;
        }

        public LayerPattern build() {
            return new LayerPattern(this.layers);
        }
    }

    public static class Layer {

        public static final Codec<LayerPattern.Layer> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(Codec.list(OreConfiguration.TargetBlockState.CODEC)).fieldOf("targets").forGetter(layer -> layer.targets), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("min_size").forGetter(layer -> layer.minSize), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("max_size").forGetter(layer -> layer.maxSize), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("weight").forGetter(layer -> layer.weight)).apply(instance, LayerPattern.Layer::new));

        public final List<List<OreConfiguration.TargetBlockState>> targets;

        public final int minSize;

        public final int maxSize;

        public final int weight;

        public Layer(List<List<OreConfiguration.TargetBlockState>> targets, int minSize, int maxSize, int weight) {
            this.targets = targets;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.weight = weight;
        }

        public List<OreConfiguration.TargetBlockState> rollBlock(RandomSource random) {
            return this.targets.size() == 1 ? (List) this.targets.get(0) : (List) this.targets.get(random.nextInt(this.targets.size()));
        }

        public static class Builder {

            private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

            private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

            private static final RuleTest NETHER_ORE_REPLACEABLES = new TagMatchTest(BlockTags.BASE_STONE_NETHER);

            private final List<List<OreConfiguration.TargetBlockState>> targets = new ArrayList();

            private int minSize = 1;

            private int maxSize = 1;

            private int weight = 1;

            private boolean netherMode;

            public LayerPattern.Layer.Builder block(NonNullSupplier<? extends Block> block) {
                return this.block((Block) block.get());
            }

            public LayerPattern.Layer.Builder passiveBlock() {
                return this.blocks(Blocks.STONE.defaultBlockState(), Blocks.DEEPSLATE.defaultBlockState());
            }

            public LayerPattern.Layer.Builder block(Block block) {
                if (this.netherMode) {
                    this.targets.add(ImmutableList.of(OreConfiguration.target(NETHER_ORE_REPLACEABLES, block.defaultBlockState())));
                    return this;
                } else {
                    return this.blocks(block.defaultBlockState(), block.defaultBlockState());
                }
            }

            public LayerPattern.Layer.Builder blocks(Block block, Block deepblock) {
                return this.blocks(block.defaultBlockState(), deepblock.defaultBlockState());
            }

            public LayerPattern.Layer.Builder blocks(Couple<NonNullSupplier<? extends Block>> blocksByDepth) {
                return this.blocks(((Block) blocksByDepth.getFirst().get()).defaultBlockState(), ((Block) blocksByDepth.getSecond().get()).defaultBlockState());
            }

            private LayerPattern.Layer.Builder blocks(BlockState stone, BlockState deepslate) {
                this.targets.add(ImmutableList.of(OreConfiguration.target(STONE_ORE_REPLACEABLES, stone), OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, deepslate)));
                return this;
            }

            public LayerPattern.Layer.Builder weight(int weight) {
                this.weight = weight;
                return this;
            }

            public LayerPattern.Layer.Builder size(int min, int max) {
                this.minSize = min;
                this.maxSize = max;
                return this;
            }

            public LayerPattern.Layer build() {
                return new LayerPattern.Layer(this.targets, this.minSize, this.maxSize, this.weight);
            }
        }
    }
}