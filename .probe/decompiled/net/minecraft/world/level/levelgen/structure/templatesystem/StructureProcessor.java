package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class StructureProcessor {

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        return structureTemplateStructureBlockInfo4;
    }

    protected abstract StructureProcessorType<?> getType();

    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, BlockPos blockPos2, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo3, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        return listStructureTemplateStructureBlockInfo4;
    }
}