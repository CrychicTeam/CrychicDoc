package com.github.einjerjar.mc.keymapforge;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import java.io.File;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("keymap")
public class KeymapForge {

    public KeymapForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> KeymapForge.IDK::clientInit);
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> KeymapForge.IDK::serverInit);
    }

    public static File configDirProvider(String name) {
        return new File(FMLPaths.GAMEDIR.get().resolve("config/" + name).toUri());
    }

    public static class IDK {

        private IDK() {
        }

        private static void serverInit() {
            Keymap.logger().warn("Keymap is being ran on a DedicatedServer environment, even though it can only work on Client side environment");
        }

        private static void clientInit() {
            Keymap.init();
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new ConfigScreen(parent)));
        }
    }
}