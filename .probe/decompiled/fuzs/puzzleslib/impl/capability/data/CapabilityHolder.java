package fuzs.puzzleslib.impl.capability.data;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityHolder<T extends CapabilityComponent> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final Capability<T> capability;

    private final T storage;

    public CapabilityHolder(Capability<T> capability, T storage) {
        this.capability = capability;
        this.storage = storage;
    }

    @Nonnull
    @Override
    public <S> LazyOptional<S> getCapability(@Nonnull Capability<S> capability, @Nullable Direction facing) {
        return capability == this.capability ? LazyOptional.<CapabilityComponent>of(() -> this.storage).cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return this.storage.toCompoundTag();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.storage.read(nbt);
    }
}