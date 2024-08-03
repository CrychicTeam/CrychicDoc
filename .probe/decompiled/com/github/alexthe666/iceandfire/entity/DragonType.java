package com.github.alexthe666.iceandfire.entity;

import net.minecraft.world.entity.EntityType;

public class DragonType {

    public static final DragonType FIRE = new DragonType("fire");

    public static final DragonType ICE = new DragonType("ice").setPiscivore();

    public static final DragonType LIGHTNING = new DragonType("lightning");

    private String name;

    private boolean piscivore;

    public DragonType(String name) {
        this.name = name;
    }

    public static String getNameFromInt(int type) {
        if (type == 2) {
            return "lightning";
        } else {
            return type == 1 ? "ice" : "fire";
        }
    }

    public int getIntFromType() {
        if (this == LIGHTNING) {
            return 2;
        } else {
            return this == ICE ? 1 : 0;
        }
    }

    public EntityType<? extends EntityDragonBase> getEntity() {
        if (this == LIGHTNING) {
            return IafEntityRegistry.LIGHTNING_DRAGON.get();
        } else {
            return this == ICE ? IafEntityRegistry.ICE_DRAGON.get() : IafEntityRegistry.FIRE_DRAGON.get();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPiscivore() {
        return this.piscivore;
    }

    public DragonType setPiscivore() {
        this.piscivore = true;
        return this;
    }
}