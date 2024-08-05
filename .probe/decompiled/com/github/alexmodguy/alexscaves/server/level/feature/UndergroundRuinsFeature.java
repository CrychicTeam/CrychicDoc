package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.config.UndergroundRuinsFeatureConfiguration;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class UndergroundRuinsFeature extends Feature<UndergroundRuinsFeatureConfiguration> {

    public UndergroundRuinsFeature(Codec<UndergroundRuinsFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<UndergroundRuinsFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos chunkCenter = context.origin().atY(level.m_141937_() + 3);
        List<BlockPos> genPos = new ArrayList();
        int surface = level.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, chunkCenter.m_123341_(), chunkCenter.m_123343_()) - 5;
        int j = 0;
        while (chunkCenter.m_123342_() < surface) {
            BlockPos next = chunkCenter.above();
            BlockState currentState = level.m_8055_(chunkCenter);
            BlockState nextState = level.m_8055_(next);
            if (!this.canReplace(currentState, j) && this.canReplace(nextState, j + 1)) {
                genPos.add(chunkCenter);
            }
            j++;
            chunkCenter = next;
        }
        if (genPos.isEmpty()) {
            return false;
        } else {
            BlockPos blockpos = genPos.size() <= 1 ? (BlockPos) genPos.get(0) : (BlockPos) genPos.get(randomsource.nextInt(genPos.size() - 1));
            if (!this.canGenerateAt(level, blockpos)) {
                return false;
            } else {
                Rotation rotation = Rotation.getRandom(randomsource);
                UndergroundRuinsFeatureConfiguration config = context.config();
                int i = randomsource.nextInt(config.structures.size());
                StructureTemplateManager structuretemplatemanager = level.m_6018_().getServer().getStructureManager();
                StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate((ResourceLocation) config.structures.get(i));
                ChunkPos chunkpos = new ChunkPos(blockpos);
                BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, level.m_141937_(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, level.m_151558_(), chunkpos.getMaxBlockZ() + 16);
                StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingbox).setRandom(randomsource);
                structureplacesettings = this.modifyPlacementSettings(structureplacesettings);
                Vec3i vec3i = structuretemplate.getSize(rotation);
                BlockPos blockpos1 = blockpos.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
                for (int replaceDown = 0; this.skipsOver(level.m_8055_(blockpos1), replaceDown) && blockpos1.m_123342_() < level.m_141937_(); replaceDown++) {
                    blockpos1 = blockpos1.below();
                }
                blockpos1 = blockpos1.below(config.sinkBy);
                BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1, Mirror.NONE, rotation);
                if (structuretemplate.placeInWorld(level, blockpos2, blockpos2, structureplacesettings, randomsource, 18)) {
                    for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : StructureTemplate.processBlockInfos(level, blockpos2, blockpos2, structureplacesettings, getDataMarkers(structuretemplate, blockpos2, rotation, false))) {
                        String marker = structuretemplate$structureblockinfo.nbt().getString("metadata");
                        if (marker.equals("loot_chest")) {
                            level.m_7731_(structuretemplate$structureblockinfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), 3);
                            RandomizableContainerBlockEntity.setLootTable(level, randomsource, structuretemplate$structureblockinfo.pos().below(), context.config().chestLoot);
                        } else {
                            this.processMarker(marker, level, structuretemplate$structureblockinfo.pos(), randomsource);
                        }
                    }
                    this.processBoundingBox(level, structuretemplate.getBoundingBox(structureplacesettings, blockpos2), randomsource);
                }
                return true;
            }
        }
    }

    public void processBoundingBox(WorldGenLevel level, BoundingBox boundingBox, RandomSource randomsource) {
    }

    public StructurePlaceSettings modifyPlacementSettings(StructurePlaceSettings structureplacesettings) {
        return structureplacesettings;
    }

    public void processMarker(String marker, WorldGenLevel level, BlockPos pos, RandomSource randomsource) {
    }

    protected boolean canGenerateAt(WorldGenLevel level, BlockPos blockpos) {
        return true;
    }

    protected boolean canReplace(BlockState state, int already) {
        return (state.m_60795_() || state.m_247087_()) && (state.m_60819_().getFluidType() != ACFluidRegistry.ACID_FLUID_TYPE.get() || already < 3);
    }

    protected boolean skipsOver(BlockState state, int already) {
        return this.canReplace(state, already);
    }

    private static List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplate structuretemplate, BlockPos blockPos0, Rotation rotation1, boolean boolean2) {
        List<StructureTemplate.StructureBlockInfo> list = structuretemplate.filterBlocks(blockPos0, new StructurePlaceSettings().setRotation(rotation1), Blocks.STRUCTURE_BLOCK, boolean2);
        List<StructureTemplate.StructureBlockInfo> list1 = Lists.newArrayList();
        for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : list) {
            if (structuretemplate$structureblockinfo.nbt() != null) {
                StructureMode structuremode = StructureMode.valueOf(structuretemplate$structureblockinfo.nbt().getString("mode"));
                if (structuremode == StructureMode.DATA) {
                    list1.add(structuretemplate$structureblockinfo);
                }
            }
        }
        return list1;
    }
}