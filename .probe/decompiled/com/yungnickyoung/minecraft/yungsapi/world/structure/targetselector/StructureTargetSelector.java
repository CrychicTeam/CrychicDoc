package com.yungnickyoung.minecraft.yungsapi.world.structure.targetselector;

import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import java.util.List;

public abstract class StructureTargetSelector {

    public abstract StructureTargetSelectorType<?> type();

    public abstract List<PieceEntry> apply(StructureContext var1);
}