package com.yungnickyoung.minecraft.yungsapi.world.structure.action;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;

public class DelayGenerationAction extends StructureAction {

    private static final DelayGenerationAction INSTANCE = new DelayGenerationAction();

    public static final Codec<DelayGenerationAction> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureActionType<?> type() {
        return StructureActionType.DELAY_GENERATION;
    }

    @Override
    public void apply(StructureContext ctx, PieceEntry targetPieceEntry) {
        targetPieceEntry.setDelayGeneration(true);
    }
}