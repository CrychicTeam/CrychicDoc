package net.minecraft.client.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

public class RemotePlayer extends AbstractClientPlayer {

    private Vec3 lerpDeltaMovement = Vec3.ZERO;

    private int lerpDeltaMovementSteps;

    public RemotePlayer(ClientLevel clientLevel0, GameProfile gameProfile1) {
        super(clientLevel0, gameProfile1);
        this.m_274367_(1.0F);
        this.f_19794_ = true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double $$1 = this.m_20191_().getSize() * 10.0;
        if (Double.isNaN($$1)) {
            $$1 = 1.0;
        }
        $$1 *= 64.0 * m_20150_();
        return double0 < $$1 * $$1;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.m_267651_(false);
    }

    @Override
    public void aiStep() {
        if (this.f_20903_ > 0) {
            double $$0 = this.m_20185_() + (this.f_20904_ - this.m_20185_()) / (double) this.f_20903_;
            double $$1 = this.m_20186_() + (this.f_20905_ - this.m_20186_()) / (double) this.f_20903_;
            double $$2 = this.m_20189_() + (this.f_20906_ - this.m_20189_()) / (double) this.f_20903_;
            this.m_146922_(this.m_146908_() + (float) Mth.wrapDegrees(this.f_20907_ - (double) this.m_146908_()) / (float) this.f_20903_);
            this.m_146926_(this.m_146909_() + (float) (this.f_20908_ - (double) this.m_146909_()) / (float) this.f_20903_);
            this.f_20903_--;
            this.m_6034_($$0, $$1, $$2);
            this.m_19915_(this.m_146908_(), this.m_146909_());
        }
        if (this.f_20934_ > 0) {
            this.f_20885_ = this.f_20885_ + (float) (Mth.wrapDegrees(this.f_20933_ - (double) this.f_20885_) / (double) this.f_20934_);
            this.f_20934_--;
        }
        if (this.lerpDeltaMovementSteps > 0) {
            this.m_246865_(new Vec3((this.lerpDeltaMovement.x - this.m_20184_().x) / (double) this.lerpDeltaMovementSteps, (this.lerpDeltaMovement.y - this.m_20184_().y) / (double) this.lerpDeltaMovementSteps, (this.lerpDeltaMovement.z - this.m_20184_().z) / (double) this.lerpDeltaMovementSteps));
            this.lerpDeltaMovementSteps--;
        }
        this.f_36099_ = this.f_36100_;
        this.m_21203_();
        float $$4;
        if (this.m_20096_() && !this.m_21224_()) {
            $$4 = (float) Math.min(0.1, this.m_20184_().horizontalDistance());
        } else {
            $$4 = 0.0F;
        }
        this.f_36100_ = this.f_36100_ + ($$4 - this.f_36100_) * 0.4F;
        this.m_9236_().getProfiler().push("push");
        this.m_6138_();
        this.m_9236_().getProfiler().pop();
    }

    @Override
    public void lerpMotion(double double0, double double1, double double2) {
        this.lerpDeltaMovement = new Vec3(double0, double1, double2);
        this.lerpDeltaMovementSteps = this.m_6095_().updateInterval() + 1;
    }

    @Override
    protected void updatePlayerPose() {
    }

    @Override
    public void sendSystemMessage(Component component0) {
        Minecraft $$1 = Minecraft.getInstance();
        $$1.gui.getChat().addMessage(component0);
    }
}