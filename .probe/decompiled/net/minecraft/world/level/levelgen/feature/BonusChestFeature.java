package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.stream.IntStream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class BonusChestFeature extends Feature<NoneFeatureConfiguration> {

    public BonusChestFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        RandomSource $$1 = featurePlaceContextNoneFeatureConfiguration0.random();
        WorldGenLevel $$2 = featurePlaceContextNoneFeatureConfiguration0.level();
        ChunkPos $$3 = new ChunkPos(featurePlaceContextNoneFeatureConfiguration0.origin());
        IntArrayList $$4 = Util.toShuffledList(IntStream.rangeClosed($$3.getMinBlockX(), $$3.getMaxBlockX()), $$1);
        IntArrayList $$5 = Util.toShuffledList(IntStream.rangeClosed($$3.getMinBlockZ(), $$3.getMaxBlockZ()), $$1);
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        IntListIterator var8 = $$4.iterator();
        while (var8.hasNext()) {
            Integer $$7 = (Integer) var8.next();
            IntListIterator var10 = $$5.iterator();
            while (var10.hasNext()) {
                Integer $$8 = (Integer) var10.next();
                $$6.set($$7, 0, $$8);
                BlockPos $$9 = $$2.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, $$6);
                if ($$2.m_46859_($$9) || $$2.m_8055_($$9).m_60812_($$2, $$9).isEmpty()) {
                    $$2.m_7731_($$9, Blocks.CHEST.defaultBlockState(), 2);
                    RandomizableContainerBlockEntity.setLootTable($$2, $$1, $$9, BuiltInLootTables.SPAWN_BONUS_CHEST);
                    BlockState $$10 = Blocks.TORCH.defaultBlockState();
                    for (Direction $$11 : Direction.Plane.HORIZONTAL) {
                        BlockPos $$12 = $$9.relative($$11);
                        if ($$10.m_60710_($$2, $$12)) {
                            $$2.m_7731_($$12, $$10, 2);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}