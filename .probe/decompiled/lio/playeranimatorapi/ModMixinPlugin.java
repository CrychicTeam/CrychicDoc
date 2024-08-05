package lio.playeranimatorapi;

import java.util.List;
import java.util.Set;
import lio.liosmultiloaderutils.utils.Platform;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ModMixinPlugin implements IMixinConfigPlugin {

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return mixinClassName.endsWith("_LioLibOnly") && !Platform.isModLoaded("liolib", "net.liopyu.liolib.LioLib") ? false : !mixinClassName.equals("lio.playeranimatorapi.mixin.LivingEntityRendererMixin") || !Platform.isModLoaded("liolib", "net.liopyu.liolib.LioLib");
    }

    public void onLoad(String mixinPackage) {
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