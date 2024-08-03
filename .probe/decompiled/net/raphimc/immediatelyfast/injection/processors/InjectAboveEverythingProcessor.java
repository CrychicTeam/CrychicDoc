package net.raphimc.immediatelyfast.injection.processors;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class InjectAboveEverythingProcessor {

    public static void process(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.visibleAnnotations != null) {
                for (AnnotationNode annotationNode : methodNode.visibleAnnotations) {
                    if (annotationNode.desc.equals(Type.getDescriptor(InjectAboveEverything.class))) {
                        if (!methodNode.desc.equals("(" + Type.getDescriptor(CallbackInfo.class) + ")V")) {
                            throw new RuntimeException("Method annotated with @InjectAboveEverything must have signature void(CallbackInfo)");
                        }
                        if ((methodNode.access & 8) != 0) {
                            throw new RuntimeException("Method annotated with @InjectAboveEverything must not be static");
                        }
                        for (MethodNode methodNode2 : classNode.methods) {
                            boolean shouldInject = false;
                            for (AbstractInsnNode insnNode : methodNode2.instructions.toArray()) {
                                if (insnNode instanceof MethodInsnNode) {
                                    MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                                    if (methodInsnNode.owner.equals(classNode.name) && methodInsnNode.name.equals(methodNode.name) && methodInsnNode.desc.equals(methodNode.desc)) {
                                        methodNode2.instructions.set(insnNode, new InsnNode(88));
                                        shouldInject = true;
                                    }
                                }
                            }
                            if (shouldInject) {
                                InsnList inject = new InsnList();
                                inject.add(new VarInsnNode(25, 0));
                                inject.add(new InsnNode(1));
                                inject.add(new MethodInsnNode(182, classNode.name, methodNode.name, methodNode.desc, (classNode.access & 512) != 0));
                                methodNode2.instructions.insertBefore(methodNode2.instructions.getFirst(), inject);
                            }
                        }
                        methodNode.visibleAnnotations.remove(annotationNode);
                        break;
                    }
                }
            }
        }
    }
}