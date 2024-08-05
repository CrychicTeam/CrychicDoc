package moe.wolfgirl.probejs.lang.java.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;

public abstract class TypeVariableHolder extends AnnotationHolder {

    public final List<VariableType> variableTypes;

    public TypeVariableHolder(TypeVariable<?>[] variables, Annotation[] annotations) {
        super(annotations);
        this.variableTypes = (List<VariableType>) Arrays.stream(variables).map(VariableType::new).collect(Collectors.toList());
    }
}