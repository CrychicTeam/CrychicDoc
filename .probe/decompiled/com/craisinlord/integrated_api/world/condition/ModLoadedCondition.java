package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ModLoadedCondition extends StructureCondition {

    public static final Codec<ModLoadedCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.STRING.fieldOf("modid").forGetter(conditon -> conditon.modId)).apply(builder, ModLoadedCondition::new));

    private final String modId;

    public ModLoadedCondition(String modId) {
        this.modId = modId;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.MOD_LOADED;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return PlatformHooks.isModLoaded(this.modId);
    }
}