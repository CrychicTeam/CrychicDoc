package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.WaystonesCompatImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class WaystonesCompat {

    @ExpectPlatform
    @Transformed
    public static boolean isWaystone(@Nullable BlockEntity te) {
        return WaystonesCompatImpl.isWaystone(te);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static Component getName(BlockEntity te) {
        return WaystonesCompatImpl.getName(te);
    }
}