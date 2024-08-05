package info.journeymap.shaded.org.javax.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartConfig {

    String location() default "";

    long maxFileSize() default -1L;

    long maxRequestSize() default -1L;

    int fileSizeThreshold() default 0;
}