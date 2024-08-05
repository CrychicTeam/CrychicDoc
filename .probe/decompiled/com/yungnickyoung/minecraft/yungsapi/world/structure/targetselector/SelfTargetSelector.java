package com.yungnickyoung.minecraft.yungsapi.world.structure.targetselector;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import java.util.ArrayList;
import java.util.List;

public class SelfTargetSelector extends StructureTargetSelector {

    private static final SelfTargetSelector INSTANCE = new SelfTargetSelector();

    public static final Codec<SelfTargetSelector> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTargetSelectorType<?> type() {
        return StructureTargetSelectorType.SELF;
    }

    @Override
    public List<PieceEntry> apply(StructureContext ctx) {
        List<PieceEntry> list = new ArrayList();
        if (ctx.pieceEntry() != null) {
            list.add(ctx.pieceEntry());
        }
        return list;
    }
}