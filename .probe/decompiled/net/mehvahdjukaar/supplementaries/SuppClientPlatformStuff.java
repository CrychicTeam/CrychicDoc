package net.mehvahdjukaar.supplementaries;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.ISlider;
import net.mehvahdjukaar.supplementaries.forge.SuppClientPlatformStuffImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SuppClientPlatformStuff {

    @ExpectPlatform
    @Transformed
    public static RenderType staticNoise(ResourceLocation location) {
        return SuppClientPlatformStuffImpl.staticNoise(location);
    }

    @ExpectPlatform
    @Transformed
    public static ShaderInstance getNoiseShader() {
        return SuppClientPlatformStuffImpl.getNoiseShader();
    }

    @ExpectPlatform
    @Transformed
    public static ISlider createSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        return SuppClientPlatformStuffImpl.createSlider(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
    }
}