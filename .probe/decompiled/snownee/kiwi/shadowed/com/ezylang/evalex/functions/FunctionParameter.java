package snownee.kiwi.shadowed.com.ezylang.evalex.functions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.TYPE })
@Repeatable(FunctionParameters.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParameter {

    String name();

    boolean isLazy() default false;

    boolean isVarArg() default false;

    boolean nonZero() default false;

    boolean nonNegative() default false;
}