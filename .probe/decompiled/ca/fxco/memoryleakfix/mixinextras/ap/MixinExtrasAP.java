package ca.fxco.memoryleakfix.mixinextras.ap;

import ca.fxco.memoryleakfix.mixinextras.injector.ModifyExpressionValueInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReceiverInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReturnValueInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.WrapWithConditionInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperationInjectionInfo;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.util.logging.MessageRouter;

@SupportedAnnotationTypes({})
public class MixinExtrasAP extends AbstractProcessor {

    private static boolean setup() {
        try {
            MessageRouter.setMessager(new StdoutMessager());
            return true;
        } catch (NoClassDefFoundError var1) {
            return false;
        }
    }

    private static void registerInjectors() {
        InjectionInfo.register(ModifyExpressionValueInjectionInfo.class);
        InjectionInfo.register(ModifyReceiverInjectionInfo.class);
        InjectionInfo.register(ModifyReturnValueInjectionInfo.class);
        InjectionInfo.register(WrapOperationInjectionInfo.class);
        InjectionInfo.register(WrapWithConditionInjectionInfo.class);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    static {
        if (setup()) {
            registerInjectors();
        }
    }
}