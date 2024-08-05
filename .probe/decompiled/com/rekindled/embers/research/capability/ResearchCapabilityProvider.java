package com.rekindled.embers.research.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ResearchCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private IResearchCapability capability;

    public LazyOptional<IResearchCapability> holder = LazyOptional.of(() -> this.capability);

    public static final Capability<IResearchCapability> researchCapability = CapabilityManager.get(new CapabilityToken<IResearchCapability>() {
    });

    public ResearchCapabilityProvider() {
        this.capability = new DefaultResearchCapability();
        this.holder = LazyOptional.of(() -> this.capability);
    }

    public ResearchCapabilityProvider(IResearchCapability capability) {
        this.capability = capability;
        this.holder = LazyOptional.of(() -> this.capability);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return researchCapability != null && capability == researchCapability ? researchCapability.orEmpty(capability, this.holder) : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        this.capability.writeToNBT(compound);
        return compound;
    }

    public void deserializeNBT(CompoundTag compound) {
        this.capability.readFromNBT(compound);
    }
}