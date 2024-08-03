package org.violetmoon.quark.content.client.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.tooltip.AttributeTooltips;

public class AttributeTooltipManager extends SimplePreparableReloadListener<Map<String, AttributeIconEntry>> {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(AttributeIconEntry.class, AttributeIconEntry.Serializer.INSTANCE).create();

    private static final Logger LOGGER = Quark.LOG;

    private static final TypeToken<Map<String, AttributeIconEntry>> ATTRIBUTE_ICON_ENTRY_TYPE = new TypeToken<Map<String, AttributeIconEntry>>() {
    };

    @NotNull
    protected Map<String, AttributeIconEntry> prepare(@NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        Map<String, AttributeIconEntry> tooltips = new HashMap();
        profiler.startTick();
        try {
            for (Resource resource : manager.getResourceStack(new ResourceLocation("quark", "attribute_tooltips.json"))) {
                profiler.push(resource.sourcePackId());
                try {
                    InputStream stream = resource.open();
                    try {
                        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                        try {
                            profiler.push("parse");
                            Map<String, AttributeIconEntry> map = GsonHelper.fromJson(GSON, reader, ATTRIBUTE_ICON_ENTRY_TYPE);
                            profiler.popPush("register");
                            if (map != null) {
                                tooltips.putAll(map);
                            }
                            profiler.pop();
                        } catch (Throwable var12) {
                            try {
                                reader.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                            throw var12;
                        }
                        reader.close();
                    } catch (Throwable var13) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var10) {
                                var13.addSuppressed(var10);
                            }
                        }
                        throw var13;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (RuntimeException var14) {
                    LOGGER.warn("Invalid {} in resourcepack: '{}'", "attribute_tooltips.json", resource.sourcePackId(), var14);
                }
                profiler.pop();
            }
        } catch (IOException var15) {
        }
        profiler.endTick();
        return tooltips;
    }

    protected void apply(@NotNull Map<String, AttributeIconEntry> tooltips, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        AttributeTooltips.receiveAttributes(tooltips);
    }
}