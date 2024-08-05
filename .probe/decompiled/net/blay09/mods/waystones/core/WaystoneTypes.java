package net.blay09.mods.waystones.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

public class WaystoneTypes {

    public static final ResourceLocation WAYSTONE = new ResourceLocation("waystones", "waystone");

    public static final ResourceLocation WARP_PLATE = new ResourceLocation("waystones", "warp_plate");

    public static final ResourceLocation PORTSTONE = new ResourceLocation("waystones", "portstone");

    private static final ResourceLocation SHARESTONE = new ResourceLocation("waystones", "sharestone");

    public static ResourceLocation getSharestone(@Nullable DyeColor color) {
        return color == null ? SHARESTONE : new ResourceLocation("waystones", color.getSerializedName() + "_sharestone");
    }

    public static boolean isSharestone(ResourceLocation waystoneType) {
        return waystoneType.equals(SHARESTONE) || waystoneType.getPath().endsWith("_sharestone");
    }
}