package moe.wolfgirl.probejs.lang.java.type;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.lang.java.base.AnnotationHolder;
import moe.wolfgirl.probejs.lang.java.base.ClassPathProvider;
import moe.wolfgirl.probejs.lang.java.base.ClassProvider;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;

public abstract class TypeDescriptor extends AnnotationHolder implements ClassPathProvider, ClassProvider {

    public TypeDescriptor(Annotation[] annotations) {
        super(annotations);
    }

    public abstract Stream<TypeDescriptor> stream();

    @Override
    public Collection<ClassPath> getClassPaths() {
        return (Collection<ClassPath>) this.stream().flatMap(t -> t.getClassPaths().stream()).collect(Collectors.toSet());
    }

    @Override
    public Collection<Class<?>> getClasses() {
        return (Collection<Class<?>>) this.stream().flatMap(t -> t.getClasses().stream()).collect(Collectors.toSet());
    }
}