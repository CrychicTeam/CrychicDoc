package net.minecraft.world.level.levelgen.structure.pools;

import com.mojang.serialization.Codec;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class EmptyPoolElement extends StructurePoolElement {

    public static final Codec<EmptyPoolElement> CODEC = Codec.unit(() -> EmptyPoolElement.INSTANCE);

    public static final EmptyPoolElement INSTANCE = new EmptyPoolElement();

    private EmptyPoolElement() {
        super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager0, Rotation rotation1) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, RandomSource randomSource3) {
        return Collections.emptyList();
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2) {
        throw new IllegalStateException("Invalid call to EmtyPoolElement.getBoundingBox, filter me!");
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager0, WorldGenLevel worldGenLevel1, StructureManager structureManager2, ChunkGenerator chunkGenerator3, BlockPos blockPos4, BlockPos blockPos5, Rotation rotation6, BoundingBox boundingBox7, RandomSource randomSource8, boolean boolean9) {
        return true;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.EMPTY;
    }

    public String toString() {
        return "Empty";
    }
}