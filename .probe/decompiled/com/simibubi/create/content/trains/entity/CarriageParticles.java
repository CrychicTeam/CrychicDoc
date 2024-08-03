package com.simibubi.create.content.trains.entity;

import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CarriageParticles {

    CarriageContraptionEntity entity;

    boolean arrived;

    int depressurise;

    double prevMotion;

    LerpedFloat brakes;

    public CarriageParticles(CarriageContraptionEntity entity) {
        this.entity = entity;
        this.arrived = true;
        this.depressurise = 0;
        this.prevMotion = 0.0;
        this.brakes = LerpedFloat.linear();
    }

    public void tick(Carriage.DimensionalCarriageEntity dce) {
        Minecraft mc = Minecraft.getInstance();
        Entity camEntity = mc.cameraEntity;
        if (camEntity != null) {
            Vec3 leadingAnchor = dce.leadingAnchor();
            if (leadingAnchor != null && leadingAnchor.closerThan(camEntity.position(), 64.0)) {
                RandomSource r = this.entity.m_9236_().random;
                Vec3 contraptionMotion = this.entity.m_20182_().subtract(this.entity.getPrevPositionVec());
                double length = contraptionMotion.length();
                if (this.arrived && length > 0.01F) {
                    this.arrived = false;
                }
                this.arrived = this.arrived | this.entity.isStalled();
                boolean stopped = length < 0.002F;
                if (stopped) {
                    if (!this.arrived) {
                        this.arrived = true;
                        this.depressurise = (int) (20.0F * this.entity.getCarriage().train.accumulatedSteamRelease / 10.0F);
                    }
                } else {
                    this.depressurise = 0;
                }
                if (this.depressurise > 0) {
                    this.depressurise--;
                }
                this.brakes.chase(this.prevMotion > length + length / 512.0 ? 1.0 : 0.0, 0.25, LerpedFloat.Chaser.exp(0.625));
                this.brakes.tickChaser();
                this.prevMotion = length;
                Level level = this.entity.m_9236_();
                Vec3 position = this.entity.m_20318_(0.0F);
                float viewYRot = this.entity.m_5675_(0.0F);
                float viewXRot = this.entity.m_5686_(0.0F);
                int bogeySpacing = this.entity.getCarriage().bogeySpacing;
                for (CarriageBogey bogey : this.entity.getCarriage().bogeys) {
                    if (bogey != null) {
                        boolean spark = this.depressurise == 0 || this.depressurise > 10;
                        float cutoff = length < 0.125 ? 0.0F : 0.125F;
                        if (length > 0.16666667F) {
                            cutoff = Math.max(cutoff, this.brakes.getValue() * 1.15F);
                        }
                        for (int j : Iterate.positiveAndNegative) {
                            if (!(r.nextFloat() > cutoff) || !spark && r.nextInt(4) != 0) {
                                for (int i : Iterate.positiveAndNegative) {
                                    if (!(r.nextFloat() > cutoff) || !spark && r.nextInt(4) != 0) {
                                        Vec3 v = Vec3.ZERO.add((double) j * 1.15, spark ? -0.6F : 0.32, (double) i);
                                        Vec3 m = Vec3.ZERO.add((double) j * (spark ? 0.5 : 0.25), spark ? 0.49 : -0.29, 0.0);
                                        m = VecHelper.rotate(m, (double) bogey.pitch.getValue(0.0F), Direction.Axis.X);
                                        m = VecHelper.rotate(m, (double) bogey.yaw.getValue(0.0F), Direction.Axis.Y);
                                        v = VecHelper.rotate(v, (double) bogey.pitch.getValue(0.0F), Direction.Axis.X);
                                        v = VecHelper.rotate(v, (double) bogey.yaw.getValue(0.0F), Direction.Axis.Y);
                                        v = VecHelper.rotate(v, (double) (-viewYRot - 90.0F), Direction.Axis.Y);
                                        v = VecHelper.rotate(v, (double) viewXRot, Direction.Axis.X);
                                        v = VecHelper.rotate(v, -180.0, Direction.Axis.Y);
                                        v = v.add(0.0, 0.0, bogey.isLeading ? 0.0 : (double) (-bogeySpacing));
                                        v = VecHelper.rotate(v, 180.0, Direction.Axis.Y);
                                        v = VecHelper.rotate(v, (double) (-viewXRot), Direction.Axis.X);
                                        v = VecHelper.rotate(v, (double) (viewYRot + 90.0F), Direction.Axis.Y);
                                        v = v.add(position);
                                        m = m.add(contraptionMotion.scale(0.75));
                                        level.addParticle(spark ? bogey.getStyle().contactParticle : bogey.getStyle().smokeParticle, v.x, v.y, v.z, m.x, m.y, m.z);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}