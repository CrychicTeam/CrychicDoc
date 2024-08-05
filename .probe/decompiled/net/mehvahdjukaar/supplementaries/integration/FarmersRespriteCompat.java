package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.FarmersRespriteCompatImpl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FarmersRespriteCompat {

    @ExpectPlatform
    @Transformed
    public static IntegerProperty getWaterLevel() {
        return FarmersRespriteCompatImpl.getWaterLevel();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isKettle(BlockState block) {
        return FarmersRespriteCompatImpl.isKettle(block);
    }
}