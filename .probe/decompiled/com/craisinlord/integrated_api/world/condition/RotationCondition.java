package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.block.Rotation;

public class RotationCondition extends StructureCondition {

    public static final Codec<RotationCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Rotation.CODEC.listOf().fieldOf("rotations").forGetter(conditon -> conditon.validRotations)).apply(builder, RotationCondition::new));

    private final List<Rotation> validRotations;

    public RotationCondition(List<Rotation> validRotations) {
        this.validRotations = validRotations;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ROTATION;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        Rotation rotation = ctx.rotation();
        if (rotation == null) {
            IntegratedAPI.LOGGER.error("Missing required field 'rotation' for rotation condition!");
        }
        return rotation == null ? false : this.validRotations.contains(rotation);
    }
}