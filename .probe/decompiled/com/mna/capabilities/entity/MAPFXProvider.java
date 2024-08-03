package com.mna.capabilities.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class MAPFXProvider implements ICapabilitySerializable<Tag> {

    public static final Capability<MAPFX> MAPFX = CapabilityManager.get(new CapabilityToken<MAPFX>() {
    });

    private final LazyOptional<MAPFX> holder = LazyOptional.of(MAPFX::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return MAPFX.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLong("flags", this.holder.orElse(null).getFlags());
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag cnbt && cnbt.contains("flags")) {
            this.holder.orElse(null).setFlags(cnbt.getLong("flags"));
        }
    }
}