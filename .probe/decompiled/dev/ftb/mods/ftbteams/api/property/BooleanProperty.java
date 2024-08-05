package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class BooleanProperty extends TeamProperty<Boolean> {

    private static final Optional<Boolean> TRUE = Optional.of(Boolean.TRUE);

    private static final Optional<Boolean> FALSE = Optional.of(Boolean.FALSE);

    public BooleanProperty(ResourceLocation id, Supplier<Boolean> def) {
        super(id, def);
    }

    public BooleanProperty(ResourceLocation id, Boolean def) {
        this(id, (Supplier<Boolean>) (() -> def));
    }

    static BooleanProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        boolean val = buf.readBoolean();
        return new BooleanProperty(id, (Supplier<Boolean>) (() -> val));
    }

    @Override
    public TeamPropertyType<Boolean> getType() {
        return TeamPropertyType.BOOLEAN;
    }

    @Override
    public Optional<Boolean> fromString(String string) {
        if (string.equals("true")) {
            return TRUE;
        } else {
            return string.equals("false") ? FALSE : Optional.empty();
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.getDefaultValue());
    }

    @Override
    public void config(ConfigGroup config, TeamPropertyValue<Boolean> value) {
        config.addBool(this.id.getPath(), value.value, value.consumer, this.getDefaultValue());
    }

    public Tag toNBT(Boolean value) {
        return ByteTag.valueOf(value);
    }

    @Override
    public Optional<Boolean> fromNBT(Tag tag) {
        if (tag instanceof NumericTag) {
            return ((NumericTag) tag).getAsByte() == 1 ? TRUE : FALSE;
        } else {
            return Optional.empty();
        }
    }
}