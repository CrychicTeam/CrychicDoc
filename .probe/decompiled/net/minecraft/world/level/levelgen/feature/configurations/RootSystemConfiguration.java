package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class RootSystemConfiguration implements FeatureConfiguration {

    public static final Codec<RootSystemConfiguration> CODEC = RecordCodecBuilder.create(p_198371_ -> p_198371_.group(PlacedFeature.CODEC.fieldOf("feature").forGetter(p_204840_ -> p_204840_.treeFeature), Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(p_161151_ -> p_161151_.requiredVerticalSpaceForTree), Codec.intRange(1, 64).fieldOf("root_radius").forGetter(p_161149_ -> p_161149_.rootRadius), TagKey.hashedCodec(Registries.BLOCK).fieldOf("root_replaceable").forGetter(p_204838_ -> p_204838_.rootReplaceable), BlockStateProvider.CODEC.fieldOf("root_state_provider").forGetter(p_161145_ -> p_161145_.rootStateProvider), Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(p_161143_ -> p_161143_.rootPlacementAttempts), Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(p_161141_ -> p_161141_.rootColumnMaxHeight), Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(p_161139_ -> p_161139_.hangingRootRadius), Codec.intRange(0, 16).fieldOf("hanging_roots_vertical_span").forGetter(p_161137_ -> p_161137_.hangingRootsVerticalSpan), BlockStateProvider.CODEC.fieldOf("hanging_root_state_provider").forGetter(p_161135_ -> p_161135_.hangingRootStateProvider), Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(p_161133_ -> p_161133_.hangingRootPlacementAttempts), Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(p_161131_ -> p_161131_.allowedVerticalWaterForTree), BlockPredicate.CODEC.fieldOf("allowed_tree_position").forGetter(p_198373_ -> p_198373_.allowedTreePosition)).apply(p_198371_, RootSystemConfiguration::new));

    public final Holder<PlacedFeature> treeFeature;

    public final int requiredVerticalSpaceForTree;

    public final int rootRadius;

    public final TagKey<Block> rootReplaceable;

    public final BlockStateProvider rootStateProvider;

    public final int rootPlacementAttempts;

    public final int rootColumnMaxHeight;

    public final int hangingRootRadius;

    public final int hangingRootsVerticalSpan;

    public final BlockStateProvider hangingRootStateProvider;

    public final int hangingRootPlacementAttempts;

    public final int allowedVerticalWaterForTree;

    public final BlockPredicate allowedTreePosition;

    public RootSystemConfiguration(Holder<PlacedFeature> holderPlacedFeature0, int int1, int int2, TagKey<Block> tagKeyBlock3, BlockStateProvider blockStateProvider4, int int5, int int6, int int7, int int8, BlockStateProvider blockStateProvider9, int int10, int int11, BlockPredicate blockPredicate12) {
        this.treeFeature = holderPlacedFeature0;
        this.requiredVerticalSpaceForTree = int1;
        this.rootRadius = int2;
        this.rootReplaceable = tagKeyBlock3;
        this.rootStateProvider = blockStateProvider4;
        this.rootPlacementAttempts = int5;
        this.rootColumnMaxHeight = int6;
        this.hangingRootRadius = int7;
        this.hangingRootsVerticalSpan = int8;
        this.hangingRootStateProvider = blockStateProvider9;
        this.hangingRootPlacementAttempts = int10;
        this.allowedVerticalWaterForTree = int11;
        this.allowedTreePosition = blockPredicate12;
    }
}