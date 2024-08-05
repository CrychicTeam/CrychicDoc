package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import com.yungnickyoung.minecraft.yungsapi.mixin.accessor.SinglePoolElementAccessor;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.YungJigsawSinglePoolElement;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class PieceInRangeCondition extends StructureCondition {

    private static final ResourceLocation ALL = new ResourceLocation("yungsapi", "all");

    public static final Codec<PieceInRangeCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(ResourceLocation.CODEC.listOf().optionalFieldOf("pieces", new ArrayList()).forGetter(conditon -> conditon.matchPieces), Codec.INT.optionalFieldOf("above_range", 0).forGetter(conditon -> conditon.aboveRange), Codec.INT.optionalFieldOf("horizontal_range", 0).forGetter(conditon -> conditon.horizontalRange), Codec.INT.optionalFieldOf("below_range", 0).forGetter(conditon -> conditon.belowRange)).apply(builder, PieceInRangeCondition::new));

    private final List<ResourceLocation> matchPieces;

    private final Integer aboveRange;

    private final Integer horizontalRange;

    private final Integer belowRange;

    public PieceInRangeCondition(List<ResourceLocation> pieces, int aboveRange, int horizontalRange, int belowRange) {
        this.matchPieces = pieces;
        this.aboveRange = aboveRange;
        this.horizontalRange = horizontalRange;
        this.belowRange = belowRange;
        if (this.matchPieces.isEmpty()) {
            this.matchPieces.add(ALL);
        }
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.PIECE_IN_RANGE;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        StructureTemplateManager templateManager = ctx.structureTemplateManager();
        List<PieceEntry> pieces = ctx.pieces();
        PieceEntry pieceEntry = ctx.pieceEntry();
        if (templateManager == null) {
            YungsApiCommon.LOGGER.error("Missing required field 'structureTemplateManager' for piece_in_range condition!");
        }
        if (pieces == null) {
            YungsApiCommon.LOGGER.error("Missing required field 'pieces' for piece_in_range condition!");
        }
        if (pieceEntry == null) {
            YungsApiCommon.LOGGER.error("Missing required field 'pieceEntry' for piece_in_horizontal_direction condition!");
        }
        if (templateManager != null && pieces != null && pieceEntry != null) {
            PoolElementStructurePiece piece = pieceEntry.getPiece();
            BoundingBox searchBox = new BoundingBox(piece.m_73547_().minX() - this.horizontalRange, piece.m_73547_().minY() - this.belowRange, piece.m_73547_().minZ() - this.horizontalRange, piece.m_73547_().maxX() + this.horizontalRange, piece.m_73547_().maxY() + this.aboveRange, piece.m_73547_().maxZ() + this.horizontalRange);
            for (PieceEntry otherPieceEntry : pieces) {
                PoolElementStructurePiece otherPiece = otherPieceEntry.getPiece();
                if ((otherPiece.getElement() instanceof SinglePoolElement || otherPiece.getElement() instanceof YungJigsawSinglePoolElement) && !otherPiece.m_73547_().equals(piece.m_73547_())) {
                    StructureTemplate otherStructureTemplate = otherPiece.getElement() instanceof SinglePoolElement ? ((SinglePoolElementAccessor) otherPiece.getElement()).callGetTemplate(templateManager) : ((YungJigsawSinglePoolElement) otherPiece.getElement()).getTemplate(templateManager);
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