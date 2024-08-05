package com.mna.api.entities.construct;

import com.mojang.datafixers.util.Pair;

public enum ConstructCapability {

    ITEM_STORAGE(0, 245),
    CARRY(11, 245),
    CRAFT(22, 245),
    SMITH(33, 245),
    MINE(44, 245),
    CHOP_WOOD(55, 245),
    CAST_SPELL(66, 245),
    SHEAR(77, 245),
    HARVEST(88, 245),
    MELEE_ATTACK(99, 245),
    RANGED_ATTACK(110, 245),
    BLOCK(121, 245),
    TAUNT(132, 245),
    FLUID_STORE(143, 245),
    FLUID_DISPENSE(154, 245),
    PLANT(165, 245),
    STORE_MANA(176, 245),
    CARRY_PLAYER(187, 245),
    TELEPORT(198, 245),
    FLY(209, 245),
    ADVENTURE(220, 245),
    FISH(231, 245);

    private final int iconX;

    private final int iconY;

    private ConstructCapability(int iconX, int iconY) {
        this.iconX = iconX;
        this.iconY = iconY;
    }

    public Pair<Integer, Integer> getIconCoords() {
        return new Pair(this.iconX, this.iconY);
    }
}