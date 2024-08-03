package org.embeddedt.modernfix.searchtree;

import java.util.Optional;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.library.runtime.JeiRuntime;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIRuntimeCapturer implements IModPlugin {

    private static JeiRuntime runtimeHandle = null;

    public static Optional<JeiRuntime> runtime() {
        return Optional.ofNullable(runtimeHandle);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("modernfix", "capturer");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtimeHandle = (JeiRuntime) jeiRuntime;
    }

    @Override
    public void onRuntimeUnavailable() {
        runtimeHandle = null;
    }
}