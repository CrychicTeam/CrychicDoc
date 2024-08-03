package io.github.lightman314.lightmanscurrency.integration;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModList;

public class IntegrationUtil {

    public static void SafeRunIfLoaded(@Nonnull String modid, @Nonnull Runnable runnable, @Nullable String error) {
        try {
            if (ModList.get().isLoaded(modid)) {
                runnable.run();
            }
        } catch (Throwable var4) {
            if (error != null) {
                LightmansCurrency.LogError(error, var4);
            }
        }
    }
}