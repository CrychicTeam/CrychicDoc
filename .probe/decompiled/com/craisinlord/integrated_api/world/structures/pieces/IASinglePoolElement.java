package com.craisinlord.integrated_api.world.structures.pieces;

import com.craisinlord.integrated_api.modinit.IAStructurePieces;
import com.craisinlord.integrated_api.world.condition.StructureCondition;
import com.craisinlord.integrated_api.world.structures.modifier.StructureModifier;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IASinglePoolElement extends IAPoolElement {

    private static final Codec<Either<ResourceLocation, StructureTemplate>> TEMPLATE_CODEC = Codec.of(IASinglePoolElement::encodeTemplate, ResourceLocation.CODEC.map(Either::left));

    public static final Codec<IASinglePoolElement> CODEC = RecordCodecBuilder.create(builder -> builder.group(templateCodec(), processorsCodec(), m_210538_(), nameCodec(), maxCountCodec(), minRequiredDepthCodec(), maxPossibleDepthCodec(), isPriorityCodec(), ignoreBoundsCodec(), conditionCodec(), enhancedTerrainAdaptationCodec(), ResourceLocation.CODEC.optionalFieldOf("deadend_pool").forGetter(element -> element.deadendPool), StructureModifier.CODEC.listOf().optionalFieldOf("modifiers", new ArrayList()).forGetter(element -> element.modifiers)).apply(builder, IASinglePoolElement::new));

    public final Either<ResourceLocation, StructureTemplate> template;

    public final Holder<StructureProcessorList> processors;

    public final Optional<ResourceLocation> deadendPool;

    public final List<StructureModifier> modifiers;

    public IASinglePoolElement(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<String> name, Optional<Integer> maxCount, Optional<Integer> minRequiredDepth, Optional<Integer> maxPossibleDepth, boolean isPriority, boolean ignoreBounds, StructureCondition condition, Optional<EnhancedTerrainAdaptation> enhancedTerrainAdaptation, Optional<ResourceLocation> deadendPool, List<StructureModifier> modifiers) {
        super(projection, name, maxCount, minRequiredDepth, maxPossibleDepth, isPriority, ignoreBounds, condition, enhancedTerrainAdaptation);
        this.template = template;
        this.processors = processors;
        this.deadendPool = deadendPool;
        this.modifiers = modifiers;
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
        StructureTemplate structureTemplate = this.getTemplate(structureTemplateManager);
        return structureTemplate.getSize(rotation);
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, RandomSource randomSource) {
        StructureTemplate structureTemplate = this.getTemplate(structureTemplateManager);
        ObjectArrayList<StructureTemplate.StructureBlockInfo> jigsawBlocks = structureTemplate.filterBlocks(blockPos, new StructurePlaceSettings().setRotation(rotation), Blocks.JIGSAW, true);
        Util.shuffle(jigsawBlocks, randomSource);
        return jigsawBlocks;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation) {
        StructureTemplate structureTemplate = this.getTemplate(structureTemplateManager);
        return structureTemplate.getBoundingBox(new StructurePlaceSettings().setRotation(rotation), blockPos);
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivotPos, Rotation rotation, BoundingBox boundingBox, RandomSource randomSource, boolean replaceJigsaws) {
        StructureTemplate structureTemplate = this.getTemplate(structureTemplateManager);
        StructurePlaceSettings structurePlaceSettings = this.getSettings(rotation, boundingBox, replaceJigsaws);
        if (!structureTemplate.placeInWorld(worldGenLevel, pos, pivotPos, structurePlaceSettings, randomSource, 18)) {
            return false;
        } else {
            for (StructureTemplate.StructureBlockInfo $$13 : StructureTemplate.processBlockInfos(worldGenLevel, pos, pivotPos, structurePlaceSettings, this.getDataMarkers(structureTemplateManager, pos, rotation, false))) {
                this.m_227329_(worldGenLevel, $$13, pos, rotation, randomSource, boundingBox);
            }
            return true;
        }
    }

    public Optional<ResourceLocation> getDeadendPool() {
        return this.deadendPool;
    }

    public boolean hasModifiers() {
        return this.modifiers.size() > 0;
    }

    public StructureTemplate getTemplate(StructureTemplateManager structureTemplateManager) {
        return (StructureTemplate) this.template.map(structureTemplateManager::m_230359_, Function.identity());
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return IAStructurePieces.IA_POOL_ELEMENT.get();
    }

    public String toString() {
        return String.format("IntegratedAPIJigsawSingle[%s][%s][%s][%s]", this.name.orElse("<unnamed>"), this.template, this.maxCount.isPresent() ? this.maxCount.get() : "no max count", this.isPriority);
    }

    private StructurePlaceSettings getSettings(Rotation rotation, BoundingBox boundingBox, boolean replaceJigsaws) {
        StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings();
        structurePlaceSettings.setBoundingBox(boundingBox);
        structurePlaceSettings.setRotation(rotation);
        structurePlaceSettings.setKnownShape(true);
        structurePlaceSettings.setIgnoreEntities(false);
        structurePlaceSettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        structurePlaceSettings.setFinalizeEntities(true);
        if (!replaceJigsaws) {
            structurePlaceSettings.addProcessor(JigsawReplacementProcessor.INSTANCE);
        }
        this.processors.value().list().forEach(structurePlaceSettings::m_74383_);
        this.m_210539_().getProcessors().forEach(structurePlaceSettings::m_74383_);
        return structurePlaceSettings;
    }

    private List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, boolean isPositionLocal) {
        StructureTemplate structureTemplate = this.getTemplate(structureTemplateManager);
        List<StructureTemplate.StructureBlockInfo> structureBlocks = structureTemplate.filterBlocks(blockPos, new StructurePlaceSettings().setRotation(rotation), Blocks.STRUCTURE_BLOCK, isPositionLocal);
        List<StructureTemplate.StructureBlockInfo> dataBlocks = Lists.newArrayList();
        for (StructureTemplate.StructureBlockInfo block : structureBlocks) {
            StructureMode structureMode = StructureMode.valueOf(block.nbt().getString("mode"));
            if (structureMode == StructureMode.DATA) {
                dataBlocks.add(block);
            }
        }
        return dataBlocks;
    }

    public static <E extends IASinglePoolElement> RecordCodecBuilder<E, Holder<StructureProcessorList>> processorsCodec() {
        return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(element -> element.processors);
    }

    public static <E extends IASinglePoolElement> RecordCodecBuilder<E, Either<ResourceLocation, StructureTemplate>> templateCodec() {
        return TEMPLATE_CODEC.fieldOf("location").forGetter($$0 -> $$0.template);
    }

    private static <T> DataResult<T> encodeTemplate(Either<ResourceLocation, StructureTemplate> either, DynamicOps<T> ops, T template) {
        Optional<ResourceLocation> optional = either.left();
        return !optional.isPresent() ? DataResult.error(() -> "Can not serialize a runtime pool element") : ResourceLocation.CODEC.encode((ResourceLocation) optional.get(), ops, template);
    }
}