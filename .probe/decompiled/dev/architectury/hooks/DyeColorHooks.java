package dev.architectury.hooks;

import dev.architectury.hooks.forge.DyeColorHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.world.item.DyeColor;

public class DyeColorHooks {

    private DyeColorHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static int getColorValue(DyeColor color) {
        return DyeColorHooksImpl.getColorValue(color);
    }
}