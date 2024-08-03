package net.minecraft.world.item;

import net.minecraft.resources.ResourceLocation;

public class HorseArmorItem extends Item {

    private static final String TEX_FOLDER = "textures/entity/horse/";

    private final int protection;

    private final String texture;

    public HorseArmorItem(int int0, String string1, Item.Properties itemProperties2) {
        super(itemProperties2);
        this.protection = int0;
        this.texture = "textures/entity/horse/armor/horse_armor_" + string1 + ".png";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(this.texture);
    }

    public int getProtection() {
        return this.protection;
    }
}