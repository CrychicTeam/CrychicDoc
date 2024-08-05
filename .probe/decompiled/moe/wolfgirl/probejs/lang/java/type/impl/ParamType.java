package moe.wolfgirl.probejs.lang.java.type.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.lang.java.type.TypeAdapter;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;

public class ParamType extends TypeDescriptor {

    public TypeDescriptor base;

    public final List<TypeDescriptor> params;

    public ParamType(AnnotatedParameterizedType annotatedType) {
        super(annotatedType.getAnnotations());
        this.base = TypeAdapter.getTypeDescription(((ParameterizedType) annotatedType.getType()).getRawType(), false);
        this.params = (List<TypeDescriptor>) Arrays.stream(annotatedType.getAnnotatedActualTypeArguments()).map(t -> TypeAdapter.getTypeDescription(t, false)).collect(Collectors.toList());
    }

    public ParamType(ParameterizedType parameterizedType) {
        super(new Annotation[0]);
        this.base = TypeAdapter.getTypeDescription(parameterizedType.getRawType(), false);
        this.params = (List<TypeDescriptor>) Arrays.stream(parameterizedType.getActualTypeArguments()).map(t -> TypeAdapter.getTypeDescription(t, false)).collect(Collectors.toList());
    }

    public ParamType(Annotation[] annotations, TypeDescriptor base, List<TypeDescriptor> params) {
        super(annotations);
        this.base = base;
        this.params = params;
    }

    @Override
    public Stream<TypeDescriptor> stream() {
        return Stream.concat(this.base.stream(), this.params.stream().flatMap(TypeDescriptor::stream));
    }

    public int hashCode() {
        return this.base.hashCode() * 31 + this.params.hashCode();
    }
}