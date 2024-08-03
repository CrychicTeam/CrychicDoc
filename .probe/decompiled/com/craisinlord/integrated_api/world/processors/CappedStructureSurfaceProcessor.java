package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class CappedStructureSurfaceProcessor extends StructureProcessor {

    public static final Codec<CappedStructureSurfaceProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(cappedProcessor -> cappedProcessor.delegate), Codec.BOOL.fieldOf("allow_void_sides").orElse(false).forGetter(cappedProcessor -> cappedProcessor.allowVoidSides)).apply(instance, CappedStructureSurfaceProcessor::new));

    private static final Pair<StructureTemplate.StructureBlockInfo, Integer> DEFAULT_AIR_BLOCK = Pair.of(new StructureTemplate.StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null), 0);

    private static final Pair<StructureTemplate.StructureBlockInfo, Integer> DEFAULT_SOLID_BLOCK = Pair.of(new StructureTemplate.StructureBlockInfo(BlockPos.ZERO, Blocks.STONE.defaultBlockState(), null), 0);

    private final StructureProcessor delegate;

    private final boolean allowVoidSides;

    public CappedStructureSurfaceProcessor(StructureProcessor structureProcessor, boolean allowVoidSides) {
        this.delegate = structureProcessor;
        this.allowVoidSides = allowVoidSides;
    }

    @Override
    public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevelAccessor, BlockPos nbtOriginPos, BlockPos chunkCenter, List<StructureTemplate.StructureBlockInfo> nbtOriginBlockInfo, List<StructureTemplate.StructureBlockInfo> worldOriginBlockInfo, StructurePlaceSettings structurePlaceSettings) {
        if (!worldOriginBlockInfo.isEmpty()) {
            if (nbtOriginBlockInfo.size() != worldOriginBlockInfo.size()) {
                int listSize = nbtOriginBlockInfo.size();
                Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + listSize + ", Processed size: " + worldOriginBlockInfo.size());
            } else {
                BoundingBox boundingBox = structurePlaceSettings.getBoundingBox() == null ? BoundingBox.infinite() : structurePlaceSettings.getBoundingBox();
                Map<BlockPos, Pair<StructureTemplate.StructureBlockInfo, Integer>> nbtPosToData = new Object2ObjectArrayMap();
                for (int index = 0; index < worldOriginBlockInfo.size(); index++) {
                    StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) worldOriginBlockInfo.get(index);
                    if (boundingBox.isInside(info.pos())) {
                        nbtPosToData.put(info.pos(), Pair.of(info, index));
                    }
                }
                List<BlockPos> shuffledPositionList = new ArrayList(nbtPosToData.keySet());
                Collections.shuffle(shuffledPositionList);
                for (BlockPos currentPosition : shuffledPositionList) {
                    Pair<StructureTemplate.StructureBlockInfo, Integer> currentInfo = (Pair<StructureTemplate.StructureBlockInfo, Integer>) nbtPosToData.get(currentPosition);
                    StructureTemplate.StructureBlockInfo structureBlockInfoOriginalNbtOrigin = (StructureTemplate.StructureBlockInfo) nbtOriginBlockInfo.get((Integer) currentInfo.getSecond());
                    StructureTemplate.StructureBlockInfo structureBlockInfoWorld = (StructureTemplate.StructureBlockInfo) worldOriginBlockInfo.get((Integer) currentInfo.getSecond());
                    if (structureBlockInfoWorld != null && !structureBlockInfoWorld.state().m_60795_() && structureBlockInfoWorld.state().m_60819_().isEmpty()) {
                        BlockPos belowPos = structureBlockInfoWorld.pos().below();
                        BlockPos abovePos = structureBlockInfoWorld.pos().above();
                        if ((this.allowVoidSides || belowPos.m_123342_() >= 0) && (this.allowVoidSides || nbtPosToData.containsKey(belowPos) && nbtPosToData.containsKey(abovePos))) {
                            BlockState belowState = ((StructureTemplate.StructureBlockInfo) ((Pair) nbtPosToData.getOrDefault(belowPos, DEFAULT_SOLID_BLOCK)).getFirst()).state();
                            BlockState aboveState = ((StructureTemplate.StructureBlockInfo) ((Pair) nbtPosToData.getOrDefault(abovePos, DEFAULT_AIR_BLOCK)).getFirst()).state();
                            if (belowState.m_60815_() && !belowState.m_60713_(Blocks.JIGSAW) && (!aboveState.m_60815_() || aboveState.m_60713_(Blocks.JIGSAW))) {
                                StructureTemplate.StructureBlockInfo structureBlockInfo3 = this.delegate.processBlock(serverLevelAccessor, structureBlockInfoOriginalNbtOrigin.pos(), structureBlockInfoWorld.pos(), structureBlockInfoOriginalNbtOrigin, structureBlockInfoWorld, structurePlaceSettings);
                                if (structureBlockInfo3 != null && !structureBlockInfoWorld.equals(structureBlockInfo3)) {
                                    worldOriginBlockInfo.set((Integer) currentInfo.getSecond(), structureBlockInfo3);
                                }
                            }
                        }
                    }
                }
            }
        }
        return worldOriginBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.CAPPED_STRUCTURE_SURFACE_PROCESSOR.get();
    }
}