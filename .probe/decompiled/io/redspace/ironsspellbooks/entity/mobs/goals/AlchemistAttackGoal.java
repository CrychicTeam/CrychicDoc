package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

public class AlchemistAttackGoal extends WizardAttackGoal {

    protected float throwRangeSqr;

    protected float throwRange;

    protected float potionBias;

    public static final List<MobEffect> ATTACK_POTIONS = List.of(MobEffects.WEAKNESS, MobEffects.BLINDNESS, MobEffects.LEVITATION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN);

    public AlchemistAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float throwRange, float potionBias) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.throwRange = throwRange;
        this.throwRangeSqr = throwRange * throwRange;
        this.attackRadius = throwRange - 2.0F;
        this.attackRadiusSqr = this.attackRadius * this.attackRadius;
        this.potionBias = potionBias;
    }

    public AlchemistAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells, List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (AlchemistAttackGoal) super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    public AlchemistAttackGoal setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        return (AlchemistAttackGoal) super.setSpellQuality(minSpellQuality, maxSpellQuality);
    }

    public AlchemistAttackGoal setSingleUseSpell(AbstractSpell spellType, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (AlchemistAttackGoal) super.setSingleUseSpell(spellType, minDelay, maxDelay, minLevel, maxLevel);
    }

    public AlchemistAttackGoal setIsFlying() {
        return (AlchemistAttackGoal) super.setIsFlying();
    }

    @Override
    protected void doSpellAction() {
        if (this.mob.m_20280_(this.target) < (double) this.throwRangeSqr && this.mob.m_217043_().nextFloat() < this.potionBias) {
            int attackWeight = this.getAttackWeight();
            int supportWeight = this.getSupportWeight();
            ItemStack potion = new ItemStack(Items.SPLASH_POTION);
            LivingEntity targetedEntity = this.target;
            if (this.hasLineOfSight && this.mob.m_217043_().nextFloat() * (float) (attackWeight + supportWeight) > (float) supportWeight) {
                int amplifier = (this.mob.m_217043_().nextFloat() < 0.75F ? 0 : 1) + (this.target.getMaxHealth() > 30.0F ? (this.mob.m_217043_().nextFloat() < 0.5F ? 0 : 1) : 0);
                MobEffect effect = this.target.isInvertedHealAndHarm() ? MobEffects.HEAL : MobEffects.HARM;
                if (this.mob.m_217043_().nextFloat() < 0.45F) {
                    for (int i = 0; i < ATTACK_POTIONS.size(); i++) {
                        int p = this.mob.m_217043_().nextInt(ATTACK_POTIONS.size());
                        if (!this.target.hasEffect((MobEffect) ATTACK_POTIONS.get(p))) {
                            effect = (MobEffect) ATTACK_POTIONS.get(p);
                            break;
                        }
                    }
                }
                PotionUtils.setCustomEffects(potion, List.of(new MobEffectInstance(effect, effect.isInstantenous() ? 0 : 200, amplifier)));
                PotionUtils.setPotion(potion, Potions.WATER);
            } else {
                PotionUtils.setPotion(potion, Potions.STRONG_HEALING);
                targetedEntity = this.mob;
            }
            ThrownPotion thrownpotion = new ThrownPotion(this.mob.f_19853_, this.mob);
            thrownpotion.m_37446_(potion);
            thrownpotion.m_146926_(thrownpotion.m_146909_() - -20.0F);
            Vec3 vec3 = targetedEntity.m_20184_();
            double d0 = targetedEntity.m_20185_() + vec3.x - this.mob.m_20185_();
            double d1 = targetedEntity.m_20188_() - 1.1F - this.mob.m_20188_();
            double d2 = targetedEntity.m_20189_() + vec3.z - this.mob.m_20189_();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            thrownpotion.m_6686_(d0, d1 + d3 * 0.2, d2, (float) Mth.clampedLerp(0.5, 1.25, this.mob.m_20280_(targetedEntity) / (double) this.throwRangeSqr), 8.0F);
            if (!this.mob.m_20067_()) {
                this.mob.f_19853_.playSound(null, this.mob.m_20185_(), this.mob.m_20186_(), this.mob.m_20189_(), SoundEvents.WITCH_THROW, this.mob.m_5720_(), 1.0F, 0.8F + this.mob.m_217043_().nextFloat() * 0.4F);
            }
            this.mob.f_19853_.m_7967_(thrownpotion);
            this.mob.m_21011_(InteractionHand.MAIN_HAND, true);
        } else {
            super.doSpellAction();
        }
    }
}