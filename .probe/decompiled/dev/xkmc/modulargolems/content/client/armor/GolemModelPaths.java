package dev.xkmc.modulargolems.content.client.armor;

import net.minecraft.resources.ResourceLocation;

public class GolemModelPaths {

    public static final ResourceLocation HELMETS = modLoc("helmet");

    public static final ResourceLocation CHESTPLATES = modLoc("chestplate");

    public static final ResourceLocation LEGGINGS = modLoc("shinguard");

    public static ResourceLocation modLoc(String str) {
        return new ResourceLocation("modulargolems", str);
    }
}