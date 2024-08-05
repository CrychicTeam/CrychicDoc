package net.zanckor.questapi.multiloader.platform;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.zanckor.questapi.multiloader.platform.services.IPlatformHelper;
import net.zanckor.questapi.multiloader.platform.services.PlatformEnum;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.FORGE;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}