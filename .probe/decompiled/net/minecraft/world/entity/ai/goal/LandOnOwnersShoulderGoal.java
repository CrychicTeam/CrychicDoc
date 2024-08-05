package net.minecraft.world.entity.ai.goal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;

public class LandOnOwnersShoulderGoal extends Goal {

    private final ShoulderRidingEntity entity;

    private ServerPlayer owner;

    private boolean isSittingOnShoulder;

    public LandOnOwnersShoulderGoal(ShoulderRidingEntity shoulderRidingEntity0) {
        this.entity = shoulderRidingEntity0;
    }

    @Override
    public boolean canUse() {
        ServerPlayer $$0 = (ServerPlayer) this.entity.m_269323_();
        boolean $$1 = $$0 != null && !$$0.isSpectator() && !$$0.m_150110_().flying && !$$0.m_20069_() && !$$0.f_146808_;
        return !this.entity.m_21827_() && $$1 && this.entity.canSitOnShoulder();
    }

    @Override
    public boolean isInterruptable() {
        return !this.isSittingOnShoulder;
    }

    @Override
    public void start() {
        this.owner = (ServerPlayer) this.entity.m_269323_();
        this.isSittingOnShoulder = false;
    }

    @Override
    public void tick() {
        if (!this.isSittingOnShoulder && !this.entity.m_21825_() && !this.entity.m_21523_()) {
            if (this.entity.m_20191_().intersects(this.owner.m_20191_())) {
                this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
            }
        }
    }
}