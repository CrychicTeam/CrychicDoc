package info.journeymap.shaded.org.eclipse.jetty.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.METHOD })
public @interface ManagedAttribute {

    String value() default "Not Specified";

    String name() default "";

    boolean readonly() default false;

    boolean proxied() default false;

    String setter() default "";
}