package icyllis.modernui.mc;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    private boolean mDisableSmoothScrolling;

    private boolean mDisableEnhancedTextField;

    public void onLoad(String mixinPackage) {
        if (MuiPlatform.get().isClient()) {
            this.mDisableSmoothScrolling = Boolean.parseBoolean(ModernUIMod.getBootstrapProperty("modernui_mc_disableSmoothScrolling"));
            this.mDisableEnhancedTextField = Boolean.parseBoolean(ModernUIMod.getBootstrapProperty("modernui_mc_disableEnhancedTextField"));
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.mDisableSmoothScrolling) {
            return !mixinClassName.equals("icyllis.modernui.mc.mixin.MixinScrollPanel") && !mixinClassName.equals("icyllis.modernui.mc.mixin.MixinSelectionList");
        } else {
            return !this.mDisableEnhancedTextField ? !mixinClassName.endsWith("DBG") : !mixinClassName.equals("icyllis.modernui.mc.mixin.MixinEditBox") && !mixinClassName.equals("icyllis.modernui.mc.mixin.MixinStringSplitter") && !mixinClassName.equals("icyllis.modernui.mc.mixin.MixinTextFieldHelper");
        }
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