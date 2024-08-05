package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class TeamProperty<T> {

    protected final ResourceLocation id;

    private final Supplier<T> defaultValue;

    protected TeamProperty(ResourceLocation _id, Supplier<T> def) {
        this.id = _id;
        this.defaultValue = def;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public T getDefaultValue() {
        return (T) this.defaultValue.get();
    }

    public String getTranslationKey(String prefix) {
        return prefix + "." + this.id.getNamespace() + "." + this.id.getPath();
    }

    public abstract TeamPropertyType<T> getType();

    public abstract Optional<T> fromString(String var1);

    public abstract void write(FriendlyByteBuf var1);

    public String toString(T value) {
        return value.toString();
    }

    public final int hashCode() {
        return this.id.hashCode();
    }

    public final String toString() {
        return this.id.toString();
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return o instanceof TeamProperty ? this.id.equals(((TeamProperty) o).id) : false;
        }
    }

    public void writeValue(FriendlyByteBuf buf, T value) {
        buf.writeUtf(this.toString(value), 32767);
    }

    public T readValue(FriendlyByteBuf buf) {
        return (T) this.fromString(buf.readUtf(32767)).orElse(this.getDefaultValue());
    }

    public void config(ConfigGroup config, TeamPropertyValue<T> value) {
    }

    public Tag toNBT(T value) {
        return StringTag.valueOf(this.toString(value));
    }

    public Optional<T> fromNBT(Tag tag) {
        return this.fromString(tag.getAsString());
    }

    public TeamPropertyValue<T> createDefaultValue() {
        return new TeamPropertyValue<>(this, this.getDefaultValue());
    }

    public TeamPropertyValue<T> createValueFromNetwork(FriendlyByteBuf buf) {
        return new TeamPropertyValue<>(this, this.readValue(buf));
    }

    public TeamPropertyValue<T> createValueFromNBT(Tag tag) {
        return new TeamPropertyValue<>(this, (T) this.fromNBT(tag).orElse(this.getDefaultValue()));
    }
}