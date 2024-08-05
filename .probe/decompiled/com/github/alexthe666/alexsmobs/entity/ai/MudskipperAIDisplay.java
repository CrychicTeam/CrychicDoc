package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMudskipper;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MudskipperAIDisplay extends Goal {

    private static final TargetingConditions JOSTLE_PREDICATE = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight();

    protected EntityMudskipper partner;

    private EntityMudskipper mudskipper;

    private Level world;

    private float angle;

    private Vec3 center = null;

    public MudskipperAIDisplay(EntityMudskipper mudskipper) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.mudskipper = mudskipper;
        this.world = mudskipper.m_9236_();
    }

    @Override
    public boolean canUse() {
        if (!this.mudskipper.isDisplaying() && !this.mudskipper.shouldFollow() && !this.mudskipper.isOrderedToSit() && !this.mudskipper.m_27593_() && !this.mudskipper.m_20160_() && !this.mudskipper.m_20159_() && !this.mudskipper.m_6162_() && this.mudskipper.m_5448_() == null && !this.mudskipper.m_20096_() && this.mudskipper.displayCooldown <= 0) {
            if (this.mudskipper.instantlyTriggerDisplayAI || this.mudskipper.m_217043_().nextInt(30) == 0) {
                this.mudskipper.instantlyTriggerDisplayAI = false;
                if (this.mudskipper.getDisplayingPartner() instanceof EntityMudskipper) {
                    this.partner = (EntityMudskipper) this.mudskipper.getDisplayingPartner();
                    return this.partner.displayCooldown == 0;
                }
                EntityMudskipper possiblePartner = this.getNearbyMudskipper();
                if (possiblePartner != null) {
                    this.mudskipper.setDisplayingPartner(possiblePartner);
                    possiblePartner.setDisplayingPartner(this.mudskipper);
                    this.partner = possiblePartner;
                    this.partner.instantlyTriggerDisplayAI = true;
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mudskipper.displayTimer = 0;
        this.angle = 0.0F;
        this.setDisplayDirection(this.mudskipper.m_217043_().nextBoolean());
    }

    public void setDisplayDirection(boolean dir) {
        this.mudskipper.displayDirection = dir;
        this.partner.displayDirection = !dir;
    }

    @Override
    public void stop() {
        this.center = null;
        this.mudskipper.setDisplaying(false);
        this.mudskipper.setDisplayingPartner(null);
        this.mudskipper.displayTimer = 0;
        this.angle = 0.0F;
        this.mudskipper.m_21573_().stop();
        if (this.partner != null) {
            this.partner.setDisplaying(false);
            this.partner.setDisplayingPartner(null);
            this.partner.displayTimer = 0;
            this.partner = null;
        }
    }

    @Override
    public void tick() {
        if (this.partner != null) {
            if (this.center == null || this.mudskipper.m_217043_().nextInt(100) == 0) {
                this.center = new Vec3((this.mudskipper.m_20185_() + this.partner.m_20185_()) / 2.0, (this.mudskipper.m_20186_() + this.partner.m_20186_()) / 2.0, (this.mudskipper.m_20189_() + this.partner.m_20189_()) / 2.0);
            }
            this.mudskipper.setDisplaying(true);
            float x = (float) (this.mudskipper.m_20185_() - this.partner.m_20185_());
            float y = Math.abs((float) (this.mudskipper.m_20186_() - this.partner.m_20186_()));
            float z = (float) (this.mudskipper.m_20189_() - this.partner.m_20189_());
            double distXZ = Math.sqrt((double) (x * x + z * z));
            if (distXZ > 3.0) {
                this.mudskipper.m_21573_().moveTo(this.partner, 1.0);
            } else {
                float speed = this.mudskipper.m_217043_().nextFloat() * 0.5F + 0.8F;
                if (this.mudskipper.displayDirection) {
                    if (this.angle < 180.0F) {
                        this.angle += 10.0F;
                    } else {
                        this.mudskipper.displayDirection = false;
                    }
                }
                if (!this.mudskipper.displayDirection) {
                    if (this.angle > -180.0F) {
                        this.angle -= 10.0F;
                    } else {
                        this.mudskipper.displayDirection = true;
                    }
                }
                if (distXZ < 0.8F && this.mudskipper.m_20096_() && this.partner.m_20096_()) {
                    this.mudskipper.m_21391_(this.partner, 360.0F, 360.0F);
                    this.setDisplayDirection(!this.mudskipper.displayDirection);
                    this.mudskipper.openMouth(10 + this.mudskipper.m_217043_().nextInt(20));
                    this.partner.m_20256_(this.partner.m_20184_().add((double) (0.2F * this.mudskipper.m_217043_().nextFloat()), 0.35F, (double) (0.2F * this.mudskipper.m_217043_().nextFloat())));
                }
                Vec3 circle = this.getCirclingPosOf(this.center, (double) (1.5F + this.mudskipper.m_217043_().nextFloat()));
                Vec3 dirVec = circle.subtract(this.mudskipper.m_20182_());
                float headAngle = -((float) (Mth.atan2(dirVec.x, dirVec.z) * 180.0F / (float) Math.PI));
                this.mudskipper.m_21573_().moveTo(circle.x, circle.y, circle.z, (double) speed);
                this.mudskipper.m_146922_(headAngle);
                this.mudskipper.f_20885_ = headAngle;
                this.mudskipper.f_20883_ = headAngle;
                this.mudskipper.nextDisplayAngleFromServer = this.angle;
                this.mudskipper.displayTimer++;
                this.partner.displayTimer++;
                if (this.mudskipper.displayTimer > 400 || y > 2.0F) {
                    this.mudskipper.m_21573_().stop();
                    this.partner.m_21573_().stop();
                    this.mudskipper.f_19812_ = true;
                    this.mudskipper.displayTimer = 0;
                    this.partner.displayTimer = 0;
                    this.mudskipper.displayCooldown = 200 + this.mudskipper.m_217043_().nextInt(200);
                    this.partner.displayTimer = 0;
                    this.partner.displayCooldown = 200 + this.partner.m_217043_().nextInt(200);
                    this.stop();
                }
            }
        }
    }

    public Vec3 getCirclingPosOf(Vec3 center, double circleDistance) {
        float cir = (float) (Math.PI / 180.0) * this.angle;
        double extraX = circleDistance * (double) Mth.sin(cir);
        double extraZ = circleDistance * (double) Mth.cos(cir);
        return center.add(extraX, 0.0, extraZ);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mudskipper.m_6162_() && !this.mudskipper.shouldFollow() && !this.mudskipper.isOrderedToSit() && !this.mudskipper.m_27593_() && !this.mudskipper.m_20160_() && this.mudskipper.m_5448_() == null && this.partner != null && this.partner.m_6084_() && this.mudskipper.displayCooldown == 0 && this.partner.displayCooldown == 0;
    }

    @Nullable
    private EntityMudskipper getNearbyMudskipper() {
        List<EntityMudskipper> skippers = this.world.m_45971_(EntityMudskipper.class, JOSTLE_PREDICATE, this.mudskipper, this.mudskipper.m_20191_().inflate(16.0));
        double lvt_2_1_ = Double.MAX_VALUE;
        EntityMudskipper lvt_4_1_ = null;
        for (EntityMudskipper lvt_6_1_ : skippers) {
            if (this.mudskipper.canDisplayWith(lvt_6_1_) && this.mudskipper.m_20280_(lvt_6_1_) < lvt_2_1_) {
                lvt_4_1_ = lvt_6_1_;
                lvt_2_1_ = this.mudskipper.m_20280_(lvt_6_1_);
            }
        }
        return lvt_4_1_;
    }
}