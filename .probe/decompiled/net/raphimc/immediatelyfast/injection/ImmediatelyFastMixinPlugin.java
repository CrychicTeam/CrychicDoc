package net.raphimc.immediatelyfast.injection;

import java.util.List;
import java.util.Set;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.PlatformCode;
import net.raphimc.immediatelyfast.injection.processors.InjectAboveEverythingProcessor;
import net.raphimc.immediatelyfast.injection.processors.InjectOnAllReturnsProcessor;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ImmediatelyFastMixinPlugin implements IMixinConfigPlugin {

    private String mixinPackage;

    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage + ".";
        ImmediatelyFast.earlyInit();
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(this.mixinPackage)) {
            return false;
        } else {
            String mixinName = mixinClassName.substring(this.mixinPackage.length());
            String packageName = mixinName.substring(0, mixinName.lastIndexOf(46));
            if (!ImmediatelyFast.config.font_atlas_resizing && packageName.startsWith("font_atlas_resizing")) {
                return false;
            } else if (!ImmediatelyFast.config.map_atlas_generation && packageName.startsWith("map_atlas_generation")) {
                return false;
            } else if (!ImmediatelyFast.config.hud_batching && packageName.startsWith("hud_batching")) {
                return false;
            } else if (!ImmediatelyFast.config.fast_text_lookup && packageName.startsWith("fast_text_lookup")) {
                return false;
            } else if (!ImmediatelyFast.config.fast_buffer_upload && packageName.startsWith("fast_buffer_upload")) {
                return false;
            } else if (!ImmediatelyFast.config.experimental_disable_error_checking && packageName.startsWith("disable_error_checking")) {
                return false;
            } else if (!ImmediatelyFast.config.experimental_sign_text_buffering && packageName.startsWith("sign_text_buffering")) {
                return false;
            } else if (!ImmediatelyFast.config.experimental_screen_batching && packageName.startsWith("screen_batching")) {
                return false;
            } else if (packageName.startsWith("hud_batching.compat.armorchroma") && PlatformCode.getModVersion("armorchroma").isEmpty()) {
                return false;
            } else if (packageName.startsWith("hud_batching.compat.appleskin") && PlatformCode.getModVersion("appleskin").isEmpty()) {
                return false;
            } else if (packageName.startsWith("hud_batching.compat.iceberg") && PlatformCode.getModVersion("iceberg").isEmpty()) {
                return false;
            } else {
                return packageName.startsWith("hud_batching.compat.highlighter") && PlatformCode.getModVersion("highlighter").isEmpty() ? false : !packageName.startsWith("hud_batching.compat.itemborders") || !PlatformCode.getModVersion("itemborders").isEmpty();
            }
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
        InjectAboveEverythingProcessor.process(targetClass);
        InjectOnAllReturnsProcessor.process(targetClass);
    }
}