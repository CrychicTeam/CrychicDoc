package net.blay09.mods.balm.api.config;

import java.lang.reflect.Field;

public class BalmConfigPropertyImpl<T> implements BalmConfigProperty<T> {

    private final BalmConfigData configData;

    private final Field categoryField;

    private final Field propertyField;

    private final BalmConfigData defaultConfig;

    public BalmConfigPropertyImpl(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        this.configData = configData;
        this.categoryField = categoryField;
        this.propertyField = propertyField;
        this.defaultConfig = defaultConfig;
    }

    @Override
    public Class<T> getType() {
        return this.propertyField.getType();
    }

    @Override
    public Class<T> getInnerType() {
        ExpectedType expectedTypeAnnotation = (ExpectedType) this.propertyField.getAnnotation(ExpectedType.class);
        return (Class<T>) (expectedTypeAnnotation != null ? expectedTypeAnnotation.value() : null);
    }

    @Override
    public void setValue(T value) {
        try {
            Object instance = this.categoryField != null ? this.categoryField.get(this.configData) : this.configData;
            this.propertyField.set(instance, value);
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public T getValue() {
        try {
            Object instance = this.categoryField != null ? this.categoryField.get(this.configData) : this.configData;
            return (T) this.propertyField.get(instance);
        } catch (IllegalAccessException var2) {
            var2.printStackTrace();
            return this.getDefaultValue();
        }
    }

    @Override
    public T getDefaultValue() {
        try {
            Object instance = this.categoryField != null ? this.categoryField.get(this.defaultConfig) : this.defaultConfig;
            return (T) this.propertyField.get(instance);
        } catch (IllegalAccessException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}