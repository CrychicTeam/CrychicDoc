package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.util.IAFMath;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class IafDragonFlightManager {

    private final EntityDragonBase dragon;

    private Vec3 target;

    private IafDragonAttacks.Air prevAirAttack;

    private Vec3 startAttackVec;

    private Vec3 startPreyVec;

    private boolean hasStartedToScorch = false;

    private LivingEntity prevAttackTarget = null;

    public IafDragonFlightManager(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    public static float approach(float number, float max, float min) {
        min = Math.abs(min);
        return number < max ? Mth.clamp(number + min, number, max) : Mth.clamp(number - min, max, number);
    }

    public static float approachDegrees(float number, float max, float min) {
        float add = Mth.wrapDegrees(max - number);
        return approach(number, number + add, min);
    }

    public static float degreesDifferenceAbs(float f1, float f2) {
        return Math.abs(Mth.wrapDegrees(f2 - f1));
    }

    public void update() {
        if (this.dragon.m_5448_() != null && this.dragon.m_5448_().isAlive()) {
            if (this.dragon instanceof EntityIceDragon && this.dragon.isInWater()) {
                if (this.dragon.m_5448_() == null) {
                    this.dragon.airAttack = IafDragonAttacks.Air.SCORCH_STREAM;
                } else {
                    this.dragon.airAttack = IafDragonAttacks.Air.TACKLE;
                }
            }
            LivingEntity entity = this.dragon.m_5448_();
            if (this.dragon.airAttack == IafDragonAttacks.Air.TACKLE) {
                this.target = new Vec3(entity.m_20185_(), entity.m_20186_() + (double) entity.m_20206_(), entity.m_20189_());
            }
            if (this.dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                float distY = (float) (5 + this.dragon.getDragonStage() * 2);
                int randomDist = 20;
                if (this.dragon.m_20275_(entity.m_20185_(), this.dragon.m_20186_(), entity.m_20189_()) < 16.0 || this.dragon.m_20275_(entity.m_20185_(), this.dragon.m_20186_(), entity.m_20189_()) > 900.0) {
                    this.target = new Vec3(entity.m_20185_() + (double) this.dragon.m_217043_().nextInt(randomDist) - (double) (randomDist / 2), entity.m_20186_() + (double) distY, entity.m_20189_() + (double) this.dragon.m_217043_().nextInt(randomDist) - (double) (randomDist / 2));
                }
                this.dragon.stimulateFire(entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), 3);
            }
            if (this.dragon.airAttack == IafDragonAttacks.Air.SCORCH_STREAM && this.startPreyVec != null && this.startAttackVec != null) {
                float distX = (float) (this.startPreyVec.x - this.startAttackVec.x);
                float distY = (float) (5 + this.dragon.getDragonStage() * 2);
                float distZ = (float) (this.startPreyVec.z - this.startAttackVec.z);
                this.target = new Vec3(entity.m_20185_() + (double) distX, entity.m_20186_() + (double) distY, entity.m_20189_() + (double) distZ);
                this.dragon.tryScorchTarget();
                this.hasStartedToScorch = true;
                if (this.target != null && this.dragon.m_20275_(this.target.x, this.target.y, this.target.z) < 100.0) {
                    this.target = new Vec3(entity.m_20185_() - (double) distX, entity.m_20186_() + (double) distY, entity.m_20189_() - (double) distZ);
                }
            }
        } else if (this.target == null || this.dragon.m_20275_(this.target.x, this.target.y, this.target.z) < 4.0 || !this.dragon.m_9236_().m_46859_(BlockPos.containing(this.target.x, this.target.y, this.target.z)) && (this.dragon.isHovering() || this.dragon.isFlying()) || this.dragon.getCommand() == 2 && this.dragon.shouldTPtoOwner()) {
            BlockPos viewBlock = null;
            if (this.dragon instanceof EntityIceDragon && this.dragon.isInWater()) {
                viewBlock = DragonUtils.getWaterBlockInView(this.dragon);
            }
            if (this.dragon.getCommand() == 2 && this.dragon.useFlyingPathFinder()) {
                if (this.dragon instanceof EntityIceDragon && this.dragon.isInWater()) {
                    viewBlock = DragonUtils.getWaterBlockInViewEscort(this.dragon);
                } else {
                    viewBlock = DragonUtils.getBlockInViewEscort(this.dragon);
                }
            } else if (this.dragon.lookingForRoostAIFlag) {
                BlockPos upPos = this.dragon.getRestrictCenter();
                if (this.dragon.getDistanceSquared(Vec3.atCenterOf(this.dragon.getRestrictCenter())) > 200.0F) {
                    upPos = upPos.above(30);
                }
                viewBlock = upPos;
            } else if (viewBlock == null) {
                viewBlock = DragonUtils.getBlockInView(this.dragon);
                if (this.dragon.isInWater()) {
                    this.dragon.setHovering(true);
                }
            }
            if (viewBlock != null) {
                this.target = new Vec3((double) viewBlock.m_123341_() + 0.5, (double) viewBlock.m_123342_() + 0.5, (double) viewBlock.m_123343_() + 0.5);
            }
        }
        if (this.target != null) {
            if (this.target.y > (double) IafConfig.maxDragonFlight) {
                this.target = new Vec3(this.target.x, (double) IafConfig.maxDragonFlight, this.target.z);
            }
            if (this.target.y >= this.dragon.m_20186_() && !this.dragon.isModelDead()) {
                this.dragon.m_20256_(this.dragon.m_20184_().add(0.0, 0.1, 0.0));
            }
        }
        this.prevAirAttack = this.dragon.airAttack;
    }

    public Vec3 getFlightTarget() {
        return this.target == null ? Vec3.ZERO : this.target;
    }

    public void setFlightTarget(Vec3 target) {
        this.target = target;
    }

    private float getDistanceXZ(double x, double z) {
        float f = (float) (this.dragon.m_20185_() - x);
        float f2 = (float) (this.dragon.m_20189_() - z);
        return f * f + f2 * f2;
    }

    public void onSetAttackTarget(@Nullable LivingEntity LivingEntityIn) {
        if (this.prevAttackTarget != LivingEntityIn) {
            if (LivingEntityIn != null) {
                this.startPreyVec = new Vec3(LivingEntityIn.m_20185_(), LivingEntityIn.m_20186_(), LivingEntityIn.m_20189_());
            } else {
                this.startPreyVec = new Vec3(this.dragon.m_20185_(), this.dragon.m_20186_(), this.dragon.m_20189_());
            }
            this.startAttackVec = new Vec3(this.dragon.m_20185_(), this.dragon.m_20186_(), this.dragon.m_20189_());
        }
        this.prevAttackTarget = LivingEntityIn;
    }

    protected static class FlightMoveHelper extends MoveControl {

        private final EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        @Override
        public void tick() {
            if (this.dragon.f_19862_) {
                this.dragon.m_146922_(this.dragon.m_146908_() + 180.0F);
                this.f_24978_ = 0.1F;
                this.dragon.flightManager.target = null;
            } else {
                float distX = (float) (this.dragon.flightManager.getFlightTarget().x - this.dragon.m_20185_());
                float distY = (float) (this.dragon.flightManager.getFlightTarget().y - this.dragon.m_20186_());
                float distZ = (float) (this.dragon.flightManager.getFlightTarget().z - this.dragon.m_20189_());
                double planeDist = Math.sqrt((double) (distX * distX + distZ * distZ));
                double yDistMod = 1.0 - (double) Mth.abs(distY * 0.7F) / planeDist;
                distX = (float) ((double) distX * yDistMod);
                distZ = (float) ((double) distZ * yDistMod);
                planeDist = (double) Mth.sqrt(distX * distX + distZ * distZ);
                double dist = Math.sqrt((double) (distX * distX + distZ * distZ + distY * distY));
                if (dist > 1.0) {
                    float yawCopy = this.dragon.m_146908_();
                    float atan = (float) Mth.atan2((double) distZ, (double) distX);
                    float yawTurn = Mth.wrapDegrees(this.dragon.m_146908_() + 90.0F);
                    float yawTurnAtan = Mth.wrapDegrees(atan * (180.0F / (float) Math.PI));
                    this.dragon.m_146922_(IafDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, this.dragon.airAttack == IafDragonAttacks.Air.TACKLE && this.dragon.m_5448_() != null ? 10.0F : 4.0F) - 90.0F);
                    this.dragon.f_20883_ = this.dragon.m_146908_();
                    if (IafDragonFlightManager.degreesDifferenceAbs(yawCopy, this.dragon.m_146908_()) < 3.0F) {
                        this.f_24978_ = (double) IafDragonFlightManager.approach((float) this.f_24978_, 1.8F, 0.005F * (1.8F / (float) this.f_24978_));
                    } else {
                        this.f_24978_ = (double) IafDragonFlightManager.approach((float) this.f_24978_, 0.2F, 0.025F);
                        if (dist < 100.0 && this.dragon.m_5448_() != null) {
                            this.f_24978_ *= dist / 100.0;
                        }
                    }
                    float finPitch = (float) (-(Mth.atan2((double) (-distY), planeDist) * 180.0F / (float) Math.PI));
                    this.dragon.m_146926_(finPitch);
                    float yawTurnHead = this.dragon.m_146908_() + 90.0F;
                    this.f_24978_ = this.f_24978_ * this.dragon.getFlightSpeedModifier();
                    this.f_24978_ = this.f_24978_ * Math.min(1.0, dist / 50.0 + 0.3);
                    double x = this.f_24978_ * (double) Mth.cos(yawTurnHead * (float) (Math.PI / 180.0)) * Math.abs((double) distX / dist);
                    double y = this.f_24978_ * (double) Mth.sin(finPitch * (float) (Math.PI / 180.0)) * Math.abs((double) distY / dist);
                    double z = this.f_24978_ * (double) Mth.sin(yawTurnHead * (float) (Math.PI / 180.0)) * Math.abs((double) distZ / dist);
                    double motionCap = 0.2;
                    this.dragon.m_20256_(this.dragon.m_20184_().add(Math.min(x * 0.2, motionCap), Math.min(y * 0.2, motionCap), Math.min(z * 0.2, motionCap)));
                }
            }
        }
    }

    protected static class GroundMoveHelper extends MoveControl {

        public GroundMoveHelper(Mob LivingEntityIn) {
            super(LivingEntityIn);
        }

        public float distance(float rotateAngleFrom, float rotateAngleTo) {
            return (float) IAFMath.atan2_accurate((double) Mth.sin(rotateAngleTo - rotateAngleFrom), (double) Mth.cos(rotateAngleTo - rotateAngleFrom));
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.STRAFE) {
                float f = (float) this.f_24974_.m_21051_(Attributes.MOVEMENT_SPEED).getValue();
                float f1 = (float) this.f_24978_ * f;
                float f2 = this.f_24979_;
                float f3 = this.f_24980_;
                float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }
                f4 = f1 / f4;
                f2 *= f4;
                f3 *= f4;
                float f5 = Mth.sin(this.f_24974_.m_146908_() * (float) (Math.PI / 180.0));
                float f6 = Mth.cos(this.f_24974_.m_146908_() * (float) (Math.PI / 180.0));
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigation pathnavigate = this.f_24974_.getNavigation();
                if (pathnavigate != null) {
                    NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();
                    if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.f_24974_.m_9236_(), Mth.floor(this.f_24974_.m_20185_() + (double) f7), Mth.floor(this.f_24974_.m_20186_()), Mth.floor(this.f_24974_.m_20189_() + (double) f8)) != BlockPathTypes.WALKABLE) {
                        this.f_24979_ = 1.0F;
                        this.f_24980_ = 0.0F;
                        f1 = f;
                    }
                }
                this.f_24974_.setSpeed(f1);
                this.f_24974_.setZza(this.f_24979_);
                this.f_24974_.setXxa(this.f_24980_);
                this.f_24981_ = MoveControl.Operation.WAIT;
            } else if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                EntityDragonBase dragonBase = (EntityDragonBase) this.f_24974_;
                double d0 = this.m_25000_() - this.f_24974_.m_20185_();
                double d1 = this.m_25002_() - this.f_24974_.m_20189_();
                double d2 = this.m_25001_() - this.f_24974_.m_20186_();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < 2.5000003E-7F) {
                    this.f_24974_.setZza(0.0F);
                    return;
                }
                float targetDegree = (float) (Mth.atan2(d1, d0) * (180.0 / Math.PI)) - 90.0F;
                float changeRange = 70.0F;
                if (Math.ceil((double) dragonBase.m_20205_()) > 2.0) {
                    float ageMod = 1.0F - (float) Math.min(dragonBase.getAgeInDays(), 125) / 125.0F;
                    changeRange = 5.0F + ageMod * 10.0F;
                }
                this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), targetDegree, changeRange));
                this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                if (d2 > (double) this.f_24974_.m_274421_() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.f_24974_.m_20205_() / 2.0F)) {
                    this.f_24974_.getJumpControl().jump();
                    this.f_24981_ = MoveControl.Operation.JUMPING;
                }
            } else if (this.f_24981_ == MoveControl.Operation.JUMPING) {
                this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                if (this.f_24974_.m_20096_()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                }
            } else {
                this.f_24974_.setZza(0.0F);
            }
        }
    }

    protected static class PlayerFlightMoveHelper<T extends Mob & IFlyingMount> extends MoveControl {

        private final T dragon;

        public PlayerFlightMoveHelper(T dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void tick() {
            if (this.dragon instanceof EntityDragonBase theDragon && theDragon.getControllingPassenger() != null) {
                return;
            }
            double flySpeed = this.f_24978_ * this.speedMod() * 3.0;
            Vec3 dragonVec = this.dragon.m_20182_();
            Vec3 moveVec = new Vec3(this.f_24975_, this.f_24976_, this.f_24977_);
            Vec3 normalized = moveVec.subtract(dragonVec).normalize();
            double dist = dragonVec.distanceTo(moveVec);
            this.dragon.m_20334_(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
            if (dist > 2.5E-7) {
                float yaw = (float) Math.toDegrees((Math.PI * 2) - Math.atan2(normalized.x, normalized.y));
                this.dragon.m_146922_(this.m_24991_(this.dragon.m_146908_(), yaw, 5.0F));
                this.dragon.setSpeed((float) this.f_24978_);
            }
            this.dragon.m_6478_(MoverType.SELF, this.dragon.m_20184_());
        }

        public double speedMod() {
            return (this.dragon instanceof EntityAmphithere ? 0.6 : 1.25) * IafConfig.dragonFlightSpeedMod * this.dragon.m_21133_(Attributes.MOVEMENT_SPEED);
        }
    }
}