package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenSirenIsland extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    private final int MAX_ISLAND_RADIUS = 10;

    public WorldGenSirenIsland(Codec<NoneFeatureConfiguration> configuration) {
        super(configuration);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.generateSirenChance, context.level(), context.random(), context.origin(), this.getId(), false)) {
            return false;
        } else {
            int up = context.random().nextInt(4) + 1;
            BlockPos center = context.origin().above(up);
            int layer = 0;
            for (int sirens = 1 + context.random().nextInt(3); !context.level().m_8055_(center).m_60815_() && center.m_123342_() >= context.level().m_141937_(); center = center.below()) {
                layer++;
                for (float i = 0.0F; i < (float) this.getRadius(layer, up); i += 0.5F) {
                    for (float j = 0.0F; (double) j < (Math.PI * 2) * (double) i + (double) context.random().nextInt(2); j += 0.5F) {
                        BlockPos stonePos = BlockPos.containing(Math.floor((double) ((float) center.m_123341_() + Mth.sin(j) * i + (float) context.random().nextInt(2))), (double) center.m_123342_(), Math.floor((double) ((float) center.m_123343_() + Mth.cos(j) * i + (float) context.random().nextInt(2))));
                        context.level().m_7731_(stonePos, this.getStone(context.random()), 3);
                        BlockPos upPos = stonePos.above();
                        if (context.level().m_46859_(upPos) && context.level().m_46859_(upPos.east()) && context.level().m_46859_(upPos.north()) && context.level().m_46859_(upPos.north().east()) && context.random().nextInt(3) == 0 && sirens > 0) {
                            this.spawnSiren(context.level(), context.random(), upPos.north().east());
                            sirens--;
                        }
                    }
                }
            }
            layer++;
            for (float i = 0.0F; i < (float) this.getRadius(layer, up); i += 0.5F) {
                for (float jx = 0.0F; (double) jx < (Math.PI * 2) * (double) i + (double) context.random().nextInt(2); jx += 0.5F) {
                    for (BlockPos stonePos = BlockPos.containing(Math.floor((double) ((float) center.m_123341_() + Mth.sin(jx) * i + (float) context.random().nextInt(2))), (double) center.m_123342_(), Math.floor((double) ((float) center.m_123343_() + Mth.cos(jx) * i + (float) context.random().nextInt(2)))); !context.level().m_8055_(stonePos).m_60815_() && stonePos.m_123342_() >= 0; stonePos = stonePos.below()) {
                        context.level().m_7731_(stonePos, this.getStone(context.random()), 3);
                    }
                }
            }
            return true;
        }
    }

    private int getRadius(int layer, int up) {
        return layer > up ? (int) ((double) layer * 0.25) + up : Math.min(layer, 10);
    }

    private BlockState getStone(RandomSource random) {
        int chance = random.nextInt(100);
        if (chance > 90) {
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        } else if (chance > 70) {
            return Blocks.GRAVEL.defaultBlockState();
        } else {
            return chance > 45 ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.STONE.defaultBlockState();
        }
    }

    private void spawnSiren(ServerLevelAccessor worldIn, RandomSource rand, BlockPos position) {
        EntitySiren siren = new EntitySiren(IafEntityRegistry.SIREN.get(), worldIn.getLevel());
        siren.setSinging(true);
        siren.setHairColor(rand.nextInt(2));
        siren.setSingingPose(rand.nextInt(2));
        siren.m_19890_((double) position.m_123341_() + 0.5, (double) (position.m_123342_() + 1), (double) position.m_123343_() + 0.5, rand.nextFloat() * 360.0F, 0.0F);
        worldIn.m_7967_(siren);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.OCEAN;
    }

    @Override
    public String getId() {
        return "siren_island";
    }
}