package snownee.kiwi.shadowed.com.ezylang.evalex.operators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PostfixOperator {

    int precedence() default 60;

    boolean leftAssociative() default true;
}