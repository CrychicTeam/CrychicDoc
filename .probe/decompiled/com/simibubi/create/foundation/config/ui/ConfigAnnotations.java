package com.simibubi.create.foundation.config.ui;

public class ConfigAnnotations {

    public interface ConfigAnnotation {

        String getName();

        default String getValue() {
            return null;
        }

        default String asComment() {
            String comment = "[@cui:" + this.getName();
            String value = this.getValue();
            if (value != null) {
                comment = comment + ":" + value;
            }
            return comment + "]";
        }
    }

    public static class Execute implements ConfigAnnotations.ConfigAnnotation {

        private final String command;

        public static ConfigAnnotations.Execute run(String command) {
            return new ConfigAnnotations.Execute(command);
        }

        private Execute(String command) {
            this.command = command;
        }

        @Override
        public String getName() {
            return "Execute";
        }

        @Override
        public String getValue() {
            return this.command;
        }
    }

    public static enum IntDisplay implements ConfigAnnotations.ConfigAnnotation {

        HEX("#"), ZERO_X("0x"), ZERO_B("0b");

        private final String value;

        private IntDisplay(String value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return "IntDisplay";
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    public static enum RequiresRelog implements ConfigAnnotations.ConfigAnnotation {

        TRUE;

        @Override
        public String getName() {
            return "RequiresRelog";
        }
    }

    public static enum RequiresRestart implements ConfigAnnotations.ConfigAnnotation {

        CLIENT("client"), SERVER("server"), BOTH("both");

        private final String value;

        private RequiresRestart(String value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return "RequiresReload";
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
}