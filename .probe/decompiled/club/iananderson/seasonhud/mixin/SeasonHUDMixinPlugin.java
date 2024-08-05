package club.iananderson.seasonhud.mixin;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

public class SeasonHUDMixinPlugin implements IMixinConfigPlugin {

    private static final boolean HAS_XAERO = hasClass("xaero.common.HudMod");

    private static final boolean HAS_FTB = hasClass("dev.ftb.mods.ftbchunks.FTBChunks");

    private String prefix = null;

    private static boolean hasClass(String modClass) {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode(modClass);
            return true;
        } catch (IOException | ClassNotFoundException var2) {
            return false;
        }
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Preconditions.checkState(mixinClassName.startsWith(this.prefix), "Unexpected prefix on " + mixinClassName);
        if (mixinClassName.startsWith("club.iananderson.seasonhud.mixin.xaero")) {
            return HAS_XAERO;
        } else {
            return mixinClassName.startsWith("club.iananderson.seasonhud.mixin.ftbchunks") ? HAS_FTB : true;
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
}