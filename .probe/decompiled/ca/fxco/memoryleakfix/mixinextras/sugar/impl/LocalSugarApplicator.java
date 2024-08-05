package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.injector.StackExtension;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.ref.LocalRefClassGenerator;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.ref.LocalRefUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.CompatibilityHelper;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.modify.InvalidImplicitDiscriminatorException;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator.Context;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.SignaturePrinter;

class LocalSugarApplicator extends SugarApplicator {

    private final boolean isArgsOnly;

    private final Type targetLocalType = LocalRefUtils.getTargetType(this.paramType, this.paramGeneric);

    private final boolean isMutable = this.targetLocalType != this.paramType;

    LocalSugarApplicator(InjectionInfo info, SugarParameter parameter) {
        super(info, parameter);
        this.isArgsOnly = (Boolean) Annotations.getValue(this.sugar, "argsOnly", false);
    }

    @Override
    void validate(Target target, InjectionNode node) {
        LocalVariableDiscriminator discriminator = LocalVariableDiscriminator.parse(this.sugar);
        Context context = this.getOrCreateLocalContext(target, node);
        if (discriminator.printLVT()) {
            this.printLocals(target, node.getCurrentTarget(), context, discriminator);
            this.info.addCallbackInvocation(this.info.getMethod());
            throw new SugarApplicationException("Application aborted because locals are being printed instead.");
        } else {
            try {
                if (discriminator.findLocal(context) < 0) {
                    throw new SugarApplicationException("Unable to find matching local!");
                }
            } catch (InvalidImplicitDiscriminatorException var6) {
                throw new SugarApplicationException("Invalid implicit variable discriminator: ", var6);
            }
        }
    }

    @Override
    void prepare(Target target, InjectionNode node) {
        this.getOrCreateLocalContext(target, node);
    }

    @Override
    void inject(Target target, InjectionNode node, StackExtension stack) {
        LocalVariableDiscriminator discriminator = LocalVariableDiscriminator.parse(this.sugar);
        Context context = (Context) node.getDecoration(this.getLocalContextKey());
        int index = discriminator.findLocal(context);
        if (index < 0) {
            throw new SugarApplicationException("Failed to match a local, this should have been caught during validation.");
        } else {
            if (this.isMutable) {
                this.initAndLoadLocalRef(target, node, index, stack);
            } else {
                stack.extra(this.targetLocalType.getSize());
                target.insns.insertBefore(node.getCurrentTarget(), new VarInsnNode(this.targetLocalType.getOpcode(21), index));
            }
        }
    }

    private void initAndLoadLocalRef(Target target, InjectionNode node, int index, StackExtension stack) {
        String refName = LocalRefClassGenerator.getForType(this.targetLocalType);
        int refIndex = this.getOrCreateRef(target, node, index, refName, stack);
        stack.extra(1);
        target.insns.insertBefore(node.getCurrentTarget(), new VarInsnNode(25, refIndex));
    }

    private int getOrCreateRef(Target target, InjectionNode node, int index, String refImpl, StackExtension stack) {
        Map<Integer, Integer> refIndices = (Map<Integer, Integer>) node.getDecoration("mixinextras_localRefMap");
        if (refIndices == null) {
            refIndices = new HashMap();
            node.decorate("mixinextras_localRefMap", refIndices);
        }
        if (refIndices.containsKey(index)) {
            return (Integer) refIndices.get(index);
        } else {
            int refIndex = target.allocateLocal();
            target.addLocalVariable(refIndex, "ref" + refIndex, 'L' + refImpl + ';');
            InsnList construction = new InsnList();
            LocalRefUtils.generateNew(construction, this.targetLocalType);
            construction.add(new VarInsnNode(58, refIndex));
            target.insertBefore(node, construction);
            SugarPostProcessingExtension.enqueuePostProcessing(this, () -> {
                InsnList initialization = new InsnList();
                initialization.add(new VarInsnNode(25, refIndex));
                initialization.add(new VarInsnNode(this.targetLocalType.getOpcode(21), index));
                LocalRefUtils.generateInitialization(initialization, this.targetLocalType);
                target.insertBefore(node, initialization);
                stack.extra(this.targetLocalType.getSize() + 1);
                InsnList after = new InsnList();
                after.add(new VarInsnNode(25, refIndex));
                LocalRefUtils.generateDisposal(after, this.targetLocalType);
                after.add(new VarInsnNode(this.targetLocalType.getOpcode(54), index));
                target.insns.insert(node.getCurrentTarget(), after);
            });
            refIndices.put(index, refIndex);
            return refIndex;
        }
    }

    private Context getOrCreateLocalContext(Target target, InjectionNode node) {
        String decorationKey = this.getLocalContextKey();
        if (node.hasDecoration(decorationKey)) {
            return (Context) node.getDecoration(decorationKey);
        } else {
            Context context = CompatibilityHelper.makeLvtContext(this.info, this.targetLocalType, this.isArgsOnly, target, node.getCurrentTarget());
            node.decorate(decorationKey, context);
            return context;
        }
    }

    private String getLocalContextKey() {
        return String.format("mixinextras_persistent_localSugarContext(%s,%s)", this.targetLocalType, this.isArgsOnly ? "argsOnly" : "fullFrame");
    }

    private void printLocals(Target target, AbstractInsnNode node, Context context, LocalVariableDiscriminator discriminator) {
        int baseArgIndex = target.isStatic ? 0 : 1;
        new PrettyPrinter().kvWidth(20).kv("Target Class", target.classNode.name.replace('/', '.')).kv("Target Method", target.method.name).kv("Capture Type", SignaturePrinter.getTypeName(this.targetLocalType, false)).kv("Instruction", "[%d] %s %s", new Object[] { target.insns.indexOf(node), node.getClass().getSimpleName(), Bytecode.getOpcodeName(node.getOpcode()) }).hr().kv("Match mode", this.isImplicit(discriminator, baseArgIndex) ? "IMPLICIT (match single)" : "EXPLICIT (match by criteria)").kv("Match ordinal", discriminator.getOrdinal() < 0 ? "any" : discriminator.getOrdinal()).kv("Match index", discriminator.getIndex() < baseArgIndex ? "any" : discriminator.getIndex()).kv("Match name(s)", discriminator.hasNames() ? discriminator.getNames() : "any").kv("Args only", this.isArgsOnly).hr().add(context).print(System.err);
    }

    private boolean isImplicit(LocalVariableDiscriminator discriminator, int baseArgIndex) {
        return discriminator.getOrdinal() < 0 && discriminator.getIndex() < baseArgIndex && discriminator.getNames().isEmpty();
    }
}