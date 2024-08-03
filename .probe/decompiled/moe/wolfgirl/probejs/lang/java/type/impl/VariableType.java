package moe.wolfgirl.probejs.lang.java.type.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.lang.java.type.TypeAdapter;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;

public class VariableType extends TypeDescriptor {

    public String symbol;

    public List<TypeDescriptor> descriptors;

    public VariableType(AnnotatedTypeVariable typeVariable) {
        this(typeVariable, true);
    }

    public VariableType(TypeVariable<?> typeVariable) {
        this(typeVariable, true);
    }

    public VariableType(AnnotatedTypeVariable typeVariable, boolean checkBounds) {
        super(typeVariable.getAnnotations());
        this.symbol = ((TypeVariable) typeVariable.getType()).getName();
        this.descriptors = (List<TypeDescriptor>) (checkBounds ? (List) Arrays.stream(typeVariable.getAnnotatedBounds()).filter(bound -> !bound.getType().equals(Object.class)).map(TypeAdapter::getTypeDescription).collect(Collectors.toList()) : new ArrayList());
    }

    public VariableType(TypeVariable<?> typeVariable, boolean checkBounds) {
        super(new Annotation[0]);
        this.symbol = typeVariable.getName();
        this.descriptors = (List<TypeDescriptor>) (checkBounds ? (List) Arrays.stream(typeVariable.getAnnotatedBounds()).filter(bound -> !bound.getType().equals(Object.class)).map(TypeAdapter::getTypeDescription).collect(Collectors.toList()) : new ArrayList());
    }

    @Override
    public Stream<TypeDescriptor> stream() {
        return this.descriptors.stream().flatMap(TypeDescriptor::stream);
    }

    public String getSymbol() {
        return this.symbol;
    }

    public List<TypeDescriptor> getDescriptors() {
        return this.descriptors;
    }
}