package net.mehvahdjukaar.supplementaries.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.utils.forge.VibeCheckerImpl;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

public class VibeChecker {

    @ExpectPlatform
    @Transformed
    public static void checkVibe() {
        VibeCheckerImpl.checkVibe();
    }

    public static void checkVibe(Level level) {
        checkDatapackRegistry();
        if (PlatHelper.getPhysicalSide().isClient()) {
            clientStuff();
        }
    }

    public static void checkDatapackRegistry() {
        try {
            SoftFluidRegistry.getEmpty();
        } catch (Exception var1) {
            throw new RuntimeException("Not all required entries were found in datapack registry. How did this happen?Note that this could be caused by Paper or similar servers. Know that those are NOT meant to be used with mods", var1);
        }
    }

    private static void clientStuff() {
        for (ResourceKey<BannerPattern> v : BuiltInRegistries.BANNER_PATTERN.registryKeySet()) {
            if (!Sheets.BANNER_MATERIALS.containsKey(v)) {
                ArrayList<ResourceKey<BannerPattern>> keys = new ArrayList(BuiltInRegistries.BANNER_PATTERN.registryKeySet());
                keys.removeAll(Sheets.BANNER_MATERIALS.keySet());
                throw new VibeChecker.BadModError("Some OTHER mod loaded the Sheets class to early, causing modded banner patterns and sherds textures to not include modded ones.\nRefusing to proceed further. Do Not report this to Supplementaries\nMissing entries: " + keys + "\nCheck previous forge log lines to find the offending mod (if its a forge mod). Good luck if a connector mod caused this tho.");
            }
        }
        for (ResourceKey<String> vx : BuiltInRegistries.DECORATED_POT_PATTERNS.registryKeySet()) {
            if (!Sheets.DECORATED_POT_MATERIALS.containsKey(vx)) {
                ArrayList<ResourceKey<String>> keys = new ArrayList(BuiltInRegistries.DECORATED_POT_PATTERNS.registryKeySet());
                keys.removeAll(Sheets.DECORATED_POT_MATERIALS.keySet());
                throw new VibeChecker.BadModError("Some OTHER mod loaded the Sheets class to early, causing modded banner patterns and sherds textures to not include modded ones.\nRefusing to proceed further. Do Not report this to Supplementaries\nMissing entries: " + keys + "\nCheck previous forge log lines to find the offending mod (if its a forge mod). Good luck if a connector mod caused this tho.");
            }
        }
    }

    private static void crashWhenStolenMod() {
        String s = "creaturesfromthesnow";
        if (PlatHelper.isModLoaded(s)) {
            Supplementaries.LOGGER.error("[!!!] The mod " + s + " contains stolen assets and code from Frozen Up which is ARR.");
        }
    }

    public static class BadModError extends Error {

        public BadModError(String s) {
            super(s);
        }

        public BadModError(String s, Exception e) {
            super(s, e);
        }
    }
}