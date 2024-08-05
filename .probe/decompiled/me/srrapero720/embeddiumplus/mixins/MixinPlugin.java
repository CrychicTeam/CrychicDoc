package me.srrapero720.embeddiumplus.mixins;

import java.util.List;
import java.util.Set;
import me.srrapero720.embeddiumplus.EmbyTools;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin implements IMixinConfigPlugin {

    public void onLoad(String s) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (targetClassName.contains("zume.Zume") && !EmbyTools.isModInstalled("zume")) {
            return false;
        } else if (targetClassName.contains("jei_rei_emi.Jei") && !EmbyTools.isModInstalled("jei")) {
            return false;
        } else {
            return targetClassName.contains("jei_rei_emi.Rei") && !EmbyTools.isModInstalled("roughlyenoughitems") ? false : !targetClassName.contains("jei_rei_emi.Emi") || EmbyTools.isModInstalled("emi");
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