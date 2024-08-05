package com.illusivesoulworks.polymorph.mixin;

import com.illusivesoulworks.polymorph.PolymorphConstants;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import com.illusivesoulworks.polymorph.platform.Services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler.ErrorAction;

public class IntegratedMixinPlugin implements IMixinConfigPlugin, IMixinErrorHandler {

    private static final Map<String, String> CLASS_TO_MOD = new HashMap();

    private static boolean isConfigLoaded = false;

    public void onLoad(String mixinPackage) {
        Mixins.registerErrorHandlerClass("com.illusivesoulworks.polymorph.mixin.IntegratedMixinPlugin");
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isConfigLoaded) {
            PolymorphIntegrations.loadConfig();
            isConfigLoaded = true;
        }
        for (Entry<String, String> entry : CLASS_TO_MOD.entrySet()) {
            String modId = (String) entry.getValue();
            if (targetClassName.startsWith((String) entry.getKey())) {
                return PolymorphIntegrations.isActive(modId) && Services.PLATFORM.isModFileLoaded(modId);
            }
        }
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public ErrorAction onPrepareError(IMixinConfig config, Throwable th, IMixinInfo mixin, ErrorAction action) {
        return null;
    }

    public ErrorAction onApplyError(String targetClassName, Throwable th, IMixinInfo mixin, ErrorAction action) {
        if (!mixin.getConfig().getMixinPackage().startsWith("com.illusivesoulworks.polymorph.mixin.integration")) {
            return null;
        } else {
            String modId = "{MOD NOT FOUND - THIS SHOULD NOT HAPPEN}";
            for (Entry<String, String> entry : CLASS_TO_MOD.entrySet()) {
                String id = (String) entry.getValue();
                if (targetClassName.startsWith((String) entry.getKey())) {
                    modId = id;
                    break;
                }
            }
            PolymorphIntegrations.disable(modId);
            PolymorphConstants.LOG.error("Polymorph encountered an error while transforming: {}", targetClassName);
            PolymorphConstants.LOG.error("The integration module for {} will be disabled.", modId);
            PolymorphConstants.LOG.error("Please report this bug to Polymorph only, do not report this to {}.", modId);
            return ErrorAction.WARN;
        }
    }

    static {
        CLASS_TO_MOD.put("dev.shadowsoffire.fastbench.", PolymorphIntegrations.Mod.FASTWORKBENCH.getId());
        CLASS_TO_MOD.put("dev.shadowsoffire.fastsuite.", PolymorphIntegrations.Mod.FASTSUITE.getId());
        CLASS_TO_MOD.put("tfar.fastbench.", PolymorphIntegrations.Mod.QUICKBENCH.getId());
    }
}