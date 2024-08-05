package ca.fxco.memoryleakfix.mixinextras.utils;

import ca.fxco.memoryleakfix.mixinextras.versions.MixinVersion;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator.Context;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class CompatibilityHelper {

    public static RuntimeException makeInvalidInjectionException(InjectionInfo info, String message) {
        return MixinVersion.getInstance().makeInvalidInjectionException(info, message);
    }

    public static IMixinContext getMixin(InjectionInfo info) {
        return MixinVersion.getInstance().getMixin(info);
    }

    public static Context makeLvtContext(InjectionInfo info, Type returnType, boolean argsOnly, Target target, AbstractInsnNode node) {
        return MixinVersion.getInstance().makeLvtContext(info, returnType, argsOnly, target, node);
    }

    public static void preInject(InjectionInfo info) {
        MixinVersion.getInstance().preInject(info);
    }

    public static AnnotationNode getAnnotation(InjectionInfo info) {
        return MixinVersion.getInstance().getAnnotation(info);
    }
}