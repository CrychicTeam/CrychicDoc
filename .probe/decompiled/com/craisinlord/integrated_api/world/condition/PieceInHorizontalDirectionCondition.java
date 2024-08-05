package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.mixins.structures.SinglePoolElementAccessor;
import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.craisinlord.integrated_api.world.structures.pieces.IASinglePoolElement;
import com.craisinlord.integrated_api.world.structures.pieces.assembler.PieceEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class PieceInHorizontalDirectionCondition extends StructureCondition {

    private static final ResourceLocation ALL = new ResourceLocation("integrated_api", "all");

    public static final Codec<PieceInHorizontalDirectionCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(ResourceLocation.CODEC.listOf().optionalFieldOf("pieces", new ArrayList()).forGetter(conditon -> conditon.matchPieces), Codec.INT.fieldOf("range").forGetter(conditon -> conditon.range), Rotation.CODEC.fieldOf("rotation").forGetter(conditon -> conditon.rotation)).apply(builder, PieceInHorizontalDirectionCondition::new));

    private final List<ResourceLocation> matchPieces;

    private final Integer range;

    private final Rotation rotation;

    public PieceInHorizontalDirectionCondition(List<ResourceLocation> pieces, int range, Rotation rotation) {
        this.matchPieces = pieces;
        this.range = range;
        this.rotation = rotation;
        if (this.matchPieces.isEmpty()) {
            this.matchPieces.add(ALL);
        }
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.PIECE_IN_HORIZONTAL_DIRECTION;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        StructureTemplateManager templateManager = ctx.structureTemplateManager();
        List<PieceEntry> pieces = ctx.pieces();
        Rotation pieceRotation = ctx.rotation();
        PieceEntry pieceEntry = ctx.pieceEntry();
        if (templateManager == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'structureTemplateManager' for piece_in_horizontal_direction condition!");
        }
        if (pieces == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'pieces' for piece_in_horizontal_direction condition!");
        }
        if (this.rotation == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'rotation' for piece_in_horizontal_direction condition!");
        }
        if (pieceEntry == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'pieceEntry' for piece_in_horizontal_direction condition!");
        }
        if (templateManager != null && pieces != null && this.rotation != null && pieceEntry != null) {
            PoolElementStructurePiece piece = pieceEntry.getPiece();
            Rotation searchRotation = pieceRotation.getRotated(this.rotation);
            int negX = 0;
            int negZ = 0;
            int posX = 0;
            int posZ = 0;
            switch(searchRotation) {
                case NONE:
                    negZ = this.range;
                    break;
                case CLOCKWISE_90:
                    posX = this.range;
                    break;
                case CLOCKWISE_180:
                    posZ = this.range;
                    break;
                case COUNTERCLOCKWISE_90:
                    negX = this.range;
            }
            BoundingBox searchBox = new BoundingBox(piece.m_73547_().minX() - negX, piece.m_73547_().minY(), piece.m_73547_().minZ() - negZ, piece.m_73547_().maxX() + posX, piece.m_73547_().maxY(), piece.m_73547_().maxZ() + posZ);
            for (PieceEntry otherPieceEntry : pieces) {
                PoolElementStructurePiece otherPiece = otherPieceEntry.getPiece();
                if ((otherPiece.getElement() instanceof SinglePoolElement || otherPiece.getElement() instanceof IASinglePoolElement) && !otherPiece.m_73547_().equals(piece.m_73547_())) {
                    StructureTemplate otherStructureTemplate = otherPiece.getElement() instanceof SinglePoolElement ? ((SinglePoolElementAccessor) otherPiece.getElement()).callGetTemplate(templateManager) : ((IASinglePoolElement) otherPiece.getElement()).getTemplate(templateManager);
                    for (ResourceLocation matchPieceId : this.matchPieces) {
                        StructureTemplate structureTemplate = templateManager.getOrCreate(matchPieceId);
                        if ((otherStructureTemplate == structureTemplate || matchPieceId.equals(ALL)) && otherPiece.m_73547_().intersects(searchBox) && !otherPiece.m_73547_().intersects(piece.m_73547_())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }
}