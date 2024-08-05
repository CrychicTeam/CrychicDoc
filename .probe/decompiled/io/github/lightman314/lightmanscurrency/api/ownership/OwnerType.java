package io.github.lightman314.lightmanscurrency.api.ownership;

import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class OwnerType {

    private final ResourceLocation type;

    private final Function<CompoundTag, Owner> deserializer;

    private OwnerType(@Nonnull ResourceLocation type, @Nonnull Function<CompoundTag, Owner> deserializer) {
        this.type = type;
        this.deserializer = deserializer;
    }

    public static OwnerType create(@Nonnull ResourceLocation type, @Nonnull Function<CompoundTag, Owner> deserializer) {
        return new OwnerType(type, deserializer);
    }

    @Nonnull
    public ResourceLocation getID() {
        return this.type;
    }

    @Nonnull
    public Owner load(@Nonnull CompoundTag tag) {
        return (Owner) this.deserializer.apply(tag);
    }
}