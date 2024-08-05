package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class AnimalAIWanderRanged extends RandomStrollGoal {

    protected final float probability;

    protected final int xzRange;

    protected final int yRange;

    public AnimalAIWanderRanged(PathfinderMob creature, int chance, double speedIn, int xzRange, int yRange) {
        this(creature, chance, speedIn, 0.001F, xzRange, yRange);
    }

    @Override
    public boolean canUse() {
        if (this.f_25725_.m_20160_() && !(this.f_25725_ instanceof EntityKangaroo)) {
            return false;
        } else {
            if (!this.f_25731_) {
                if (this.f_25725_.m_21216_() >= 100) {
                    return false;
                }
                if (this.f_25725_.m_217043_().nextInt(this.f_25730_) != 0) {
                    return false;
                }
            }
            Vec3 lvt_1_1_ = this.getPosition();
            if (lvt_1_1_ == null) {
                return false;
            } else {
                this.f_25726_ = lvt_1_1_.x;
                this.f_25727_ = lvt_1_1_.y;
                this.f_25728_ = lvt_1_1_.z;
                this.f_25731_ = false;
                return true;
            }
        }
    }

    public AnimalAIWanderRanged(PathfinderMob creature, int chance, double speedIn, float probabilityIn, int xzRange, int yRange) {
        super(creature, speedIn, chance);
        this.probability = probabilityIn;
        this.xzRange = xzRange;
        this.yRange = yRange;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.f_25725_.m_20072_()) {
            Vec3 vector3d = LandRandomPos.getPos(this.f_25725_, this.xzRange, this.yRange);
            return vector3d == null ? super.getPosition() : vector3d;
        } else {
            return this.f_25725_.m_217043_().nextFloat() >= this.probability ? LandRandomPos.getPos(this.f_25725_, this.xzRange, this.yRange) : super.getPosition();
        }
    }
}