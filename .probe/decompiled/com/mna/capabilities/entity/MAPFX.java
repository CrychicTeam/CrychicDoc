package com.mna.capabilities.entity;

import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.world.entity.LivingEntity;

public class MAPFX {

    private long flags;

    private boolean requestedSync = false;

    private boolean needsSync = false;

    public long getFlags() {
        return this.flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public boolean getFlag(LivingEntity e, MAPFX.Flag f) {
        long mask = (long) (1 << f.ordinal());
        return (this.flags & mask) != 0L;
    }

    public void setFlag(LivingEntity e, MAPFX.Flag f, boolean value) {
        long mask = (long) (1 << f.ordinal());
        if (value) {
            this.flags |= mask;
        } else {
            this.flags &= ~mask;
        }
        this.needsSync = true;
    }

    public void sync(LivingEntity entity) {
        if (this.needsSync && !entity.m_9236_().isClientSide()) {
            this.needsSync = false;
            ServerMessageDispatcher.sendMAPFXMessage(entity);
        }
    }

    public void requestSync(LivingEntity entity) {
        if (!this.requestedSync) {
            this.requestedSync = ClientMessageDispatcher.sendMAPFXSyncRequestMessage(entity);
        }
    }

    public static enum Flag {

        CANCEL_RENDER,
        MIST_FORM,
        LIVING_BOMB,
        FIRE_SHIELD,
        MANA_SHIELD,
        BRIARTHORN_BARRIER,
        CIRCLE_OF_POWER,
        WIND_WALL,
        AURA_OF_FROST,
        SOAKED
    }
}