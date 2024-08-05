package snownee.kiwi.build;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.io.Closeables;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;
import snownee.kiwi.KiwiAnnotationData;

@SupportedAnnotationTypes({ "snownee.kiwi.KiwiModule", "snownee.kiwi.KiwiModule.Optional", "snownee.kiwi.KiwiModule.LoadingCondition", "snownee.kiwi.config.KiwiConfig", "snownee.kiwi.network.KiwiPacket", "net.minecraftforge.fml.common.Mod" })
public class KiwiAnnotationProcessor extends AbstractProcessor {

    Messager messager;

    Filer filer;

    String modId;

    Elements elementUtils;

    TypeElement skipType;

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
        this.skipType = processingEnv.getElementUtils().getTypeElement("snownee.kiwi.KiwiModule.Skip");
        Objects.requireNonNull(this.skipType);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return true;
        } else {
            this.messager.printMessage(Kind.NOTE, "KiwiAnnotationProcessor is processing");
            Multimap<String, KiwiAnnotationData> map = Multimaps.newMultimap(Maps.newTreeMap(), Lists::newArrayList);
            for (TypeElement annotation : annotations) {
                String className = annotation.getQualifiedName().toString();
                ElementKind elementKind = ElementKind.CLASS;
                if ("snownee.kiwi.KiwiModule.Optional".equals(className)) {
                    className = "snownee.kiwi.KiwiModule$Optional";
                } else if ("snownee.kiwi.KiwiModule.LoadingCondition".equals(className)) {
                    className = "snownee.kiwi.KiwiModule$LoadingCondition";
                    elementKind = ElementKind.METHOD;
                }
                for (Element ele : roundEnv.getElementsAnnotatedWith(annotation)) {
                    if (ele.getKind() != elementKind) {
                        this.messager.printMessage(Kind.ERROR, "Annotated element is not matched", ele);
                    } else {
                        AnnotationMirror a = this.getAnnotation(ele, annotation);
                        if (a != null) {
                            Map<String, Object> o = Maps.newHashMap();
                            for (Entry<? extends ExecutableElement, ? extends AnnotationValue> e : a.getElementValues().entrySet()) {
                                o.put(((ExecutableElement) e.getKey()).getSimpleName().toString(), mapValue((AnnotationValue) e.getValue()));
                            }
                            if ("net.minecraftforge.fml.common.Mod".equals(className)) {
                                if (this.modId == null) {
                                    this.modId = (String) o.get("value");
                                } else {
                                    this.messager.printMessage(Kind.ERROR, "Found more than one @Mod");
                                }
                            } else {
                                String target;
                                if (elementKind == ElementKind.METHOD) {
                                    target = ele.getEnclosingElement().toString();
                                    o.put("method", ele.getSimpleName().toString());
                                } else {
                                    target = ele.toString();
                                }
                                if (!target.startsWith("snownee.kiwi.test.")) {
                                    map.put(annotation.getSimpleName().toString(), new KiwiAnnotationData(target, o.isEmpty() ? null : o));
                                }
                            }
                        }
                    }
                }
            }
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(map.asMap());
            PrintWriter writer = null;
            try {
                FileObject file = this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", this.modId + ".kiwi.json", new Element[0]);
                writer = new PrintWriter(file.openWriter());
                writer.write(json);
            } catch (IOException var23) {
                this.messager.printMessage(Kind.ERROR, var23.toString());
            } finally {
                try {
                    Closeables.close(writer, true);
                } catch (IOException var22) {
                }
            }
            return true;
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private AnnotationMirror getAnnotation(Element elem, TypeElement annotation2) {
        if (elem == null) {
            return null;
        } else {
            List<? extends AnnotationMirror> annotations = elem.getAnnotationMirrors();
            if (annotations == null) {
                return null;
            } else {
                AnnotationMirror found = null;
                for (AnnotationMirror annotation : annotations) {
                    if (annotation.getAnnotationType().asElement() instanceof TypeElement annotationElement) {
                        if (annotationElement.equals(this.skipType)) {
                            return null;
                        }
                        if (annotationElement.equals(annotation2)) {
                            found = annotation;
                        }
                    }
                }
                return found;
            }
        }
    }

    private static Object mapValue(AnnotationValue av) {
        Object v = av.getValue();
        if (v instanceof VariableElement) {
            v = v.toString();
        } else if (v instanceof List) {
            v = ((List) v).stream().map(KiwiAnnotationProcessor::mapValue).toList();
        }
        return v;
    }
}