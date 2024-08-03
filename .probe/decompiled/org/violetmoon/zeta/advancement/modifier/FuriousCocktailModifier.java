package org.violetmoon.zeta.advancement.modifier;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.violetmoon.zeta.advancement.AdvancementModifier;
import org.violetmoon.zeta.api.IMutableAdvancement;
import org.violetmoon.zeta.module.ZetaModule;

public class FuriousCocktailModifier extends AdvancementModifier {

    private static final ResourceLocation TARGET_AP = new ResourceLocation("nether/all_potions");

    private static final ResourceLocation TARGET_AE = new ResourceLocation("nether/all_effects");

    final BooleanSupplier isPotion;

    final Set<MobEffect> effects;

    public FuriousCocktailModifier(ZetaModule module, BooleanSupplier isPotion, Set<MobEffect> effects) {
        super(module);
        this.isPotion = isPotion;
        this.effects = effects;
    }

    @Override
    public Set<ResourceLocation> getTargets() {
        return ImmutableSet.of(TARGET_AP, TARGET_AE);
    }

    @Override
    public boolean apply(ResourceLocation res, IMutableAdvancement adv) {
        if (!this.isPotion.getAsBoolean() && res.equals(TARGET_AP)) {
            return false;
        } else {
            Criterion crit = adv.getCriterion("all_effects");
            if (crit != null && crit.getTrigger() instanceof EffectsChangedTrigger.TriggerInstance ect) {
                for (MobEffect e : this.effects) {
                    ect.effects.and(e);
                }
                return true;
            } else {
                return false;
            }
        }
    }
}