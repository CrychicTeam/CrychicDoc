package ca.fxco.memoryleakfix.mixinextras.injector;

import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

@AnnotationType(WrapWithCondition.class)
@HandlerPrefix("wrapWithCondition")
public class WrapWithConditionInjectionInfo extends MixinExtrasInjectionInfo {

    public WrapWithConditionInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new WrapWithConditionInjector(this);
    }

    public void prepare() {
        super.prepare();
        for (List<InjectionNode> nodeList : this.targetNodes.values()) {
            for (InjectionNode node : nodeList) {
                AbstractInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget instanceof MethodInsnNode) {
                    Type returnType = Type.getReturnType(((MethodInsnNode) currentTarget).desc);
                    if (this.isTypePoppedByInstruction(returnType, currentTarget.getNext())) {
                        node.decorate("mixinextras_operationIsImmediatelyPopped", true);
                    }
                }
            }
        }
    }

    private boolean isTypePoppedByInstruction(Type type, AbstractInsnNode insn) {
        switch(type.getSize()) {
            case 1:
                return insn.getOpcode() == 87;
            case 2:
                return insn.getOpcode() == 88;
            default:
                return false;
        }
    }
}