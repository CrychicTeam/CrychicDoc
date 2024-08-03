package moe.wolfgirl.probejs.lang.java.type.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;

public class ClassType extends TypeDescriptor {

    public final ClassPath classPath;

    public final Class<?> clazz;

    public ClassType(AnnotatedType type) {
        super(type.getAnnotations());
        this.clazz = (Class<?>) type.getType();
        this.classPath = new ClassPath(this.clazz);
    }

    public ClassType(Type type) {
        super(new Annotation[0]);
        this.clazz = (Class<?>) type;
        this.classPath = new ClassPath(this.clazz);
    }

    @Override
    public Stream<TypeDescriptor> stream() {
        return Stream.of(this);
    }

    @Override
    public Collection<ClassPath> getClassPaths() {
        return List.of(this.classPath);
    }

    @Override
    public Collection<Class<?>> getClasses() {
        return List.of(this.clazz);
    }

    public int hashCode() {
        return this.classPath.hashCode();
    }
}