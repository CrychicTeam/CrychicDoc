package ca.fxco.memoryleakfix.mixinextras.wrapper.factory;

import ca.fxco.memoryleakfix.mixinextras.transformer.MixinTransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.util.Annotations;

public class FactoryRedirectWrapperMixinTransformer implements MixinTransformer {

    @Override
    public void transform(IMixinInfo mixinInfo, ClassNode mixinNode) {
        for (MethodNode method : mixinNode.methods) {
            AnnotationNode redirect = Annotations.getVisible(method, Redirect.class);
            if (redirect != null) {
                AnnotationNode at = (AnnotationNode) Annotations.getValue(redirect, "at");
                if (at != null) {
                    String value = (String) Annotations.getValue(at);
                    if ("NEW".equals(value)) {
                        this.wrapInjectorAnnotation(method, redirect);
                    }
                }
            }
        }
    }

    private void wrapInjectorAnnotation(MethodNode method, AnnotationNode redirect) {
        AnnotationNode wrapped = new AnnotationNode(Type.getDescriptor(FactoryRedirectWrapper.class));
        wrapped.visit("original", redirect);
        method.visibleAnnotations.remove(redirect);
        method.visibleAnnotations.add(wrapped);
    }
}