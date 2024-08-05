package com.mna.api.spells.adjusters;

import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpellAdjustingContext {

    public final ItemStack stack;

    public final ISpellDefinition spell;

    public final LivingEntity caster;

    public final SpellCastStage stage;

    public SpellAdjustingContext(ItemStack stack, ISpellDefinition spell, LivingEntity caster, SpellCastStage stage) {
        this.stack = stack;
        this.caster = caster;
        this.stage = stage;
        this.spell = spell;
    }
}