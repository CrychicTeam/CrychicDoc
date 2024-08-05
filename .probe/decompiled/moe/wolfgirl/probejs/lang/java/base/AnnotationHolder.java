package moe.wolfgirl.probejs.lang.java.base;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class AnnotationHolder {

    private final Annotation[] annotations;

    public AnnotationHolder(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return Arrays.stream(this.annotations).anyMatch(annotation::isInstance);
    }

    public <T extends Annotation> List<T> getAnnotations(Class<T> type) {
        return Arrays.stream(this.annotations).filter(type::isInstance).map(a -> a).toList();
    }

    public <T extends Annotation> T getAnnotation(Class<T> type) {
        List<T> annotations = this.getAnnotations(type);
        return (T) (annotations.isEmpty() ? null : annotations.get(0));
    }
}