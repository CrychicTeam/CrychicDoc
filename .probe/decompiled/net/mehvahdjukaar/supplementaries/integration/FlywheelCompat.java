package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.FlywheelCompatImpl;

public class FlywheelCompat {

    @ExpectPlatform
    @Transformed
    public static void setupClient() {
        FlywheelCompatImpl.setupClient();
    }
}