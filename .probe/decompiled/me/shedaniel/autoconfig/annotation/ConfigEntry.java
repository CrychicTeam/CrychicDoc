package me.shedaniel.autoconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigEntry {

    private ConfigEntry() {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface BoundedDiscrete {

        long min() default 0L;

        long max();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Category {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface ColorPicker {

        boolean allowAlpha() default false;
    }

    public static class Gui {

        private Gui() {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface CollapsibleObject {

            boolean startExpanded() default false;
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface EnumHandler {

            ConfigEntry.Gui.EnumHandler.EnumDisplayOption option() default ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN;

            public static enum EnumDisplayOption {

                DROPDOWN, BUTTON
            }
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface Excluded {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface NoTooltip {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface PrefixText {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface RequiresRestart {

            boolean value() default true;
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface Tooltip {

            int count() default 1;
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.FIELD })
        public @interface TransitiveObject {
        }
    }
}