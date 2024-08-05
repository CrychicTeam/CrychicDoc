package com.mna.api.spells.base;

import java.util.UUID;
import net.minecraft.world.entity.Entity;

public interface ISpellSigil<T extends Entity> {

    UUID getID();

    int getCountBonus();
}