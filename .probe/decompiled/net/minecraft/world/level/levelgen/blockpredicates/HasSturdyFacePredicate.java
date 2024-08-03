package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;

public class HasSturdyFacePredicate implements BlockPredicate {

    private final Vec3i offset;

    private final Direction direction;

    public static final Codec<HasSturdyFacePredicate> CODEC = RecordCodecBuilder.create(p_198327_ -> p_198327_.group(Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(p_198331_ -> p_198331_.offset), Direction.CODEC.fieldOf("direction").forGetter(p_198329_ -> p_198329_.direction)).apply(p_198327_, HasSturdyFacePredicate::new));

    public HasSturdyFacePredicate(Vec3i vecI0, Direction direction1) {
        this.offset = vecI0;
        this.direction = direction1;
    }

    public boolean test(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        BlockPos $$2 = blockPos1.offset(this.offset);
        return worldGenLevel0.m_8055_($$2).m_60783_(worldGenLevel0, $$2, this.direction);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.HAS_STURDY_FACE;
    }
}