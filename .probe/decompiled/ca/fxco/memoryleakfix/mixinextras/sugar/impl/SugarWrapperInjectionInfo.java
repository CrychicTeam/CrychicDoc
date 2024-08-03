package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.wrapper.WrapperInjectionInfo;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

@AnnotationType(SugarWrapper.class)
@HandlerPrefix("sugarWrapper")
public class SugarWrapperInjectionInfo extends WrapperInjectionInfo {

    public SugarWrapperInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(SugarWrapperImpl::new, mixin, method, annotation);
    }
}