package de.keksuccino.fancymenu.util;

import de.keksuccino.konkrete.config.Config;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractOptions {

    public static class Option<T> {

        protected final Config config;

        protected final String key;

        protected final T defaultValue;

        protected final String category;

        public Option(@NotNull Config config, @NotNull String key, @NotNull T defaultValue, @NotNull String category) {
            this.config = (Config) Objects.requireNonNull(config);
            this.key = (String) Objects.requireNonNull(key);
            this.defaultValue = (T) Objects.requireNonNull(defaultValue);
            this.category = (String) Objects.requireNonNull(category);
            this.register();
        }

        protected void register() {
            boolean unsupported = false;
            try {
                if (this.defaultValue instanceof Integer) {
                    this.config.registerValue(this.key, (Integer) this.defaultValue, this.category);
                } else if (this.defaultValue instanceof Double) {
                    this.config.registerValue(this.key, (Double) this.defaultValue, this.category);
                } else if (this.defaultValue instanceof Long) {
                    this.config.registerValue(this.key, (Long) this.defaultValue, this.category);
                } else if (this.defaultValue instanceof Float) {
                    this.config.registerValue(this.key, (Float) this.defaultValue, this.category);
                } else if (this.defaultValue instanceof Boolean) {
                    this.config.registerValue(this.key, (Boolean) this.defaultValue, this.category);
                } else if (this.defaultValue instanceof String) {
                    this.config.registerValue(this.key, (String) this.defaultValue, this.category);
                } else {
                    unsupported = true;
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
            if (unsupported) {
                throw new AbstractOptions.UnsupportedOptionTypeException("Tried to register Option with unsupported type: " + this.key + " (" + this.defaultValue.getClass().getName() + ")");
            }
        }

        @NotNull
        public T getValue() {
            return this.config.getOrDefault(this.key, this.defaultValue);
        }

        public AbstractOptions.Option<T> setValue(T value) {
            try {
                if (value == null) {
                    value = this.getDefaultValue();
                }
                if (value instanceof Integer) {
                    this.config.setValue(this.key, (Integer) value);
                } else if (value instanceof Double) {
                    this.config.setValue(this.key, (Double) value);
                } else if (value instanceof Long) {
                    this.config.setValue(this.key, (Long) value);
                } else if (value instanceof Float) {
                    this.config.setValue(this.key, (Float) value);
                } else if (value instanceof Boolean) {
                    this.config.setValue(this.key, (Boolean) value);
                } else if (value instanceof String) {
                    this.config.setValue(this.key, (String) value);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
            return this;
        }

        public AbstractOptions.Option<T> resetToDefault() {
            this.setValue(null);
            return this;
        }

        @NotNull
        public T getDefaultValue() {
            return this.defaultValue;
        }

        @NotNull
        public String getKey() {
            return this.key;
        }
    }

    public static class UnsupportedOptionTypeException extends RuntimeException {

        public UnsupportedOptionTypeException() {
        }

        public UnsupportedOptionTypeException(String msg) {
            super(msg);
        }
    }
}