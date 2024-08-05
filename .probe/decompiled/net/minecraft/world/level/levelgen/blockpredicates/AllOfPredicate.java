package net.minecraft.world.level.levelgen.blockpredicates;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

class AllOfPredicate extends CombiningPredicate {

    public static final Codec<AllOfPredicate> CODEC = m_190458_(AllOfPredicate::new);

    public AllOfPredicate(List<BlockPredicate> listBlockPredicate0) {
        super(listBlockPredicate0);
    }

    public boolean test(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        for (BlockPredicate $$2 : this.f_190453_) {
            if (!$$2.test(worldGenLevel0, blockPos1)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.ALL_OF;
    }
}