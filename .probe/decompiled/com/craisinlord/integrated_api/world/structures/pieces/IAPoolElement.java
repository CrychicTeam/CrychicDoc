package com.craisinlord.integrated_api.world.structures.pieces;

import com.craisinlord.integrated_api.world.condition.StructureCondition;
import com.craisinlord.integrated_api.world.condition.StructureConditionType;
import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IAPoolElement extends StructurePoolElement {

    public final Optional<String> name;

    public final Optional<Integer> maxCount;

    @Deprecated
    public final Optional<Integer> minRequiredDepth;

    @Deprecated
    public final Optional<Integer> maxPossibleDepth;

    public final boolean isPriority;

    public final boolean ignoreBounds;

    public final StructureCondition condition;

    public final Optional<EnhancedTerrainAdaptation> enhancedTerrainAdaptation;

    public IAPoolElement(StructureTemplatePool.Projection projection, Optional<String> name, Optional<Integer> maxCount, Optional<Integer> minRequiredDepth, Optional<Integer> maxPossibleDepth, boolean isPriority, boolean ignoreBounds, StructureCondition condition, Optional<EnhancedTerrainAdaptation> enhancedTerrainAdaptation) {
        super(projection);
        this.name = name;
        this.maxCount = maxCount;
        this.minRequiredDepth = minRequiredDepth;
        this.maxPossibleDepth = maxPossibleDepth;
        this.isPriority = isPriority;
        this.ignoreBounds = ignoreBounds;
        this.condition = condition;
        this.enhancedTerrainAdaptation = enhancedTerrainAdaptation;
    }

    public Optional<String> getName() {
        return this.name;
    }

    public Optional<Integer> getMaxCount() {
        return this.maxCount;
    }

    @Deprecated
    public Optional<Integer> getMinRequiredDepth() {
        return this.minRequiredDepth;
    }

    @Deprecated
    public Optional<Integer> getMaxPossibleDepth() {
        return this.maxPossibleDepth;
    }

    public boolean isPriorityPiece() {
        return this.isPriority;
    }

    public boolean ignoresBounds() {
        return this.ignoreBounds;
    }

    public StructureCondition getCondition() {
        return this.condition;
    }

    public Optional<EnhancedTerrainAdaptation> getEnhancedTerrainAdaptation() {
        return this.enhancedTerrainAdaptation;
    }

    public boolean isAtValidDepth(int depth) {
        boolean isAtMinRequiredDepth = this.minRequiredDepth.isEmpty() || (Integer) this.minRequiredDepth.get() <= depth;
        boolean isAtMaxAllowableDepth = this.maxPossibleDepth.isEmpty() || (Integer) this.maxPossibleDepth.get() >= depth;
        return isAtMinRequiredDepth && isAtMaxAllowableDepth;
    }

    public boolean passesConditions(StructureContext ctx) {
        return this.condition.passes(ctx);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Optional<String>> nameCodec() {
        return Codec.STRING.optionalFieldOf("name").forGetter(IAPoolElement::getName);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Optional<Integer>> maxCountCodec() {
        return ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_count").forGetter(IAPoolElement::getMaxCount);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Optional<Integer>> minRequiredDepthCodec() {
        return ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("min_required_depth").forGetter(IAPoolElement::getMinRequiredDepth);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Optional<Integer>> maxPossibleDepthCodec() {
        return ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_possible_depth").forGetter(IAPoolElement::getMaxPossibleDepth);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Boolean> isPriorityCodec() {
        return Codec.BOOL.optionalFieldOf("is_priority", false).forGetter(IAPoolElement::isPriorityPiece);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Boolean> ignoreBoundsCodec() {
        return Codec.BOOL.optionalFieldOf("ignore_bounds", false).forGetter(IAPoolElement::ignoresBounds);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, StructureCondition> conditionCodec() {
        return StructureConditionType.CONDITION_CODEC.optionalFieldOf("condition", StructureCondition.ALWAYS_TRUE).forGetter(IAPoolElement::getCondition);
    }

    public static <E extends IAPoolElement> RecordCodecBuilder<E, Optional<EnhancedTerrainAdaptation>> enhancedTerrainAdaptationCodec() {
        return EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation").forGetter(IAPoolElement::getEnhancedTerrainAdaptation);
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
        return null;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, RandomSource randomSource) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation) {
        return null;
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockPos blockPos2, Rotation rotation, BoundingBox boundingBox, RandomSource randomSource, boolean bl) {
        return false;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return null;
    }
}