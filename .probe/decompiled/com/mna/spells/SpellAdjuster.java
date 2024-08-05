package com.mna.spells;

import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpellAdjuster {

    private final Predicate<SpellAdjustingContext> executeCheck;

    private final BiConsumer<ISpellDefinition, LivingEntity> bi_adjuster;

    private final Consumer<SpellAdjustingContext> adjuster;

    @Deprecated(forRemoval = false)
    public SpellAdjuster(Predicate<SpellAdjustingContext> executeCheck, BiConsumer<ISpellDefinition, LivingEntity> adjuster) {
        this.executeCheck = executeCheck;
        this.bi_adjuster = adjuster;
        this.adjuster = null;
    }

    public SpellAdjuster(Predicate<SpellAdjustingContext> executeCheck, Consumer<SpellAdjustingContext> adjuster) {
        this.executeCheck = executeCheck;
        this.bi_adjuster = null;
        this.adjuster = adjuster;
    }

    public boolean check(SpellCastStage stage, ItemStack stack, ISpellDefinition spellRecipe, @Nullable LivingEntity caster) {
        return this.executeCheck.test(new SpellAdjustingContext(stack, spellRecipe, caster, stage));
    }

    public void modify(ItemStack stack, ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (this.adjuster != null) {
            this.adjuster.accept(new SpellAdjustingContext(stack, recipe, caster, SpellCastStage.CASTING));
        } else if (this.bi_adjuster != null) {
            this.bi_adjuster.accept(recipe, caster);
        }
    }
}