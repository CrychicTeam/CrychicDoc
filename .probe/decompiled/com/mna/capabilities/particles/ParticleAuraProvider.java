package com.mna.capabilities.particles;

import com.mna.ManaAndArtifice;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ParticleAuraProvider implements ICapabilitySerializable<Tag> {

    public static final Capability<ParticleAura> AURA = CapabilityManager.get(new CapabilityToken<ParticleAura>() {
    });

    private final LazyOptional<ParticleAura> holder = LazyOptional.of(ParticleAura::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return AURA.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        ParticleAura instance = this.holder.orElse(null);
        return instance.save();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        ParticleAura instance = this.holder.orElse(null);
        if (nbt instanceof CompoundTag cnbt) {
            instance.load(cnbt);
        } else {
            ManaAndArtifice.LOGGER.error("Aura NBT passed back not an instance of CompoundNBT - save data was NOT loaded!");
        }
    }
}