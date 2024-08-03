package net.minecraft.world.entity.ai.attributes;

public class Attribute {

    public static final int MAX_NAME_LENGTH = 64;

    private final double defaultValue;

    private boolean syncable;

    private final String descriptionId;

    protected Attribute(String string0, double double1) {
        this.defaultValue = double1;
        this.descriptionId = string0;
    }

    public double getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isClientSyncable() {
        return this.syncable;
    }

    public Attribute setSyncable(boolean boolean0) {
        this.syncable = boolean0;
        return this;
    }

    public double sanitizeValue(double double0) {
        return double0;
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }
}