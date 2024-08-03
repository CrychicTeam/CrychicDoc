package net.minecraft.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public class TreeConfiguration implements FeatureConfiguration {

    public static final Codec<TreeConfiguration> CODEC = RecordCodecBuilder.create(p_225468_ -> p_225468_.group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(p_161248_ -> p_161248_.trunkProvider), TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(p_161246_ -> p_161246_.trunkPlacer), BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(p_161244_ -> p_161244_.foliageProvider), FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(p_191357_ -> p_191357_.foliagePlacer), RootPlacer.CODEC.optionalFieldOf("root_placer").forGetter(p_225478_ -> p_225478_.rootPlacer), BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter(p_225476_ -> p_225476_.dirtProvider), FeatureSize.CODEC.fieldOf("minimum_size").forGetter(p_225474_ -> p_225474_.minimumSize), TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(p_225472_ -> p_225472_.decorators), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter(p_161232_ -> p_161232_.ignoreVines), Codec.BOOL.fieldOf("force_dirt").orElse(false).forGetter(p_225470_ -> p_225470_.forceDirt)).apply(p_225468_, TreeConfiguration::new));

    public final BlockStateProvider trunkProvider;

    public final BlockStateProvider dirtProvider;

    public final TrunkPlacer trunkPlacer;

    public final BlockStateProvider foliageProvider;

    public final FoliagePlacer foliagePlacer;

    public final Optional<RootPlacer> rootPlacer;

    public final FeatureSize minimumSize;

    public final List<TreeDecorator> decorators;

    public final boolean ignoreVines;

    public final boolean forceDirt;

    protected TreeConfiguration(BlockStateProvider blockStateProvider0, TrunkPlacer trunkPlacer1, BlockStateProvider blockStateProvider2, FoliagePlacer foliagePlacer3, Optional<RootPlacer> optionalRootPlacer4, BlockStateProvider blockStateProvider5, FeatureSize featureSize6, List<TreeDecorator> listTreeDecorator7, boolean boolean8, boolean boolean9) {
        this.trunkProvider = blockStateProvider0;
        this.trunkPlacer = trunkPlacer1;
        this.foliageProvider = blockStateProvider2;
        this.foliagePlacer = foliagePlacer3;
        this.rootPlacer = optionalRootPlacer4;
        this.dirtProvider = blockStateProvider5;
        this.minimumSize = featureSize6;
        this.decorators = listTreeDecorator7;
        this.ignoreVines = boolean8;
        this.forceDirt = boolean9;
    }

    public static class TreeConfigurationBuilder {

        public final BlockStateProvider trunkProvider;

        private final TrunkPlacer trunkPlacer;

        public final BlockStateProvider foliageProvider;

        private final FoliagePlacer foliagePlacer;

        private final Optional<RootPlacer> rootPlacer;

        private BlockStateProvider dirtProvider;

        private final FeatureSize minimumSize;

        private List<TreeDecorator> decorators = ImmutableList.of();

        private boolean ignoreVines;

        private boolean forceDirt;

        public TreeConfigurationBuilder(BlockStateProvider blockStateProvider0, TrunkPlacer trunkPlacer1, BlockStateProvider blockStateProvider2, FoliagePlacer foliagePlacer3, Optional<RootPlacer> optionalRootPlacer4, FeatureSize featureSize5) {
            this.trunkProvider = blockStateProvider0;
            this.trunkPlacer = trunkPlacer1;
            this.foliageProvider = blockStateProvider2;
            this.dirtProvider = BlockStateProvider.simple(Blocks.DIRT);
            this.foliagePlacer = foliagePlacer3;
            this.rootPlacer = optionalRootPlacer4;
            this.minimumSize = featureSize5;
        }

        public TreeConfigurationBuilder(BlockStateProvider blockStateProvider0, TrunkPlacer trunkPlacer1, BlockStateProvider blockStateProvider2, FoliagePlacer foliagePlacer3, FeatureSize featureSize4) {
            this(blockStateProvider0, trunkPlacer1, blockStateProvider2, foliagePlacer3, Optional.empty(), featureSize4);
        }

        public TreeConfiguration.TreeConfigurationBuilder dirt(BlockStateProvider blockStateProvider0) {
            this.dirtProvider = blockStateProvider0;
            return this;
        }

        public TreeConfiguration.TreeConfigurationBuilder decorators(List<TreeDecorator> listTreeDecorator0) {
            this.decorators = listTreeDecorator0;
            return this;
        }

        public TreeConfiguration.TreeConfigurationBuilder ignoreVines() {
            this.ignoreVines = true;
            return this;
        }

        public TreeConfiguration.TreeConfigurationBuilder forceDirt() {
            this.forceDirt = true;
            return this;
        }

        public TreeConfiguration build() {
            return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
        }
    }
}