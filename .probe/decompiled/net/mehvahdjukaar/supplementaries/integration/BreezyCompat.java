package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.BreezyCompatImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BreezyCompat {

    @ExpectPlatform
    @Transformed
    public static float getWindDirection(BlockPos pos, Level level) {
        return BreezyCompatImpl.getWindDirection(pos, level);
    }
}