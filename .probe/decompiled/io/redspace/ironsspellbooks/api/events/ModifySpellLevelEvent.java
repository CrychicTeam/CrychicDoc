package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class ModifySpellLevelEvent extends Event {

    final AbstractSpell spell;

    final LivingEntity caster;

    final int baseLevel;

    int totalLevel;

    public ModifySpellLevelEvent(AbstractSpell spell, LivingEntity caster, int baseLevel, int totalLevel) {
        this.spell = spell;
        this.caster = caster;
        this.baseLevel = baseLevel;
        this.totalLevel = totalLevel;
    }

    public int getBaseLevel() {
        return this.baseLevel;
    }

    public int getLevel() {
        return this.totalLevel;
    }

    public void setLevel(int level) {
        this.totalLevel = level;
    }

    public void addLevels(int levels) {
        this.totalLevel += levels;
    }

    public AbstractSpell getSpell() {
        return this.spell;
    }

    @Nullable
    public LivingEntity getEntity() {
        return this.caster;
    }

    public boolean isCancelable() {
        return false;
    }
}