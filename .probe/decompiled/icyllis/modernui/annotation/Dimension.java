package icyllis.modernui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
public @interface Dimension {

    int DP = 0;

    int PX = 1;

    int SP = 2;

    int unit() default 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}