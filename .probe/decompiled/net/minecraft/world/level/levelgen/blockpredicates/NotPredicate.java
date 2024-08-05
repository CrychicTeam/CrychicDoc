package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

class NotPredicate implements BlockPredicate {

    public static final Codec<NotPredicate> CODEC = RecordCodecBuilder.create(p_190515_ -> p_190515_.group(BlockPredicate.CODEC.fieldOf("predicate").forGetter(p_190517_ -> p_190517_.predicate)).apply(p_190515_, NotPredicate::new));

    private final BlockPredicate predicate;

    public NotPredicate(BlockPredicate blockPredicate0) {
        this.predicate = blockPredicate0;
    }

    public boolean test(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        return !this.predicate.test(worldGenLevel0, blockPos1);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.NOT;
    }
}