package dev.architectury.event;

import dev.architectury.event.forge.EventHandlerImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class EventHandler {

    private static boolean initialized = false;

    private EventHandler() {
    }

    public static void init() {
        if (!initialized) {
            initialized = true;
            if (Platform.getEnvironment() == Env.CLIENT) {
                registerClient();
            }
            registerCommon();
            if (Platform.getEnvironment() == Env.SERVER) {
                registerServer();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    @Transformed
    private static void registerClient() {
        EventHandlerImpl.registerClient();
    }

    @ExpectPlatform
    @Transformed
    private static void registerCommon() {
        EventHandlerImpl.registerCommon();
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @ExpectPlatform
    @Transformed
    private static void registerServer() {
        EventHandlerImpl.registerServer();
    }
}