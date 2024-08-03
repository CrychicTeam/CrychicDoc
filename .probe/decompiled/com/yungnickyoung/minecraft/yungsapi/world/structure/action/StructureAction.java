package com.yungnickyoung.minecraft.yungsapi.world.structure.action;

import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;

public abstract class StructureAction {

    public abstract StructureActionType<?> type();

    public abstract void apply(StructureContext var1, PieceEntry var2);
}