package ca.fxco.memoryleakfix.mixinextras;

import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasVersion;

public class MixinExtrasBootstrap {

    private static boolean initialized = false;

    @Deprecated
    public static String getVersion() {
        return MixinExtrasVersion.LATEST.toString();
    }

    public static void init() {
        if (!initialized) {
            initialized = true;
            MixinExtrasService.setup();
        }
    }
}