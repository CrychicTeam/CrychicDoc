package dev.shadowsoffire.placebo.config;

public class ConfigFlags {

    public static enum Loadability {

        LOCKED("Locked"), RELOADABLE("Reloadable"), RESTARTABLE("Restartable");

        String name;

        private Loadability(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum Type {

        COMMON("Common"), SYNCED("Synced"), SERVER("Server"), CLIENT("Client");

        String name;

        private Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}