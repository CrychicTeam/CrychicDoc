package snownee.kiwi.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface KiwiConfig {

    String value() default "";

    KiwiConfig.ConfigType type() default KiwiConfig.ConfigType.COMMON;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface AdvancedPath {

        String[] value();
    }

    public static enum ConfigType {

        COMMON, CLIENT, SERVER;

        public String extension() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface GameRestart {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface LevelRestart {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    @Repeatable(KiwiConfig.Listens.class)
    public @interface Listen {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public @interface Listens {

        KiwiConfig.Listen[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface Path {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Range {

        double min() default Double.NaN;

        double max() default Double.NaN;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Translation {

        String value();
    }
}