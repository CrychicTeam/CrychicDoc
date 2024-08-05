package io.github.apace100.origins.networking;

import io.github.apace100.origins.Origins;
import net.minecraft.resources.ResourceLocation;

public class ModPackets {

    public static final ResourceLocation OPEN_ORIGIN_SCREEN = new ResourceLocation("origins", "open_origin_screen");

    public static final ResourceLocation CHOOSE_ORIGIN = new ResourceLocation("origins", "choose_origin");

    public static final ResourceLocation USE_ACTIVE_POWERS = new ResourceLocation("origins", "use_active_powers");

    public static final ResourceLocation ORIGIN_LIST = new ResourceLocation("origins", "origin_list");

    public static final ResourceLocation LAYER_LIST = new ResourceLocation("origins", "layer_list");

    public static final ResourceLocation POWER_LIST = new ResourceLocation("origins", "power_list");

    public static final ResourceLocation CHOOSE_RANDOM_ORIGIN = new ResourceLocation("origins", "choose_random_origin");

    public static final ResourceLocation CONFIRM_ORIGIN = Origins.identifier("confirm_origin");

    public static final ResourceLocation PLAYER_LANDED = Origins.identifier("player_landed");

    public static final ResourceLocation BADGE_LIST = Origins.identifier("badge_list");
}