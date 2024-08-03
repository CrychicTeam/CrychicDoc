package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.injector.StackExtension;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.ref.LocalRefUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.TargetDecorations;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Annotations;

class ShareSugarApplicator extends SugarApplicator {

    private final String id = this.mixin.getClassRef() + ':' + Annotations.getValue(this.sugar);

    private Type innerType;

    ShareSugarApplicator(InjectionInfo info, SugarParameter parameter) {
        super(info, parameter);
    }

    @Override
    void validate(Target target, InjectionNode node) {
        this.innerType = LocalRefUtils.getTargetType(this.paramType, Type.getType(Object.class));
        if (this.innerType == this.paramType) {
            throw new SugarApplicationException("@Share parameter must be some variation of LocalRef.");
        }
    }

    @Override
    void prepare(Target target, InjectionNode node) {
    }

    @Override
    void inject(Target target, InjectionNode node, StackExtension stack) {
        Map<String, Integer> refIndices = TargetDecorations.getOrPut(target, "ShareSugar_LocalRefIndices", HashMap::new);
        int localRefIndex;
        if (!refIndices.containsKey(this.id)) {
            localRefIndex = target.allocateLocal();
            refIndices.put(this.id, localRefIndex);
            LabelNode start = new LabelNode();
            LabelNode end = new LabelNode();
            target.addLocalVariable(localRefIndex, "sharedRef" + localRefIndex, this.paramType.getDescriptor());
            List<LocalVariableNode> lvt = target.method.localVariables;
            LocalVariableNode newVar = (LocalVariableNode) lvt.get(lvt.size() - 1);
            newVar.start = start;
            newVar.end = end;
            target.insns.insert(start);
            target.insns.add(end);
            InsnList init = new InsnList();
            LocalRefUtils.generateNew(init, this.innerType);
            init.add(new VarInsnNode(58, localRefIndex));
            init.add(new VarInsnNode(25, localRefIndex));
            init.add(new InsnNode(ASMUtils.getDummyOpcodeForType(this.innerType)));
            LocalRefUtils.generateInitialization(init, this.innerType);
            target.insns.insert(start, init);
            stack.ensureAtLeast(this.innerType.getSize() + 1);
        } else {
            localRefIndex = (Integer) refIndices.get(this.id);
        }
        stack.extra(1);
        target.insns.insertBefore(node.getCurrentTarget(), new VarInsnNode(25, localRefIndex));
    }
}