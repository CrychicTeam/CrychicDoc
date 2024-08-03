package com.mna.capabilities.particles;

import com.mna.network.ClientMessageDispatcher;
import com.mna.particles.emitter.EmitterData;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleAura implements Consumer<FriendlyByteBuf> {

    private EmitterData _emitter;

    private boolean isDirty = true;

    private boolean hasRequested = false;

    private boolean hasLoaded = false;

    public ParticleAura() {
        this._emitter = new EmitterData();
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setDirty() {
        this.isDirty = true;
    }

    public void clearDirty() {
        this.isDirty = false;
    }

    public void requestIfNeeded(Player forPlayer) {
        if (!this.hasRequested && forPlayer.m_9236_().isClientSide()) {
            this.hasRequested = true;
            ClientMessageDispatcher.sendAuraXSyncRequestMessage(forPlayer);
        }
    }

    public CompoundTag save() {
        return this._emitter.getTag();
    }

    public void load(CompoundTag tag) {
        this._emitter = EmitterData.fromTag(tag);
        this.hasLoaded = true;
    }

    public void copyFrom(ParticleAura other) {
        this._emitter = EmitterData.fromTag(other._emitter.getTag());
        this.setDirty();
    }

    public void spawn(Level world, Vec3 origin, Vec3 forward) {
        if (world.isClientSide && this.hasLoaded) {
            this._emitter.spawn(world, origin, forward);
        }
    }

    public void accept(FriendlyByteBuf t) {
        t.writeNbt(this.save());
        t.writeLong(BlockPos.ZERO.asLong());
    }

    public boolean showInFirstPerson() {
        return this._emitter.showInFirstPerson;
    }
}