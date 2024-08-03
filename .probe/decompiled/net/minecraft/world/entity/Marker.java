package net.minecraft.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

public class Marker extends Entity {

    private static final String DATA_TAG = "data";

    private CompoundTag data = new CompoundTag();

    public Marker(EntityType<?> entityType0, Level level1) {
        super(entityType0, level1);
        this.f_19794_ = true;
    }

    @Override
    public void tick() {
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.data = compoundTag0.getCompound("data");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.put("data", this.data.copy());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new IllegalStateException("Markers should never be sent");
    }

    @Override
    protected boolean canAddPassenger(Entity entity0) {
        return false;
    }

    @Override
    protected boolean couldAcceptPassenger() {
        return false;
    }

    @Override
    protected void addPassenger(Entity entity0) {
        throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }
}