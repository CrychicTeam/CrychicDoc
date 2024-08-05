package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class DebugWizardAttackGoal extends Goal {

    private final PathfinderMob mob;

    protected final IMagicEntity spellCastingMob;

    private final AbstractSpell spell;

    private final int spellLevel;

    private final int cancelCastAfterTicks;

    private int tickCount = 0;

    private AbstractSpell castingSpell;

    private int castingTicks = 0;

    public DebugWizardAttackGoal(IMagicEntity abstractSpellCastingMob, AbstractSpell spell, int spellLevel, int cancelCastAfterTicks) {
        this.spellCastingMob = abstractSpellCastingMob;
        if (abstractSpellCastingMob instanceof PathfinderMob m) {
            this.mob = m;
            this.spell = spell;
            this.spellLevel = spellLevel;
            this.cancelCastAfterTicks = cancelCastAfterTicks;
        } else {
            throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.tickCount++ % 200 == 0) {
            IronsSpellbooks.LOGGER.debug("DebugWizardAttackGoal:  mob.initiateCastSpell:{}({}), pos:{}, isCasting:{}, isClient:{}", new Object[] { this.spell.getSpellId(), this.spellLevel, this.mob.m_20182_(), this.spellCastingMob.isCasting(), this.mob.f_19853_.isClientSide() });
            this.spellCastingMob.initiateCastSpell(this.spell, this.spellLevel);
            this.castingTicks = 0;
        }
        if (this.spellCastingMob.isCasting()) {
            this.castingTicks++;
            if (this.cancelCastAfterTicks == this.castingTicks) {
                this.spellCastingMob.cancelCast();
            }
        }
    }
}