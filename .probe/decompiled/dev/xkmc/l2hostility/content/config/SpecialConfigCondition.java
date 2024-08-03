package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public abstract class SpecialConfigCondition<T> {

    private final Class<T> cls;

    @SerialField
    public ResourceLocation id;

    protected SpecialConfigCondition(Class<T> cls) {
        this.cls = cls;
    }

    public Class<T> cls() {
        return this.cls;
    }

    public abstract boolean test(T var1);
}