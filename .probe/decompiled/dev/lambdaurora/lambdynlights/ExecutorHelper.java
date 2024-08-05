package dev.lambdaurora.lambdynlights;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

class ExecutorHelper {

    public static void onInitializeClient() {
        DynLightsResourceListener reloadListener = new DynLightsResourceListener();
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }
        DynamicLightHandlers.registerDefaultHandlers();
    }
}