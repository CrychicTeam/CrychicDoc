package top.prefersmin.waystoneandlightman.mixin;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin implements IMixinConfigPlugin {

    private boolean isFrameworkInstalled;

    public void onLoad(String mixinPackage) {
        try {
            Class.forName("top.prefersmin.waystoneandlightman.WayStoneAndLightMan", false, this.getClass().getClassLoader());
            this.isFrameworkInstalled = true;
        } catch (Exception var3) {
            this.isFrameworkInstalled = false;
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.isFrameworkInstalled;
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