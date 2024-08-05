package malte0811.ferritecore.mixin.config;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

public abstract class FerriteMixinConfig implements IMixinConfigPlugin {

    protected static final Logger LOGGER = LogManager.getLogger("ferritecore-mixin");

    private static final boolean HAS_LITHIUM = hasClass("me.jellysquid.mods.lithium.common.LithiumMod");

    private static final boolean HAS_ROADRUNNER = hasClass("me.jellysquid.mods.lithium.common.RoadRunner");

    private String prefix = null;

    private final FerriteConfig.Option enableOption;

    private final FerriteMixinConfig.LithiumSupportState lithiumState;

    private final boolean optIn;

    protected FerriteMixinConfig(FerriteConfig.Option enableOption, FerriteMixinConfig.LithiumSupportState lithiumCompat, boolean optIn) {
        this.enableOption = enableOption;
        this.lithiumState = lithiumCompat;
        this.optIn = optIn;
    }

    protected FerriteMixinConfig(FerriteConfig.Option enableOption) {
        this(enableOption, FerriteMixinConfig.LithiumSupportState.NO_CONFLICT, false);
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Preconditions.checkState(mixinClassName.startsWith(this.prefix), "Unexpected prefix on " + mixinClassName);
        if (!this.enableOption.isEnabled()) {
            if (!this.optIn) {
                LOGGER.warn("Mixin " + mixinClassName + " is disabled by config");
            }
            return false;
        } else if (!this.lithiumState.shouldApply()) {
            LOGGER.warn("Mixin " + mixinClassName + " is disabled automatically as lithium is installed");
            return false;
        } else {
            if (this.optIn) {
                LOGGER.warn("Opt-in mixin {} is enabled by config", mixinClassName);
            }
            return true;
        }
    }

    public void onLoad(String mixinPackage) {
        this.prefix = mixinPackage + ".";
    }

    public String getRefMapperConfig() {
        return null;
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

    private static boolean hasClass(String name) {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode(name);
            return true;
        } catch (IOException | ClassNotFoundException var2) {
            return false;
        }
    }

    protected static enum LithiumSupportState {

        NO_CONFLICT, INCOMPATIBLE, APPLY_IF_ROADRUNNER;

        private boolean shouldApply() {
            return switch(this) {
                case NO_CONFLICT ->
                    true;
                case INCOMPATIBLE ->
                    !FerriteMixinConfig.HAS_LITHIUM;
                case APPLY_IF_ROADRUNNER ->
                    !FerriteMixinConfig.HAS_LITHIUM || FerriteMixinConfig.HAS_ROADRUNNER;
            };
        }
    }
}