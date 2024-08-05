package org.violetmoon.quark.content.client.resources;

import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum AttributeSlot {

    MAINHAND(EquipmentSlot.MAINHAND),
    OFFHAND(EquipmentSlot.OFFHAND),
    FEET(EquipmentSlot.FEET),
    LEGS(EquipmentSlot.LEGS),
    CHEST(EquipmentSlot.CHEST),
    HEAD(EquipmentSlot.HEAD),
    POTION("potion.whenDrank");

    @Nullable
    private final EquipmentSlot canonicalSlot;

    private final String locKey;

    private AttributeSlot(@Nullable EquipmentSlot canonicalSlot, String locKey) {
        this.canonicalSlot = canonicalSlot;
        this.locKey = locKey;
    }

    private AttributeSlot(String locKey) {
        this(null, locKey);
    }

    private AttributeSlot(@NotNull EquipmentSlot canonicalSlot) {
        this(canonicalSlot, "item.modifiers." + canonicalSlot.getName());
    }

    public boolean hasCanonicalSlot() {
        return this.canonicalSlot != null;
    }

    @NotNull
    public EquipmentSlot getCanonicalSlot() {
        if (this.canonicalSlot == null) {
            throw new IllegalStateException("Potions have no canonical slot");
        } else {
            return this.canonicalSlot;
        }
    }

    public String getTranslationKey() {
        return this.locKey;
    }

    public static AttributeSlot fromCanonicalSlot(@NotNull EquipmentSlot slot) {
        switch(slot) {
            case OFFHAND:
                return OFFHAND;
            case FEET:
                return FEET;
            case LEGS:
                return LEGS;
            case CHEST:
                return CHEST;
            case HEAD:
                return HEAD;
            default:
                return MAINHAND;
        }
    }
}