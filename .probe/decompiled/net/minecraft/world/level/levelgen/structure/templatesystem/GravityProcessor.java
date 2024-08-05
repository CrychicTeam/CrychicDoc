package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;

public class GravityProcessor extends StructureProcessor {

    public static final Codec<GravityProcessor> CODEC = RecordCodecBuilder.create(p_74116_ -> p_74116_.group(Heightmap.Types.CODEC.fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(p_163729_ -> p_163729_.heightmap), Codec.INT.fieldOf("offset").orElse(0).forGetter(p_163727_ -> p_163727_.offset)).apply(p_74116_, GravityProcessor::new));

    private final Heightmap.Types heightmap;

    private final int offset;

    public GravityProcessor(Heightmap.Types heightmapTypes0, int int1) {
        this.heightmap = heightmapTypes0;
        this.offset = int1;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        Heightmap.Types $$6;
        if (levelReader0 instanceof ServerLevel) {
            if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
                $$6 = Heightmap.Types.WORLD_SURFACE;
            } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
                $$6 = Heightmap.Types.OCEAN_FLOOR;
            } else {
                $$6 = this.heightmap;
            }
        } else {
            $$6 = this.heightmap;
        }
        BlockPos $$10 = structureTemplateStructureBlockInfo4.pos();
        int $$11 = levelReader0.getHeight($$6, $$10.m_123341_(), $$10.m_123343_()) + this.offset;
        int $$12 = structureTemplateStructureBlockInfo3.pos().m_123342_();
        return new StructureTemplate.StructureBlockInfo(new BlockPos($$10.m_123341_(), $$11 + $$12, $$10.m_123343_()), structureTemplateStructureBlockInfo4.state(), structureTemplateStructureBlockInfo4.nbt());
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.GRAVITY;
    }
}