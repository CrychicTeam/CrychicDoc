package ca.fxco.memoryleakfix.mixinextras.injector;

import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.CompatibilityHelper;
import ca.fxco.memoryleakfix.mixinextras.utils.InjectorUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinExtrasLogger;
import java.util.function.Supplier;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.code.Injector.InjectorData;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Bytecode;

public class ModifyExpressionValueInjector extends Injector {

    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("ModifyExpressionValue");

    public ModifyExpressionValueInjector(InjectionInfo info) {
        super(info, "@ModifyExpressionValue");
    }

    protected void inject(Target target, InjectionNode node) {
        this.checkTargetReturnsAValue(target, node);
        this.checkTargetModifiers(target, false);
        StackExtension stack = new StackExtension(target);
        AbstractInsnNode valueNode = node.getCurrentTarget();
        Type valueType = this.getReturnType(valueNode);
        boolean shouldPop = false;
        if (valueNode instanceof TypeInsnNode && valueNode.getOpcode() == 187) {
            if (!InjectorUtils.isDupedNew(node)) {
                target.insns.insert(valueNode, new InsnNode(89));
                stack.extra(1);
                node.decorate("mixinextras_newIsDuped", true);
                shouldPop = true;
            }
            valueNode = ASMUtils.findInitNodeFor(target, (TypeInsnNode) valueNode);
        }
        this.injectValueModifier(target, valueNode, valueType, InjectorUtils.isDupedFactoryRedirect(node), shouldPop, stack);
    }

    private void checkTargetReturnsAValue(Target target, InjectionNode node) {
        Type returnType = this.getReturnType(node.getCurrentTarget());
        if (returnType == Type.VOID_TYPE) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an instruction with a return type of 'void' in %s in %s", this.annotationType, target, this));
        } else if (returnType == null) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an invalid insn in %s in %s", this.annotationType, target, this));
        }
    }

    private void injectValueModifier(Target target, AbstractInsnNode valueNode, Type valueType, boolean isDupedFactoryRedirect, boolean shouldPop, StackExtension stack) {
        InsnList after = new InsnList();
        this.invokeHandler(valueType, target, after, stack);
        if (shouldPop) {
            after.add(new InsnNode(87));
        }
        target.insns.insert(this.getInsertionPoint(valueNode, target, isDupedFactoryRedirect), after);
    }

    private void invokeHandler(Type valueType, Target target, InsnList after, StackExtension stack) {
        InjectorData handler = new InjectorData(target, "expression value modifier");
        this.validateParams(handler, valueType, new Type[] { valueType });
        if (!this.isStatic) {
            after.add(new VarInsnNode(25, 0));
            if (valueType.getSize() == 2) {
                stack.extra(1);
                after.add(new InsnNode(91));
                after.add(new InsnNode(87));
            } else {
                after.add(new InsnNode(95));
            }
        }
        if (handler.captureTargetArgs > 0) {
            this.pushArgs(target.arguments, after, target.getArgIndices(), 0, handler.captureTargetArgs);
        }
        stack.receiver(this.isStatic);
        stack.capturedArgs(target.arguments, handler.captureTargetArgs);
        this.invokeHandler(after);
    }

    private AbstractInsnNode getInsertionPoint(AbstractInsnNode valueNode, Target target, boolean isDupedFactoryRedirect) {
        if (!isDupedFactoryRedirect) {
            return valueNode;
        } else {
            AbstractInsnNode node = InjectorUtils.findFactoryRedirectThrowString(target, valueNode);
            if (node == null) {
                return valueNode;
            } else {
                String message = (String) ((LdcInsnNode) node).cst;
                Supplier<AbstractInsnNode> failed = () -> {
                    LOGGER.warn("Please inform LlamaLad7! Failed to find end of factory redirect throw for '{}'", message);
                    return valueNode;
                };
                if ((node = node.getNext()).getOpcode() != 183) {
                    return (AbstractInsnNode) failed.get();
                } else if ((node = node.getNext()).getOpcode() != 191) {
                    return (AbstractInsnNode) failed.get();
                } else {
                    AbstractInsnNode var9;
                    return !((var9 = node.getNext()) instanceof LabelNode) ? (AbstractInsnNode) failed.get() : var9;
                }
            }
        }
    }

    private Type getReturnType(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            return Type.getReturnType(methodInsnNode.desc);
        } else if (node instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
            return fieldInsnNode.getOpcode() != 180 && fieldInsnNode.getOpcode() != 178 ? Type.VOID_TYPE : Type.getType(fieldInsnNode.desc);
        } else if (Bytecode.isConstant(node)) {
            return Bytecode.getConstantType(node);
        } else if (node instanceof TypeInsnNode && node.getOpcode() == 187) {
            TypeInsnNode typeInsnNode = (TypeInsnNode) node;
            return Type.getObjectType(typeInsnNode.desc);
        } else {
            return null;
        }
    }
}