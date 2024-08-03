package dev.xkmc.l2library.capability.entity;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

@SerialClass
public class GeneralCapabilitySerializer<E extends ICapabilityProvider, C extends GeneralCapabilityTemplate<E, C>> implements ICapabilitySerializable<CompoundTag> {

    public final GeneralCapabilityHolder<E, C> holder;

    public C handler;

    public LazyOptional<C> lo = LazyOptional.of(() -> this.handler);

    public GeneralCapabilitySerializer(GeneralCapabilityHolder<E, C> holder) {
        this.holder = holder;
        this.handler = (C) holder.sup.get();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return capability == this.holder.capability ? this.lo.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return TagCodec.toTag(new CompoundTag(), this.lo.resolve().get());
    }

    public void deserializeNBT(CompoundTag tag) {
        Wrappers.get(() -> (GeneralCapabilityTemplate) TagCodec.fromTag(tag, this.holder.holder_class, this.handler, f -> true));
    }
}