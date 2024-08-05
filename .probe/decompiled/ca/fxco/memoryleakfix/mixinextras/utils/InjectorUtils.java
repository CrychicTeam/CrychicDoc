package ca.fxco.memoryleakfix.mixinextras.utils;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Bytecode;

public class InjectorUtils {

    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("InjectorUtils");

    public static boolean isVirtualRedirect(InjectionNode node) {
        return node.isReplaced() && node.hasDecoration("redirector") && node.getCurrentTarget().getOpcode() != 184;
    }

    public static boolean isDynamicInstanceofRedirect(InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        return originalTarget.getOpcode() == 193 && currentTarget instanceof MethodInsnNode && Type.getReturnType(((MethodInsnNode) currentTarget).desc).equals(Type.getType(Class.class));
    }

    public static void checkForDupedNews(Map<Target, List<InjectionNode>> targets) {
        for (Entry<Target, List<InjectionNode>> entry : targets.entrySet()) {
            for (InjectionNode node : (List) entry.getValue()) {
                AbstractInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget.getOpcode() == 187 && currentTarget.getNext().getOpcode() == 89) {
                    node.decorate("mixinextras_newIsDuped", true);
                }
            }
        }
    }

    public static boolean isDupedNew(InjectionNode node) {
        AbstractInsnNode currentTarget = node.getCurrentTarget();
        return currentTarget != null && currentTarget.getOpcode() == 187 && node.hasDecoration("mixinextras_newIsDuped");
    }

    public static boolean isDupedFactoryRedirect(InjectionNode node) {
        AbstractInsnNode originalTarget = node.getOriginalTarget();
        return node.isReplaced() && originalTarget.getOpcode() == 187 && !node.hasDecoration("mixinextras_wrappedOperation") && node.hasDecoration("mixinextras_newIsDuped");
    }

    public static AbstractInsnNode findFactoryRedirectThrowString(Target target, AbstractInsnNode start) {
        ListIterator<AbstractInsnNode> it = target.insns.iterator(target.indexOf(start));
        while (it.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode) it.next();
            if (insn instanceof LdcInsnNode) {
                LdcInsnNode ldc = (LdcInsnNode) insn;
                if (ldc.cst instanceof String && ((String) ldc.cst).startsWith("@Redirect constructor handler ")) {
                    return ldc;
                }
            }
        }
        LOGGER.warn("Please inform LlamaLad7! Failed to find factory redirect throw string for {}", Bytecode.describeNode(start));
        return null;
    }
}