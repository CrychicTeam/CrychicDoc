package io.github.lightman314.lightmanscurrency.api.notifications;

import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullFunction;

public final class NotificationCategoryType<T extends NotificationCategory> {

    public final ResourceLocation type;

    private final NonNullFunction<CompoundTag, T> generator;

    public NotificationCategoryType(@Nonnull ResourceLocation type, @Nonnull NonNullFunction<CompoundTag, T> generator) {
        this.type = type;
        this.generator = generator;
    }

    @Nonnull
    public T load(@Nonnull CompoundTag tag) {
        return this.generator.apply(tag);
    }

    public String toString() {
        return this.type.toString();
    }
}