package org.embeddedt.embeddium.asm;

import java.io.IOException;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

public class AnnotationProcessingEngine {

    private static final String OPTIONAL_INTERFACE_DESC = Type.getDescriptor(OptionalInterface.class);

    public static void processClass(ClassNode clz) {
        for (AnnotationNode annotationNode : clz.invisibleAnnotations) {
            if (annotationNode.desc.equals(OPTIONAL_INTERFACE_DESC)) {
                for (Type ifaceType : (List) Annotations.getValue(annotationNode)) {
                    String ifaceName = ifaceType.getInternalName();
                    try {
                        MixinService.getService().getBytecodeProvider().getClassNode(ifaceName);
                    } catch (ClassNotFoundException | IOException var8) {
                        clz.interfaces.removeIf(i -> i.equals(ifaceName));
                    }
                }
            }
        }
    }
}