package moe.wolfgirl.probejs.lang.java.type.impl;

import com.mojang.datafixers.util.Either;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedWildcardType;
import java.util.Optional;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.lang.java.type.TypeAdapter;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;

public class WildcardType extends TypeDescriptor {

    public Optional<Either<TypeDescriptor, TypeDescriptor>> bound;

    public WildcardType(AnnotatedWildcardType wildcardType, boolean checkBound) {
        super(wildcardType.getAnnotations());
        if (!checkBound) {
            this.bound = Optional.empty();
        } else {
            if (wildcardType.getAnnotatedLowerBounds().length != 0) {
                this.bound = Optional.of(Either.left(TypeAdapter.getTypeDescription(wildcardType.getAnnotatedLowerBounds()[0])));
            } else if (!wildcardType.getAnnotatedUpperBounds()[0].getType().equals(Object.class)) {
                this.bound = Optional.of(Either.right(TypeAdapter.getTypeDescription(wildcardType.getAnnotatedUpperBounds()[0])));
            } else {
                this.bound = Optional.empty();
            }
        }
    }

    public WildcardType(java.lang.reflect.WildcardType wildcardType, boolean checkBound) {
        super(new Annotation[0]);
        if (!checkBound) {
            this.bound = Optional.empty();
        } else {
            if (wildcardType.getLowerBounds().length != 0) {
                this.bound = Optional.of(Either.left(TypeAdapter.getTypeDescription(wildcardType.getLowerBounds()[0])));
            } else if (!wildcardType.getUpperBounds()[0].equals(Object.class)) {
                this.bound = Optional.of(Either.right(TypeAdapter.getTypeDescription(wildcardType.getUpperBounds()[0])));
            } else {
                this.bound = Optional.empty();
            }
        }
    }

    @Override
    public Stream<TypeDescriptor> stream() {
        if (this.bound.isEmpty()) {
            return Stream.empty();
        } else {
            Either<TypeDescriptor, TypeDescriptor> inner = (Either<TypeDescriptor, TypeDescriptor>) this.bound.get();
            if (inner.left().isPresent()) {
                return ((TypeDescriptor) inner.left().get()).stream();
            } else if (inner.right().isPresent()) {
                return ((TypeDescriptor) inner.right().get()).stream();
            } else {
                throw new RuntimeException("Impossible");
            }
        }
    }
}