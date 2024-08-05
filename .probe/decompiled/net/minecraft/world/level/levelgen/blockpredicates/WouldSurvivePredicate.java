package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class WouldSurvivePredicate implements BlockPredicate {

    public static final Codec<WouldSurvivePredicate> CODEC = RecordCodecBuilder.create(p_190577_ -> p_190577_.group(Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(p_190581_ -> p_190581_.offset), BlockState.CODEC.fieldOf("state").forGetter(p_190579_ -> p_190579_.state)).apply(p_190577_, WouldSurvivePredicate::new));

    private final Vec3i offset;

    private final BlockState state;

    protected WouldSurvivePredicate(Vec3i vecI0, BlockState blockState1) {
        this.offset = vecI0;
        this.state = blockState1;
    }

    public boolean test(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        return this.state.m_60710_(worldGenLevel0, blockPos1.offset(this.offset));
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.WOULD_SURVIVE;
    }
}