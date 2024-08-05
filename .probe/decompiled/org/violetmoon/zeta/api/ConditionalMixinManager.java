package org.violetmoon.zeta.api;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.annotation.ConditionalMixin;

@Internal
public class ConditionalMixinManager {

    private static final Logger LOGGER = LogManager.getLogger("ZetaConditionalMixinManager");

    public static boolean shouldApply(Zeta zeta, String targetClassName, String mixinClassName) {
        try {
            List<AnnotationNode> annotationNodes = MixinService.getService().getBytecodeProvider().getClassNode(targetClassName).visibleAnnotations;
            if (annotationNodes == null) {
                return true;
            } else {
                boolean shouldApply = true;
                for (AnnotationNode node : annotationNodes) {
                    if (node.desc.equals(Type.getDescriptor(ConditionalMixin.class))) {
                        List<String> mods = (List<String>) Annotations.getValue(node, "value");
                        boolean applyIfPresent = (Boolean) Annotations.getValue(node, "applyIfPresent", Boolean.TRUE);
                        boolean anyModsLoaded = areModsLoaded(zeta, mods);
                        shouldApply = anyModsLoaded == applyIfPresent;
                        LOGGER.info("{}: {} is{}being applied because the mod(s) {} are{}loaded", zeta.getModDisplayName(zeta.modid), targetClassName, shouldApply ? " " : " not ", mods, anyModsLoaded ? " " : " not ");
                    }
                }
                return shouldApply;
            }
        } catch (ClassNotFoundException | IOException var10) {
            throw new RuntimeException(var10);
        }
    }

    private static boolean areModsLoaded(Zeta zeta, List<String> modids) {
        for (String mod : modids) {
            if (zeta.isModLoaded(mod)) {
                return true;
            }
        }
        return false;
    }
}