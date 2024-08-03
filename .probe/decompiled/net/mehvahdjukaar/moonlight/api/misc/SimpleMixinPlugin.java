package net.mehvahdjukaar.moonlight.api.misc;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

public abstract class SimpleMixinPlugin implements IMixinConfigPlugin {

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        ClassNode node;
        try {
            node = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
        } catch (Exception var9) {
            return false;
        }
        if (node != null && node.invisibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleAnnotations) {
                if (annotationNode.desc.equals("L" + OptionalMixin.class.getName().replace('.', '/') + ";")) {
                    List<Object> values = annotationNode.values;
                    boolean needsClass = values.size() < 4 || (Boolean) values.get(3);
                    try {
                        String name = values.get(1).toString();
                        MixinService.getService().getBytecodeProvider().getClassNode(name);
                        if (!needsClass) {
                            return false;
                        }
                    } catch (Exception var10) {
                        if (needsClass) {
                            return false;
                        }
                    }
                }
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
}