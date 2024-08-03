package org.embeddedt.embeddium.render.chunk;

import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.ColorMixer;
import org.embeddedt.embeddium.render.ShaderModBridge;

public interface ChunkColorWriter {

    ChunkColorWriter LEGACY = ColorABGR::withAlpha;

    ChunkColorWriter EMBEDDIUM = (color, ao) -> ColorMixer.mulSingleWithoutAlpha(color, (int) (ao * 255.0F));

    int writeColor(int var1, float var2);

    static ChunkColorWriter get() {
        return ShaderModBridge.emulateLegacyColorBrightnessFormat() ? LEGACY : EMBEDDIUM;
    }
}