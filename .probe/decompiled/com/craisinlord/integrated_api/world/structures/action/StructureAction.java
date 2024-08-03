package com.craisinlord.integrated_api.world.structures.action;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.craisinlord.integrated_api.world.structures.pieces.assembler.PieceEntry;

public abstract class StructureAction {

    public abstract StructureActionType<?> type();

    public abstract void apply(StructureContext var1, PieceEntry var2);
}