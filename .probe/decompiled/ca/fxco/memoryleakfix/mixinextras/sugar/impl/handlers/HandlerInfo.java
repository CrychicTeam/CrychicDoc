package ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers;

import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarParameter;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.UniquenessHelper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;

public class HandlerInfo {

    private final Map<Integer, HandlerInfo.ParameterWrapper> wrappers = new LinkedHashMap();

    public void wrapParameter(SugarParameter param, Type type, Type generic, BiConsumer<InsnList, Runnable> unwrap) {
        this.wrappers.put(param.paramIndex, new HandlerInfo.ParameterWrapper(type, generic, unwrap));
    }

    public void transformHandler(ClassNode targetClass, MethodNode handler) {
        Type[] paramTypes = Type.getArgumentTypes(handler.desc);
        InsnList insns = new InsnList();
        if (!Bytecode.isStatic(handler)) {
            insns.add(new VarInsnNode(25, 0));
        }
        int index = Bytecode.isStatic(handler) ? 0 : 1;
        for (int i = 0; i < paramTypes.length; i++) {
            VarInsnNode loadInsn = new VarInsnNode(paramTypes[i].getOpcode(21), index);
            HandlerInfo.ParameterWrapper wrapper = (HandlerInfo.ParameterWrapper) this.wrappers.get(i);
            if (wrapper != null) {
                paramTypes[i] = wrapper.type;
                loadInsn.setOpcode(wrapper.type.getOpcode(21));
                wrapper.unwrap.accept(insns, (Runnable) () -> insns.add(loadInsn));
            } else {
                insns.add(loadInsn);
            }
            index += paramTypes[i].getSize();
        }
        insns.add(ASMUtils.getInvokeInstruction(targetClass, handler));
        insns.add(new InsnNode(Type.getReturnType(handler.desc).getOpcode(172)));
        handler.instructions = insns;
        handler.localVariables = null;
        handler.name = UniquenessHelper.getUniqueMethodName(targetClass, handler.name + "$mixinextras$bridge");
        handler.desc = Type.getMethodDescriptor(Type.getReturnType(handler.desc), paramTypes);
    }

    public void transformGenerics(ArrayList<Type> generics) {
        for (Entry<Integer, HandlerInfo.ParameterWrapper> entry : this.wrappers.entrySet()) {
            Type type = ((HandlerInfo.ParameterWrapper) entry.getValue()).generic;
            generics.set((Integer) entry.getKey(), type);
        }
    }

    private static class ParameterWrapper {

        private final Type type;

        private final Type generic;

        private final BiConsumer<InsnList, Runnable> unwrap;

        private ParameterWrapper(Type type, Type generic, BiConsumer<InsnList, Runnable> unwrap) {
            this.type = type;
            this.generic = generic;
            this.unwrap = unwrap;
        }
    }
}