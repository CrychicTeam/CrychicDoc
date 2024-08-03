package com.rekindled.embers.util;

import com.google.common.collect.Maps;
import java.text.DecimalFormat;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DecimalFormats implements ResourceManagerReloadListener {

    private static final Map<String, DecimalFormat> decimalFormats = Maps.newHashMap();

    public static DecimalFormat getDecimalFormat(String key) {
        DecimalFormat format = (DecimalFormat) decimalFormats.get(key);
        if (format == null) {
            if (I18n.exists(key)) {
                format = new DecimalFormat(I18n.get(key));
            } else {
                format = new DecimalFormat("0.#######");
            }
            decimalFormats.put(key, format);
        }
        return format;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        decimalFormats.clear();
    }
}