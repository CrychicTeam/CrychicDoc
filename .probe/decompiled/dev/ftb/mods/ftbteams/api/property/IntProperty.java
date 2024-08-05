package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IntProperty extends TeamProperty<Integer> {

    public final int minValue;

    public final int maxValue;

    public IntProperty(ResourceLocation id, Supplier<Integer> def, int min, int max) {
        super(id, def);
        this.minValue = min;
        this.maxValue = max;
    }

    public IntProperty(ResourceLocation id, Supplier<Integer> def) {
        this(id, def, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntProperty(ResourceLocation id, int def, int min, int max) {
        this(id, () -> def, min, max);
    }

    public IntProperty(ResourceLocation id, int def) {
        this(id, () -> def);
    }

    static IntProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return new IntProperty(id, buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
    }

    @Override
    public TeamPropertyType<Integer> getType() {
        return TeamPropertyType.INT;
    }

    @Override
    public Optional<Integer> fromString(String string) {
        try {
            int num = Integer.parseInt(string);
            return Optional.of(Mth.clamp(num, this.minValue, this.maxValue));
        } catch (Exception var3) {
            return Optional.empty();
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.getDefaultValue());
        buf.writeVarInt(this.minValue);
        buf.writeVarInt(this.maxValue);
    }

    @Override
    public void config(ConfigGroup config, TeamPropertyValue<Integer> value) {
        config.addInt(this.id.getPath(), value.value, value.consumer, this.getDefaultValue(), this.minValue, this.maxValue);
    }

    public Tag toNBT(Integer value) {
        return IntTag.valueOf(value);
    }

    @Override
    public Optional<Integer> fromNBT(Tag tag) {
        return tag instanceof NumericTag ? Optional.of(Mth.clamp(((NumericTag) tag).getAsInt(), this.minValue, this.maxValue)) : Optional.empty();
    }
}