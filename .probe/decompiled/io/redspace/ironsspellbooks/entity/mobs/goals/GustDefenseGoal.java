package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;

public class GustDefenseGoal extends Goal {

    protected final PathfinderMob mob;

    protected final IMagicEntity spellCastingMob;

    protected int attackCooldown = 0;

    public GustDefenseGoal(IMagicEntity abstractSpellCastingMob) {
        this.spellCastingMob = abstractSpellCastingMob;
        if (abstractSpellCastingMob instanceof PathfinderMob m) {
            this.mob = m;
        } else {
            throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.mob.m_5448_();
        return livingentity != null && --this.attackCooldown <= 0 && livingentity.isAlive() && this.shouldAreaAttack(livingentity) ? false : false;
    }

    public boolean shouldAreaAttack(LivingEntity livingEntity) {
        if (this.spellCastingMob.isCasting()) {
            return false;
        } else {
            double d = livingEntity.m_20280_(this.mob);
            boolean inRange = d < 25.0;
            if (!inRange) {
                return false;
            } else if (livingEntity.m_6095_() == EntityType.VINDICATOR) {
                this.start();
                return false;
            } else if (this.mob.m_21223_() / this.mob.m_21233_() < 0.25F && this.mob.f_19853_.getEntities(this.mob, this.mob.m_20191_().inflate(3.0), entity -> entity instanceof Enemy).size() > 1) {
                this.start();
                return false;
            } else {
                int mobCount = livingEntity.f_19853_.getEntities(livingEntity, livingEntity.m_20191_().inflate(6.0), entity -> entity instanceof Enemy).size();
                if (mobCount >= 2) {
                    this.start();
                }
                return false;
            }
        }
    }

    @Override
    public void start() {
        this.attackCooldown = 40 + this.mob.m_217043_().nextInt(30);
        int spellLevel = (int) ((float) SpellRegistry.GUST_SPELL.get().getMaxLevel() * 0.5F);
        AbstractSpell spellType = SpellRegistry.GUST_SPELL.get();
        this.spellCastingMob.initiateCastSpell(spellType, spellLevel);
    }
}