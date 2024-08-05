package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChunkDifficultyCap implements ICapabilitySerializable<CompoundTag> {

    public final LevelChunk w;

    public final ChunkDifficulty handler;

    public final LazyOptional<ChunkDifficulty> lo;

    public ChunkDifficultyCap(LevelChunk level) {
        this.w = level;
        this.handler = new ChunkDifficulty(level);
        this.handler.init();
        this.lo = LazyOptional.of(() -> this.handler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return capability == ChunkDifficulty.CAPABILITY ? this.lo.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return TagCodec.toTag(new CompoundTag(), this.lo.resolve().get());
    }

    public void deserializeNBT(CompoundTag tag) {
        Wrappers.get(() -> (ChunkDifficulty) TagCodec.fromTag(tag, ChunkDifficulty.class, this.handler, f -> true));
    }
}