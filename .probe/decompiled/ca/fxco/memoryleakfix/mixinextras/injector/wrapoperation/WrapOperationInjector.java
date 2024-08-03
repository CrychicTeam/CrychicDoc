package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

import ca.fxco.memoryleakfix.mixinextras.injector.StackExtension;
import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.ArrayUtils;
import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.CompatibilityHelper;
import ca.fxco.memoryleakfix.mixinextras.utils.InjectorUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.UniquenessHelper;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.code.Injector.InjectorData;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.asm.ASM;

class WrapOperationInjector extends Injector {

    private static final Handle LMF_HANDLE = new Handle(6, "java/lang/invoke/LambdaMetafactory", "metafactory", Bytecode.generateDescriptor(CallSite.class, new Object[] { Lookup.class, String.class, MethodType.class, MethodType.class, MethodHandle.class, MethodType.class }), false);

    private static final String NPE = Type.getInternalName(NullPointerException.class);

    private final Type operationType = MixinExtrasService.getInstance().changePackage(Operation.class, Type.getType(CompatibilityHelper.getAnnotation(this.info).desc), WrapOperation.class);

    public WrapOperationInjector(InjectionInfo info) {
        super(info, "@WrapOperation");
    }

    protected void inject(Target target, InjectionNode node) {
        this.checkTargetModifiers(target, false);
        this.checkNode(target, node);
        this.wrapOperation(target, node);
    }

    private void checkNode(Target target, InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) currentTarget;
            if (methodInsnNode.name.equals("<init>")) {
                throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is trying to target an <init> call in %s in %s! If this is an instantiation, target the NEW instead.", this.annotationType, target, this));
            }
        } else if (!(currentTarget instanceof FieldInsnNode) && originalTarget.getOpcode() != 193 && originalTarget.getOpcode() != 187) {
            throw CompatibilityHelper.makeInvalidInjectionException(this.info, String.format("%s annotation is targeting an invalid insn in %s in %s", this.annotationType, target, this));
        }
    }

    private void wrapOperation(Target target, InjectionNode node) {
        StackExtension stack = new StackExtension(target);
        AbstractInsnNode initialTarget = node.getCurrentTarget();
        InsnList insns = new InsnList();
        boolean isNew = initialTarget.getOpcode() == 187;
        boolean isDupedNew = InjectorUtils.isDupedNew(node);
        if (isNew) {
            node.decorate("mixinextras_wrappedOperation", true);
            node = target.addInjectionNode(ASMUtils.findInitNodeFor(target, (TypeInsnNode) initialTarget));
        }
        Type[] argTypes = this.getCurrentArgTypes(node);
        Type returnType = this.getReturnType(node);
        AbstractInsnNode champion = this.invokeHandler(target, node, argTypes, returnType, insns, stack);
        if (isDupedNew) {
            target.insns.set(initialTarget, new InsnNode(1));
            stack.extra(1);
            insns.add(new InsnNode(91));
            insns.add(new InsnNode(87));
            insns.add(new InsnNode(87));
            insns.add(new InsnNode(87));
        } else if (isNew) {
            target.insns.set(initialTarget, new InsnNode(0));
            insns.add(new InsnNode(87));
        }
        AbstractInsnNode finalTarget = node.getCurrentTarget();
        target.wrapNode(finalTarget, champion, insns, new InsnList());
        if (isNew) {
            target.getInjectionNode(initialTarget).replace(champion);
        }
        node.decorate("mixinextras_wrappedOperation", true);
        target.insns.remove(finalTarget);
    }

    private AbstractInsnNode invokeHandler(Target target, InjectionNode node, Type[] argTypes, Type returnType, InsnList insns, StackExtension stack) {
        InjectorData handler = new InjectorData(target, "operation wrapper");
        boolean hasExtraThis = node.isReplaced() && node.getCurrentTarget().getOpcode() != 184;
        if (hasExtraThis) {
            argTypes = ArrayUtils.remove(argTypes, 0);
        }
        Type[] originalArgs = this.getOriginalArgTypes(node);
        this.validateParams(handler, returnType, ArrayUtils.add(originalArgs, this.operationType));
        int[] argMap = this.storeArgs(target, argTypes, insns, 0);
        if (hasExtraThis) {
            insns.add(new InsnNode(87));
        }
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
        }
        this.pushArgs(this.methodArgs, insns, argMap, 0, originalArgs.length);
        if (hasExtraThis) {
            insns.add(new VarInsnNode(25, 0));
        }
        this.pushArgs(argTypes, insns, argMap, originalArgs.length, argMap.length);
        this.makeSupplier(target, originalArgs, returnType, node, insns, hasExtraThis, ArrayUtils.subarray(argTypes, originalArgs.length, argTypes.length));
        if (handler.captureTargetArgs > 0) {
            this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, handler.captureTargetArgs);
        }
        stack.receiver(this.isStatic);
        stack.extra(1);
        stack.capturedArgs(target.arguments, handler.captureTargetArgs);
        AbstractInsnNode champion = super.invokeHandler(insns);
        if (InjectorUtils.isDynamicInstanceofRedirect(node)) {
            insns.add(new InsnNode(95));
            insns.add(new InsnNode(87));
        }
        return champion;
    }

    private void makeSupplier(Target target, Type[] argTypes, Type returnType, InjectionNode node, InsnList insns, boolean hasExtraThis, Type[] trailingParams) {
        Type[] descriptorArgs = trailingParams;
        if (hasExtraThis) {
            descriptorArgs = ArrayUtils.add(trailingParams, 0, Type.getObjectType(this.classNode.name));
        }
        insns.add(new InvokeDynamicInsnNode("call", Bytecode.generateDescriptor(this.operationType, descriptorArgs), LMF_HANDLE, new Object[] { Type.getMethodType(Type.getType(Object.class), new Type[] { Type.getType(Object[].class) }), this.generateSyntheticBridge(target, node, argTypes, returnType, hasExtraThis, trailingParams), Type.getMethodType(ASMUtils.isPrimitive(returnType) ? Type.getObjectType(returnType == Type.VOID_TYPE ? "java/lang/Void" : Bytecode.getBoxingType(returnType)) : returnType, new Type[] { Type.getType(Object[].class) }) }));
    }

    private Handle generateSyntheticBridge(Target target, InjectionNode node, Type[] argTypes, Type returnType, boolean virtual, Type[] boundParams) {
        MethodNode method = new MethodNode(ASM.API_VERSION, 4098 | (virtual ? 0 : 8), UniquenessHelper.getUniqueMethodName(this.classNode, "mixinextras$bridge$" + this.getName(node.getCurrentTarget())), Bytecode.generateDescriptor(ASMUtils.isPrimitive(returnType) ? Type.getObjectType(returnType == Type.VOID_TYPE ? "java/lang/Void" : Bytecode.getBoxingType(returnType)) : returnType, ArrayUtils.add(boundParams, Type.getType(Object[].class))), null, null);
        method.instructions = new InsnList() {

            {
                int paramArrayIndex = Arrays.stream(boundParams).mapToInt(Type::getSize).sum() + (virtual ? 1 : 0);
                this.add(new VarInsnNode(25, paramArrayIndex));
                this.add(new IntInsnNode(16, argTypes.length));
                this.add(new LdcInsnNode(Arrays.stream(argTypes).map(Type::getClassName).collect(Collectors.joining(", ", "[", "]"))));
                this.add(new MethodInsnNode(184, Type.getInternalName(WrapOperationRuntime.class), "checkArgumentCount", Bytecode.generateDescriptor(void.class, new Object[] { Object[].class, int.class, String.class }), false));
                if (virtual) {
                    this.add(new VarInsnNode(25, 0));
                }
                Consumer<InsnList> loadArgs = insns -> {
                    insns.add(new VarInsnNode(25, paramArrayIndex));
                    for (int i = 0; i < argTypes.length; i++) {
                        Type argType = argTypes[i];
                        insns.add(new InsnNode(89));
                        insns.add(new IntInsnNode(16, i));
                        insns.add(new InsnNode(50));
                        if (ASMUtils.isPrimitive(argType)) {
                            insns.add(new TypeInsnNode(192, Bytecode.getBoxingType(argType)));
                            insns.add(new MethodInsnNode(182, Bytecode.getBoxingType(argType), Bytecode.getUnboxingMethod(argType), Type.getMethodDescriptor(argType, new Type[0]), false));
                        } else {
                            insns.add(new TypeInsnNode(192, argType.getInternalName()));
                        }
                        if (argType.getSize() == 2) {
                            insns.add(new InsnNode(93));
                            insns.add(new InsnNode(88));
                        } else {
                            insns.add(new InsnNode(95));
                        }
                    }
                    insns.add(new InsnNode(87));
                    int boundParamIndex = virtual ? 1 : 0;
                    for (Type boundParamType : boundParams) {
                        insns.add(new VarInsnNode(boundParamType.getOpcode(21), boundParamIndex));
                        boundParamIndex += boundParamType.getSize();
                    }
                };
                this.add(WrapOperationInjector.this.copyNode(node, paramArrayIndex, target, loadArgs));
                if (returnType == Type.VOID_TYPE) {
                    this.add(new InsnNode(1));
                    this.add(new TypeInsnNode(192, "java/lang/Void"));
                } else if (ASMUtils.isPrimitive(returnType)) {
                    this.add(new MethodInsnNode(184, Bytecode.getBoxingType(returnType), "valueOf", Bytecode.generateDescriptor(Type.getObjectType(Bytecode.getBoxingType(returnType)), new Type[] { returnType }), false));
                }
                this.add(new InsnNode(176));
            }
        };
        this.classNode.methods.add(method);
        return new Handle(virtual ? 7 : 6, this.classNode.name, method.name, method.desc, (this.classNode.access & 512) != 0);
    }

    private InsnList copyNode(InjectionNode node, int paramArrayIndex, Target target, Consumer<InsnList> loadArgs) {
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        InsnList insns = new InsnList();
        if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) currentTarget;
            if (methodInsnNode.name.equals("<init>")) {
                insns.add(new TypeInsnNode(187, methodInsnNode.owner));
                insns.add(new InsnNode(89));
            }
        }
        loadArgs.accept(insns);
        insns.add(currentTarget.clone(Collections.emptyMap()));
        if (InjectorUtils.isDynamicInstanceofRedirect(node)) {
            insns.add(new VarInsnNode(25, paramArrayIndex));
            insns.add(new InsnNode(3));
            insns.add(new InsnNode(50));
            insns.add(new InsnNode(95));
            this.checkAndMoveNodes(target.insns, insns, currentTarget, it -> it.getOpcode() == 89, it -> it.getOpcode() == 199, it -> it.getOpcode() == 187 && ((TypeInsnNode) it).desc.equals(NPE), it -> it.getOpcode() == 89, it -> it instanceof LdcInsnNode && ((LdcInsnNode) it).cst instanceof String, it -> it.getOpcode() == 183 && ((MethodInsnNode) it).owner.equals(NPE), it -> it.getOpcode() == 191, it -> it instanceof LabelNode, it -> it.getOpcode() == 95, it -> it.getOpcode() == 89, it -> it.getOpcode() == 198, it -> it.getOpcode() == 182 && ((MethodInsnNode) it).name.equals("getClass"), it -> it.getOpcode() == 182 && ((MethodInsnNode) it).name.equals("isAssignableFrom"), it -> it.getOpcode() == 167, it -> it instanceof LabelNode, it -> it.getOpcode() == 87, it -> it.getOpcode() == 87, it -> it.getOpcode() == 3, it -> it instanceof LabelNode);
        }
        if (InjectorUtils.isDupedFactoryRedirect(node)) {
            AbstractInsnNode ldc = InjectorUtils.findFactoryRedirectThrowString(target, currentTarget);
            if (ldc != null) {
                this.checkAndMoveNodes(target.insns, insns, currentTarget, it -> it.getOpcode() == 89, it -> it.getOpcode() == 199, it -> it.getOpcode() == 187 && ((TypeInsnNode) it).desc.equals(NPE), it -> it.getOpcode() == 89, it -> it == ldc, it -> it.getOpcode() == 183 && ((MethodInsnNode) it).name.equals("<init>"), it -> it.getOpcode() == 191, it -> it instanceof LabelNode);
            }
        }
        return insns;
    }

    @SafeVarargs
    private final void checkAndMoveNodes(InsnList from, InsnList to, AbstractInsnNode node, Predicate<AbstractInsnNode>... predicates) {
        AbstractInsnNode current = node.getNext();
        for (Predicate<AbstractInsnNode> predicate : predicates) {
            if (!predicate.test(current)) {
                throw new AssertionError("Failed assertion when wrapping instructions. Please inform LlamaLad7!");
            }
            AbstractInsnNode old = current;
            do {
                current = current.getNext();
            } while (current instanceof FrameNode);
            from.remove(old);
            to.add(old);
        }
    }

    private Type getReturnType(InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        if (originalTarget.getOpcode() == 193) {
            return Type.BOOLEAN_TYPE;
        } else if (currentTarget instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) currentTarget;
            return methodInsnNode.name.equals("<init>") ? Type.getObjectType(methodInsnNode.owner) : Type.getReturnType(methodInsnNode.desc);
        } else if (currentTarget instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) currentTarget;
            return fieldInsnNode.getOpcode() != 180 && fieldInsnNode.getOpcode() != 178 ? Type.VOID_TYPE : Type.getType(fieldInsnNode.desc);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Type[] getOriginalArgTypes(InjectionNode node) {
        return node.hasDecoration("mixinextras_newArgTypes") ? (Type[]) node.getDecoration("mixinextras_newArgTypes") : this.getEffectiveArgTypes(node.getOriginalTarget());
    }

    private Type[] getCurrentArgTypes(InjectionNode node) {
        return this.getEffectiveArgTypes(node.getCurrentTarget());
    }

    private Type[] getEffectiveArgTypes(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            Type[] args = Type.getArgumentTypes(methodInsnNode.desc);
            if (methodInsnNode.name.equals("<init>")) {
                return args;
            } else {
                switch(methodInsnNode.getOpcode()) {
                    case 183:
                        args = ArrayUtils.add(args, 0, Type.getObjectType(this.classNode.name));
                    case 184:
                        break;
                    default:
                        args = ArrayUtils.add(args, 0, Type.getObjectType(methodInsnNode.owner));
                }
                return args;
            }
        } else {
            if (node instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
                switch(fieldInsnNode.getOpcode()) {
                    case 178:
                        return new Type[0];
                    case 179:
                        return new Type[] { Type.getType(fieldInsnNode.desc) };
                    case 180:
                        return new Type[] { Type.getObjectType(fieldInsnNode.owner) };
                    case 181:
                        return new Type[] { Type.getObjectType(fieldInsnNode.owner), Type.getType(fieldInsnNode.desc) };
                }
            }
            if (node.getOpcode() == 193) {
                return new Type[] { Type.getType(Object.class) };
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private String getName(AbstractInsnNode node) {
        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            if (methodInsnNode.name.equals("<init>")) {
                String desc = methodInsnNode.owner;
                return "new" + desc.substring(desc.lastIndexOf(47) + 1);
            } else {
                return ((MethodInsnNode) node).name;
            }
        } else if (node instanceof FieldInsnNode) {
            return ((FieldInsnNode) node).name;
        } else if (node.getOpcode() == 193) {
            String desc = ((TypeInsnNode) node).desc;
            return "instanceof" + desc.substring(desc.lastIndexOf(47) + 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}