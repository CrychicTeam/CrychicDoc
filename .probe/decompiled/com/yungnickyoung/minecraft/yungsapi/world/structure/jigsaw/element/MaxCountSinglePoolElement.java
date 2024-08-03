package com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePoolElementTypeModule;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

@Deprecated
public class MaxCountSinglePoolElement extends SinglePoolElement implements IMaxCountJigsawPoolElement {

    public static final Codec<MaxCountSinglePoolElement> CODEC = RecordCodecBuilder.create(builder -> builder.group(m_210465_(), m_210462_(), m_210538_(), Codec.STRING.fieldOf("name").forGetter(MaxCountSinglePoolElement::getName), Codec.INT.fieldOf("max_count").forGetter(MaxCountSinglePoolElement::getMaxCount)).apply(builder, MaxCountSinglePoolElement::new));

    protected final int maxCount;

    protected final String name;

    public MaxCountSinglePoolElement(Either<ResourceLocation, StructureTemplate> resourceLocation, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, String name, int maxCount) {
        super(resourceLocation, processors, projection);
        this.maxCount = maxCount;
        this.name = name;
    }

    @Override
    public int getMaxCount() {
        return this.maxCount;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementTypeModule.MAX_COUNT_SINGLE_ELEMENT;
    }

    @Override
    public String toString() {
        return "MaxCountSingle[" + this.name + "][" + this.f_210411_ + "][" + this.maxCount + "]";
    }
}