package me.shedaniel.autoconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Config {

    String name();

    public static class Gui {

        private Gui() {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.TYPE })
        public @interface Background {

            String TRANSPARENT = "cloth-config2:transparent";

            String value();
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.TYPE })
        @Repeatable(Config.Gui.CategoryBackgrounds.class)
        public @interface CategoryBackground {

            String category();

            String background();
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.TYPE })
        public @interface CategoryBackgrounds {

            Config.Gui.CategoryBackground[] value();
        }
    }
}