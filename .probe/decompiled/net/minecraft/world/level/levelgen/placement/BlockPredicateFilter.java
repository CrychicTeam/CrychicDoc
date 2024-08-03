package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public class BlockPredicateFilter extends PlacementFilter {

    public static final Codec<BlockPredicateFilter> CODEC = RecordCodecBuilder.create(p_191575_ -> p_191575_.group(BlockPredicate.CODEC.fieldOf("predicate").forGetter(p_191579_ -> p_191579_.predicate)).apply(p_191575_, BlockPredicateFilter::new));

    private final BlockPredicate predicate;

    private BlockPredicateFilter(BlockPredicate blockPredicate0) {
        this.predicate = blockPredicate0;
    }

    public static BlockPredicateFilter forPredicate(BlockPredicate blockPredicate0) {
        return new BlockPredicateFilter(blockPredicate0);
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        return this.predicate.test(placementContext0.getLevel(), blockPos2);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.BLOCK_PREDICATE_FILTER;
    }
}