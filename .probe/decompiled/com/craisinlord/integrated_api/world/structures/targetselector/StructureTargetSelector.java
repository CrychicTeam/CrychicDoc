package com.craisinlord.integrated_api.world.structures.targetselector;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.craisinlord.integrated_api.world.structures.pieces.assembler.PieceEntry;
import java.util.List;

public abstract class StructureTargetSelector {

    public abstract StructureTargetSelectorType<?> type();

    public abstract List<PieceEntry> apply(StructureContext var1);
}