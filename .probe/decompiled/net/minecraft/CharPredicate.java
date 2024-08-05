package net.minecraft;

import java.util.Objects;

@FunctionalInterface
public interface CharPredicate {

    boolean test(char var1);

    default CharPredicate and(CharPredicate charPredicate0) {
        Objects.requireNonNull(charPredicate0);
        return p_178295_ -> this.test(p_178295_) && charPredicate0.test(p_178295_);
    }

    default CharPredicate negate() {
        return p_178285_ -> !this.test(p_178285_);
    }

    default CharPredicate or(CharPredicate charPredicate0) {
        Objects.requireNonNull(charPredicate0);
        return p_178290_ -> this.test(p_178290_) || charPredicate0.test(p_178290_);
    }
}