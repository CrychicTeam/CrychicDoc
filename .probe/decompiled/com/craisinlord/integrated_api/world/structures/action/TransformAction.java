package com.craisinlord.integrated_api.world.structures.action;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.utils.BoxOctree;
import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.craisinlord.integrated_api.world.structures.pieces.IASinglePoolElement;
import com.craisinlord.integrated_api.world.structures.pieces.assembler.PieceEntry;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;

public class TransformAction extends StructureAction {

    private static final Codec<Either<ResourceLocation, StructureTemplate>> TEMPLATE_CODEC = Codec.of(TransformAction::encodeTemplate, ResourceLocation.CODEC.map(Either::left));

    public static final Codec<TransformAction> CODEC = RecordCodecBuilder.create(builder -> builder.group(TEMPLATE_CODEC.listOf().fieldOf("output").forGetter(action -> action.output), Codec.INT.optionalFieldOf("x_offset", 0).forGetter(action -> action.xOffset), Codec.INT.optionalFieldOf("y_offset", 0).forGetter(action -> action.yOffset), Codec.INT.optionalFieldOf("z_offset", 0).forGetter(action -> action.zOffset)).apply(builder, TransformAction::new));

    private final List<Either<ResourceLocation, StructureTemplate>> output;

    private final int xOffset;

    private final int yOffset;

    private final int zOffset;

    private static <T> DataResult<T> encodeTemplate(Either<ResourceLocation, StructureTemplate> either, DynamicOps<T> ops, T data) {
        return either.left().isEmpty() ? DataResult.error(() -> "Integrated API - Cannot serialize a runtime pool element") : ResourceLocation.CODEC.encode((ResourceLocation) either.left().get(), ops, data);
    }

    public TransformAction(List<Either<ResourceLocation, StructureTemplate>> output, int xOffset, int yOffset, int zOffset) {
        this.output = output;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    @Override
    public StructureActionType<?> type() {
        return StructureActionType.TRANSFORM;
    }

    @Override
    public void apply(StructureContext ctx, PieceEntry targetPieceEntry) {
        StructureTemplateManager templateManager = ctx.structureTemplateManager();
        if (templateManager == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'structureTemplateManager' for transform action!");
        } else {
            IASinglePoolElement old = (IASinglePoolElement) targetPieceEntry.getPiece().getElement();
            WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(0L));
            rand.setFeatureSeed((long) targetPieceEntry.getPiece().getPosition().m_123341_(), targetPieceEntry.getPiece().getPosition().m_123342_(), targetPieceEntry.getPiece().getPosition().m_123341_());
            Either<ResourceLocation, StructureTemplate> newTemplate = (Either<ResourceLocation, StructureTemplate>) this.output.get(rand.m_188503_(this.output.size()));
            StructurePoolElement newElement = new IASinglePoolElement(newTemplate, old.processors, old.m_210539_(), old.name, old.maxCount, old.minRequiredDepth, old.maxPossibleDepth, old.isPriority, old.ignoreBounds, old.condition, old.enhancedTerrainAdaptation, old.deadendPool, old.modifiers);
            BlockPos offset = new BlockPos(this.xOffset, this.yOffset, this.zOffset);
            offset = offset.rotate(targetPieceEntry.getPiece().getRotation());
            BlockPos newPos = targetPieceEntry.getPiece().getPosition().offset(offset);
            BoundingBox newBoundingBox = newElement.getBoundingBox(templateManager, newPos, targetPieceEntry.getPiece().getRotation());
            AABB newAabb = AABB.of(newBoundingBox);
            ((BoxOctree) targetPieceEntry.getBoxOctree().getValue()).removeBox(targetPieceEntry.getPieceAabb());
            ((BoxOctree) targetPieceEntry.getBoxOctree().getValue()).addBox(newAabb);
            PoolElementStructurePiece newPiece = new PoolElementStructurePiece(templateManager, newElement, newPos, targetPieceEntry.getPiece().getGroundLevelDelta(), targetPieceEntry.getPiece().getRotation(), newBoundingBox);
            targetPieceEntry.setPiece(newPiece);
        }
    }
}