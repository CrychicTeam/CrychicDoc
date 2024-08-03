package ca.fxco.memoryleakfix.mixinextras.injector;

import ca.fxco.memoryleakfix.mixinextras.utils.InjectorUtils;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

@AnnotationType(ModifyExpressionValue.class)
@HandlerPrefix("modifyExpressionValue")
public class ModifyExpressionValueInjectionInfo extends MixinExtrasInjectionInfo {

    public ModifyExpressionValueInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new ModifyExpressionValueInjector(this);
    }

    public void prepare() {
        super.prepare();
        InjectorUtils.checkForDupedNews(this.targetNodes);
    }
}