package dev.xkmc.l2backpack.content.remote.common;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

@SerialClass
public class WorldStorageCapability implements ICapabilitySerializable<CompoundTag> {

    public final ServerLevel w;

    public final WorldStorage handler;

    public final LazyOptional<WorldStorage> lo;

    public WorldStorageCapability(ServerLevel level) {
        this.w = level;
        this.handler = new WorldStorage(level);
        this.lo = LazyOptional.of(() -> this.handler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return capability == WorldStorage.CAPABILITY ? this.lo.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return TagCodec.toTag(new CompoundTag(), this.lo.resolve().get());
    }

    public void deserializeNBT(CompoundTag tag) {
        Wrappers.get(() -> (WorldStorage) TagCodec.fromTag(tag, WorldStorage.class, this.handler, f -> true));
        this.handler.init();
    }
}