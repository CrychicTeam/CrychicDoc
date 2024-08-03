package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import java.util.Optional;

public class AltitudeCondition extends StructureCondition {

    public static final Codec<AltitudeCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.DOUBLE.optionalFieldOf("bottom_cutoff_y").forGetter(conditon -> conditon.bottomCutoffY), Codec.DOUBLE.optionalFieldOf("top_cutoff_y").forGetter(conditon -> conditon.topCutoffY)).apply(builder, AltitudeCondition::new));

    private Optional<Double> bottomCutoffY;

    private Optional<Double> topCutoffY;

    public AltitudeCondition(Optional<Double> bottomCutoffY, Optional<Double> topCutoffY) {
        this.bottomCutoffY = bottomCutoffY;
        this.topCutoffY = topCutoffY;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ALTITUDE;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return this.bottomCutoffY.isPresent() && (double) ctx.pieceMinY() < this.bottomCutoffY.get() ? false : !this.topCutoffY.isPresent() || !((double) ctx.pieceMaxY() > (Double) this.topCutoffY.get());
    }
}