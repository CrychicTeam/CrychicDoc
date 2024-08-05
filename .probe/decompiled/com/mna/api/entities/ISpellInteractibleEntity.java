package com.mna.api.entities;

import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellSource;
import net.minecraft.world.entity.Entity;

public interface ISpellInteractibleEntity<T extends Entity> {

    boolean onShapeTarget(ISpellDefinition var1, SpellSource var2);
}