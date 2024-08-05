package ca.fxco.memoryleakfix.mixinextras.injector;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

@AnnotationType(ModifyReturnValue.class)
@HandlerPrefix("modifyReturnValue")
public class ModifyReturnValueInjectionInfo extends MixinExtrasInjectionInfo {

    public ModifyReturnValueInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new ModifyReturnValueInjector(this);
    }
}