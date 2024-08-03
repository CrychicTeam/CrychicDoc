package com.github.alexthe666.alexsmobs.world;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FeatureLeafcutterAnthill extends Feature<NoneFeatureConfiguration> {

    public FeatureLeafcutterAnthill(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (context.level().m_213780_().nextFloat() > 0.0175F) {
            return false;
        } else {
            int x = 8;
            int z = 8;
            BlockPos pos = context.origin();
            int y = context.level().m_6924_(Heightmap.Types.WORLD_SURFACE_WG, pos.m_123341_() + x, pos.m_123343_() + z);
            BlockPos heightPos = new BlockPos(pos.m_123341_() + x, y, pos.m_123343_() + z);
            if (!context.level().m_6425_(heightPos.below()).isEmpty()) {
                return false;
            } else {
                int outOfGround = 2 + context.level().m_213780_().nextInt(2);
                for (int i = 0; i < outOfGround; i++) {
                    float size = (float) (outOfGround - i);
                    int lvt_8_1_ = (int) (Math.floor((double) size) * (double) context.level().m_213780_().nextFloat()) + 2;
                    int lvt_10_1_ = (int) (Math.floor((double) size) * (double) context.level().m_213780_().nextFloat()) + 2;
                    float radius = (float) (lvt_8_1_ + lvt_10_1_) * 0.333F;
                    for (BlockPos lvt_13_1_ : BlockPos.betweenClosed(heightPos.offset(-lvt_8_1_, 0, -lvt_10_1_), heightPos.offset(lvt_8_1_, 3, lvt_10_1_))) {
                        if (lvt_13_1_.m_123331_(heightPos) <= (double) (radius * radius)) {
                            BlockState block = Blocks.COARSE_DIRT.defaultBlockState();
                            if (context.level().m_213780_().nextFloat() < 0.2F) {
                                block = Blocks.DIRT.defaultBlockState();
                            }
                            context.level().m_7731_(lvt_13_1_, block, 4);
                        }
                    }
                }
                Random chunkSeedRandom = new Random(pos.asLong());
                outOfGround -= chunkSeedRandom.nextInt(1) + 1;
                heightPos = heightPos.offset(-chunkSeedRandom.nextInt(2), 0, -chunkSeedRandom.nextInt(2));
                if (context.level().m_8055_(heightPos.above(outOfGround + 1)).m_60734_() != AMBlockRegistry.LEAFCUTTER_ANTHILL.get() && context.level().m_8055_(heightPos.above(outOfGround - 1)).m_60734_() != AMBlockRegistry.LEAFCUTTER_ANTHILL.get()) {
                    context.level().m_7731_(heightPos.above(outOfGround), AMBlockRegistry.LEAFCUTTER_ANTHILL.get().defaultBlockState(), 4);
                    if (context.level().m_7702_(heightPos.above(outOfGround)) instanceof TileEntityLeafcutterAnthill beehivetileentity) {
                        int j = 3 + chunkSeedRandom.nextInt(3);
                        if (beehivetileentity.hasNoAnts()) {
                            for (int k = 0; k < j; k++) {
                                EntityLeafcutterAnt beeentity = new EntityLeafcutterAnt(AMEntityRegistry.LEAFCUTTER_ANT.get(), context.level().m_6018_());
                                beeentity.setQueen(k == 0);
                                beehivetileentity.tryEnterHive(beeentity, false, context.level().m_213780_().nextInt(599));
                            }
                        }
                    }
                    if (context.level().m_213780_().nextBoolean()) {
                        context.level().m_7731_(heightPos.above(outOfGround).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 1).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 2).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                    }
                    if (context.level().m_213780_().nextBoolean()) {
                        context.level().m_7731_(heightPos.above(outOfGround).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 1).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 2).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                    }
                    if (context.level().m_213780_().nextBoolean()) {
                        context.level().m_7731_(heightPos.above(outOfGround).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 1).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 2).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                    }
                    if (context.level().m_213780_().nextBoolean()) {
                        context.level().m_7731_(heightPos.above(outOfGround).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 1).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                        context.level().m_7731_(heightPos.above(outOfGround - 2).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                    }
                    for (int airs = 1; airs < 3; airs++) {
                        context.level().m_7731_(heightPos.above(outOfGround + airs), Blocks.AIR.defaultBlockState(), 4);
                    }
                }
                int i = outOfGround;
                int down = context.level().m_213780_().nextInt(2) + 1;
                while (i > -down) {
                    context.level().m_7731_(heightPos.above(--i), AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState(), 4);
                }
                float size = (float) (chunkSeedRandom.nextInt(1) + 1);
                int lvt_8_1_ = (int) (Math.floor((double) size) * (double) context.level().m_213780_().nextFloat()) + 1;
                int lvt_9_1_ = (int) (Math.floor((double) size) * (double) context.level().m_213780_().nextFloat()) + 1;
                int lvt_10_1_ = (int) (Math.floor((double) size) * (double) context.level().m_213780_().nextFloat()) + 1;
                float radius = (float) (lvt_8_1_ + lvt_9_1_ + lvt_10_1_) * 0.333F + 0.5F;
                heightPos = heightPos.below(down + lvt_9_1_).offset(chunkSeedRandom.nextInt(2), 0, chunkSeedRandom.nextInt(2));
                for (BlockPos lvt_13_1_x : BlockPos.betweenClosed(heightPos.offset(-lvt_8_1_, -lvt_9_1_, -lvt_10_1_), heightPos.offset(lvt_8_1_, lvt_9_1_, lvt_10_1_))) {
                    if (lvt_13_1_x.m_123331_(heightPos) < (double) (radius * radius)) {
                        BlockState block = AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState();
                        context.level().m_7731_(lvt_13_1_x, block, 4);
                    }
                }
                return true;
            }
        }
    }
}