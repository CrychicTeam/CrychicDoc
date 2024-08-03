package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;

public class InsideWorldBoundsPredicate implements BlockPredicate {

    public static final Codec<InsideWorldBoundsPredicate> CODEC = RecordCodecBuilder.create(p_190473_ -> p_190473_.group(Vec3i.offsetCodec(16).optionalFieldOf("offset", BlockPos.ZERO).forGetter(p_190475_ -> p_190475_.offset)).apply(p_190473_, InsideWorldBoundsPredicate::new));

    private final Vec3i offset;

    public InsideWorldBoundsPredicate(Vec3i vecI0) {
        this.offset = vecI0;
    }

    public boolean test(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        return !worldGenLevel0.m_151570_(blockPos1.offset(this.offset));
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.INSIDE_WORLD_BOUNDS;
    }
}