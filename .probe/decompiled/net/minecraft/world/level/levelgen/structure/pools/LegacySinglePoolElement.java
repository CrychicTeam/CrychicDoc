package net.minecraft.world.level.levelgen.structure.pools;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class LegacySinglePoolElement extends SinglePoolElement {

    public static final Codec<LegacySinglePoolElement> CODEC = RecordCodecBuilder.create(p_210357_ -> p_210357_.group(m_210465_(), m_210462_(), m_210538_()).apply(p_210357_, LegacySinglePoolElement::new));

    protected LegacySinglePoolElement(Either<ResourceLocation, StructureTemplate> eitherResourceLocationStructureTemplate0, Holder<StructureProcessorList> holderStructureProcessorList1, StructureTemplatePool.Projection structureTemplatePoolProjection2) {
        super(eitherResourceLocationStructureTemplate0, holderStructureProcessorList1, structureTemplatePoolProjection2);
    }

    @Override
    protected StructurePlaceSettings getSettings(Rotation rotation0, BoundingBox boundingBox1, boolean boolean2) {
        StructurePlaceSettings $$3 = super.getSettings(rotation0, boundingBox1, boolean2);
        $$3.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        $$3.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        return $$3;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.LEGACY;
    }

    @Override
    public String toString() {
        return "LegacySingle[" + this.f_210411_ + "]";
    }
}