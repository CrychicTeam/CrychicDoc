package ca.fxco.memoryleakfix.mixinextras;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.spongepowered.asm.util.logging.MessageRouter;

@SupportedAnnotationTypes({})
public class MixinExtrasAP extends AbstractProcessor {

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        try {
            MessageRouter.setMessager(processingEnv.getMessager());
            MixinExtrasBootstrap.initialize(false);
        } catch (NoClassDefFoundError var3) {
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}