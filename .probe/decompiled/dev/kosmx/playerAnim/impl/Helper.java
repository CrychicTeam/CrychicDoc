package dev.kosmx.playerAnim.impl;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.kosmx.playerAnim.impl.forge.HelperImpl;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class Helper {

    @Nullable
    private static AtomicBoolean isBendyLibLoaded = null;

    public static boolean isBendEnabled() {
        if (isBendyLibLoaded == null) {
            isBendyLibLoaded = new AtomicBoolean(isBendyLibPresent());
        }
        return isBendyLibLoaded.get();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isBendyLibPresent() {
        return HelperImpl.isBendyLibPresent();
    }
}