package fuzs.puzzleslib.api.config.v3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Config {

    String name() default "";

    String[] description() default {};

    String[] category() default {};

    boolean worldRestart() default false;

    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AllowedValues {

        String[] values();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface DoubleRange {

        double min() default Double.MIN_VALUE;

        double max() default Double.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface IntRange {

        int min() default Integer.MIN_VALUE;

        int max() default Integer.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface LongRange {

        long min() default Long.MIN_VALUE;

        long max() default Long.MAX_VALUE;
    }
}