package team.lodestar.lodestone.systems.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class LodestoneCapabilityProvider<C extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final C instance;

    private final LazyOptional<C> capOptional;

    private final Capability<C> capability;

    public LodestoneCapabilityProvider(Capability<C> capability, NonNullSupplier<C> capInstance) {
        this.capability = capability;
        this.instance = capInstance.get();
        this.capOptional = LazyOptional.of(capInstance);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.capability.orEmpty(cap, this.capOptional);
    }

    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }
}