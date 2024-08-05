package io.github.lightman314.lightmanscurrency.mixin;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class LCMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = LogManager.getLogger();

    public void onLoad(String s) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        try {
            if (mixinClass.contains("compat")) {
                String[] splits = mixinClass.split("\\.");
                String modid = splits[splits.length - 2];
                LOGGER.debug("Compat mixin detected. Checking if '" + modid + "' is loaded!");
                boolean loaded = FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod -> mod.getModId().equals(modid));
                if (loaded) {
                    LOGGER.debug("Mod was loaded. Applying mixin.");
                } else {
                    LOGGER.debug("Mod was not loaded. Will not apply the mixin.");
                }
                return loaded;
            } else {
                return true;
            }
        } catch (Throwable var6) {
            return false;
        }
    }

    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}