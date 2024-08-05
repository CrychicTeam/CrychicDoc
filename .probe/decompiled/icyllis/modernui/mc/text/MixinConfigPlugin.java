package icyllis.modernui.mc.text;

import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiPlatform;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    private boolean mEnable;

    public void onLoad(String mixinPackage) {
        if (MuiPlatform.get().isClient()) {
            this.mEnable = ModernUIMod.isTextEngineEnabled();
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.mEnable;
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