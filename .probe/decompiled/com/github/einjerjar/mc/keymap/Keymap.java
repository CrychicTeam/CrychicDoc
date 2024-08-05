package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.Services;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySources;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.objects.Credits;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Keymap {

    public static final String MOD_ID = "keymap";

    public static final String SERVER_WARN = "Keymap is being ran on a DedicatedServer environment, even though it can only work on Client side environment";

    protected static final String MOD_NAME = "keymap";

    protected static final Logger logger = LogManager.getLogger();

    protected static KeyMapping kmOpenMapper;

    private Keymap() {
    }

    public static void init() {
        KeymapConfig.load();
        logger.info("Keymap loaded, loader={}, dev={}", Services.PLATFORM.loader(), Services.PLATFORM.dev());
        kmOpenMapper = Services.KEYBIND.create(InputConstants.Type.KEYSYM, 96, "keymap.keyOpenKeymap", "keymap.keyCat");
        for (KeyLayout keyLayout : KeyLayout.layouts().values()) {
            logger.debug("Layout for {} @ {}", keyLayout.meta().code(), keyLayout.meta().name());
        }
        KeymapSources.collect();
        CategorySources.collect();
        Services.TICK.registerEndClientTick(client -> {
            while (kmOpenMapper.consumeClick()) {
                client.setScreen(new KeymapScreen(null));
            }
        });
        logger.warn(Credits.instance().toString());
    }

    public static String MOD_NAME() {
        return "keymap";
    }

    public static Logger logger() {
        return logger;
    }

    public static KeyMapping kmOpenMapper() {
        return kmOpenMapper;
    }
}