package icyllis.modernui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
public @interface FloatRange {

    double from() default Double.NEGATIVE_INFINITY;

    double to() default Double.POSITIVE_INFINITY;

    boolean fromInclusive() default true;

    boolean toInclusive() default true;
}