package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class ArenaTrait extends AuraEffectTrait {

    public ArenaTrait() {
        super(LHEffects.ANTIBUILD::get);
    }

    @Override
    protected boolean canApply(LivingEntity e) {
        return true;
    }

    @Override
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                if (attacker.hasEffect((MobEffect) LHEffects.ANTIBUILD.get())) {
                    return;
                }
                if (attacker instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob) && ((MobTraitCap) MobTraitCap.HOLDER.get(mob)).getTraitLevel(this) >= level) {
                    return;
                }
            }
            event.setCanceled(true);
        }
    }

    @Override
    public void onDamaged(int level, LivingEntity mob, AttackCache cache) {
        LivingDamageEvent event = cache.getLivingDamageEvent();
        assert event != null;
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (cache.getAttacker() == null || !cache.getAttacker().hasEffect((MobEffect) LHEffects.ANTIBUILD.get())) {
                cache.addDealtModifier(DamageModifier.nonlinearFinal(12345, e -> 0.0F));
            }
        }
    }
}