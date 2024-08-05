package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class AnimalSwimMoveControllerSink extends MoveControl {

    private final PathfinderMob entity;

    private final float speedMulti;

    private float ySpeedMod = 1.0F;

    private float yawLimit = 10.0F;

    public AnimalSwimMoveControllerSink(PathfinderMob entity, float speedMulti, float ySpeedMod) {
        super(entity);
        this.entity = entity;
        this.speedMulti = speedMulti;
        this.ySpeedMod = ySpeedMod;
    }

    public AnimalSwimMoveControllerSink(PathfinderMob entity, float speedMulti, float ySpeedMod, float yawLimit) {
        super(entity);
        this.entity = entity;
        this.speedMulti = speedMulti;
        this.ySpeedMod = ySpeedMod;
        this.yawLimit = yawLimit;
    }

    @Override
    public void tick() {
        if (this.entity instanceof ISemiAquatic && ((ISemiAquatic) this.entity).shouldStopMoving()) {
            this.entity.m_7910_(0.0F);
        } else {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.entity.m_21573_().isDone()) {
                double lvt_1_1_ = this.f_24975_ - this.entity.m_20185_();
                double lvt_3_1_ = this.f_24976_ - this.entity.m_20186_();
                double lvt_5_1_ = this.f_24977_ - this.entity.m_20189_();
                double lvt_7_1_ = lvt_1_1_ * lvt_1_1_ + lvt_3_1_ * lvt_3_1_ + lvt_5_1_ * lvt_5_1_;
                if (lvt_7_1_ < 2.5000003E-7F) {
                    this.f_24974_.setZza(0.0F);
                } else {
                    float lvt_9_1_ = (float) (Mth.atan2(lvt_5_1_, lvt_1_1_) * 180.0F / (float) Math.PI) - 90.0F;
                    this.entity.m_146922_(this.m_24991_(this.entity.m_146908_(), lvt_9_1_, this.yawLimit));
                    this.entity.f_20883_ = this.entity.m_146908_();
                    this.entity.f_20885_ = this.entity.m_146908_();
                    float lvt_10_1_ = (float) (this.f_24978_ * (double) this.speedMulti * 3.0 * this.entity.m_21133_(Attributes.MOVEMENT_SPEED));
                    if (this.entity.m_20069_()) {
                        if (lvt_3_1_ > 0.0 && this.entity.f_19862_) {
                            this.entity.m_20256_(this.entity.m_20184_().add(0.0, 0.08F, 0.0));
                        } else {
                            this.entity.m_20256_(this.entity.m_20184_().add(0.0, (double) this.entity.m_6113_() * lvt_3_1_ * 0.6 * (double) this.ySpeedMod, 0.0));
                        }
                        this.entity.m_7910_(lvt_10_1_ * 0.02F);
                        float lvt_11_1_ = -((float) (Mth.atan2(lvt_3_1_, (double) Mth.sqrt((float) (lvt_1_1_ * lvt_1_1_ + lvt_5_1_ * lvt_5_1_))) * 180.0F / (float) Math.PI));
                        lvt_11_1_ = Mth.clamp(Mth.wrapDegrees(lvt_11_1_), -85.0F, 85.0F);
                        this.entity.m_146926_(this.m_24991_(this.entity.m_146909_(), lvt_11_1_, 5.0F));
                        float lvt_12_1_ = Mth.cos(this.entity.m_146909_() * (float) (Math.PI / 180.0));
                        float lvt_13_1_ = Mth.sin(this.entity.m_146909_() * (float) (Math.PI / 180.0));
                        this.entity.f_20902_ = lvt_12_1_ * lvt_10_1_;
                        this.entity.f_20901_ = -lvt_13_1_ * lvt_10_1_;
                    } else {
                        this.entity.m_7910_(lvt_10_1_ * 0.1F);
                    }
                }
            } else {
                if (this.entity instanceof EntityMimicOctopus && !this.entity.m_20096_()) {
                    this.entity.m_20256_(this.entity.m_20184_().add(0.0, -0.02, 0.0));
                }
                this.entity.m_7910_(0.0F);
                this.entity.m_21570_(0.0F);
                this.entity.m_21567_(0.0F);
                this.entity.m_21564_(0.0F);
            }
        }
    }
}