package com.yungnickyoung.minecraft.yungsapi.world.structure.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.world.structure.action.StructureAction;
import com.yungnickyoung.minecraft.yungsapi.world.structure.action.StructureActionType;
import com.yungnickyoung.minecraft.yungsapi.world.structure.condition.StructureCondition;
import com.yungnickyoung.minecraft.yungsapi.world.structure.condition.StructureConditionType;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import com.yungnickyoung.minecraft.yungsapi.world.structure.targetselector.StructureTargetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.structure.targetselector.StructureTargetSelectorType;
import java.util.List;

public class StructureModifier {

    public static final Codec<StructureModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(StructureConditionType.CONDITION_CODEC.fieldOf("condition").forGetter(modifier -> modifier.condition), StructureActionType.ACTION_CODEC.listOf().fieldOf("actions").forGetter(modifier -> modifier.actions), StructureTargetSelectorType.TARGET_SELECTOR_CODEC.fieldOf("target_selector").forGetter(modifier -> modifier.targetSelector)).apply(builder, StructureModifier::new));

    private final StructureCondition condition;

    private final List<StructureAction> actions;

    private final StructureTargetSelector targetSelector;

    public StructureModifier(StructureCondition condition, List<StructureAction> actions, StructureTargetSelector targetSelector) {
        this.condition = condition;
        this.actions = actions;
        this.targetSelector = targetSelector;
    }

    public boolean apply(StructureContext structureContext) {
        if (!this.condition.passes(structureContext)) {
            return false;
        } else {
            for (PieceEntry target : this.targetSelector.apply(structureContext)) {
                this.actions.forEach(action -> action.apply(structureContext, target));
            }
            return true;
        }
    }
}