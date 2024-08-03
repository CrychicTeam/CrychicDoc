package org.violetmoon.quark.content.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.material.Fluids;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.module.CompressedBlocksModule;
import org.violetmoon.quark.content.world.module.NetherObsidianSpikesModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.MiscUtil;
import org.violetmoon.zeta.world.generator.Generator;

public class ObsidianSpikeGenerator extends Generator {

    public ObsidianSpikeGenerator(DimensionConfig dimConfig) {
        super(dimConfig);
    }

    @Override
    public void generateChunk(WorldGenRegion world, ChunkGenerator generator, RandomSource rand, BlockPos chunkCorner) {
        if ((double) rand.nextFloat() < NetherObsidianSpikesModule.chancePerChunk) {
            for (int i = 0; i < NetherObsidianSpikesModule.triesPerChunk; i++) {
                for (BlockPos pos = chunkCorner.offset(rand.nextInt(16), 50, rand.nextInt(16)); pos.m_123342_() > 10; pos = pos.below()) {
                    BlockState state = world.getBlockState(pos);
                    if (state.m_60734_() == Blocks.LAVA) {
                        placeSpikeAt(world, pos, rand);
                        break;
                    }
                }
            }
        }
    }

    public static void placeSpikeAt(WorldGenRegion world, BlockPos pos, RandomSource rand) {
        int heightBelow = 10;
        int heightBottom = 3 + rand.nextInt(3);
        int heightMiddle = 2 + rand.nextInt(4);
        int heightTop = 2 + rand.nextInt(3);
        boolean addSpawner = false;
        if ((double) rand.nextFloat() < NetherObsidianSpikesModule.bigSpikeChance) {
            heightBottom += 7;
            heightMiddle += 8;
            heightTop += 4;
            addSpawner = NetherObsidianSpikesModule.bigSpikeSpawners;
        }
        int checkHeight = heightBottom + heightMiddle + heightTop + 2;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < checkHeight; k++) {
                    BlockPos checkPos = pos.offset(i - 2, k, j - 2);
                    if (!world.m_46859_(checkPos) && !world.getFluidState(checkPos).is(Fluids.LAVA) && !world.getFluidState(checkPos).is(Fluids.FLOWING_LAVA)) {
                        return;
                    }
                }
            }
        }
        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int kx = 0; kx < heightBottom + heightBelow; kx++) {
                    BlockPos placePos = pos.offset(i - 1, kx - heightBelow, j - 1);
                    if (world.getBlockState(placePos).m_60800_(world, placePos) != -1.0F) {
                        world.m_7731_(placePos, obsidian, 0);
                    }
                }
            }
        }
        for (int i = 0; i < heightMiddle; i++) {
            BlockPos placePos = pos.offset(0, heightBottom + i, 0);
            world.m_7731_(placePos, obsidian, 0);
            for (Direction face : MiscUtil.HORIZONTALS) {
                world.m_7731_(placePos.relative(face), obsidian, 0);
            }
        }
        for (int i = 0; i < heightTop; i++) {
            BlockPos placePos = pos.offset(0, heightBottom + heightMiddle + i, 0);
            world.m_7731_(placePos, obsidian, 0);
            if (addSpawner && i == 0) {
                boolean useBlazeLantern = Quark.ZETA.modules.isEnabled(CompressedBlocksModule.class) && CompressedBlocksModule.enableBlazeLantern;
                world.m_7731_(placePos, useBlazeLantern ? CompressedBlocksModule.blaze_lantern.defaultBlockState() : Blocks.GLOWSTONE.defaultBlockState(), 0);
                placePos = placePos.below();
                world.m_7731_(placePos, Blocks.SPAWNER.defaultBlockState(), 0);
                ((SpawnerBlockEntity) world.getBlockEntity(placePos)).getSpawner().setEntityId(EntityType.BLAZE, null, rand, pos);
                placePos = placePos.below();
                world.m_7731_(placePos, Blocks.CHEST.defaultBlockState(), 0);
                RandomizableContainerBlockEntity.setLootTable(world, rand, placePos, new ResourceLocation("minecraft", "chests/nether_bridge"));
            }
        }
    }
}