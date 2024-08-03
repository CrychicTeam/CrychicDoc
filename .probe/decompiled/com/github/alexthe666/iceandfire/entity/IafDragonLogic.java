package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.message.MessageSpawnParticleAt;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class IafDragonLogic {

    long ticksAfterClearingTarget;

    private final EntityDragonBase dragon;

    public IafDragonLogic(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    public void updateDragonServer() {
        this.dragon.updateRider();
        this.dragon.updatePitch(this.dragon.f_19855_ - this.dragon.m_20186_());
        if (this.dragon.lookingForRoostAIFlag && this.dragon.m_21188_() != null || this.dragon.isSleeping()) {
            this.dragon.lookingForRoostAIFlag = false;
        }
        if (IafConfig.doDragonsSleep && !this.dragon.isSleeping() && !this.dragon.isTimeToWake() && this.dragon.m_20197_().isEmpty() && this.dragon.getCommand() != 2) {
            if (this.dragon.hasHomePosition && this.dragon.getRestrictCenter() != null && DragonUtils.isInHomeDimension(this.dragon) && this.dragon.getDistanceSquared(Vec3.atCenterOf(this.dragon.getRestrictCenter())) > this.dragon.m_20205_() * 10.0F && this.dragon.getCommand() != 2 && this.dragon.getCommand() != 1) {
                this.dragon.lookingForRoostAIFlag = true;
            } else {
                this.dragon.lookingForRoostAIFlag = false;
                if (this.dragon.m_9236_().getGameTime() - this.ticksAfterClearingTarget >= 20L && !this.dragon.isInWater() && this.dragon.m_20096_() && !this.dragon.isFlying() && !this.dragon.isHovering() && this.dragon.m_5448_() == null) {
                    this.dragon.setInSittingPose(true);
                }
            }
        } else {
            this.dragon.lookingForRoostAIFlag = false;
        }
        if (this.dragon.isSleeping() && (this.dragon.isFlying() || this.dragon.isHovering() || this.dragon.isInWater() || this.dragon.m_9236_().m_46861_(this.dragon.m_20183_()) && this.dragon.isTimeToWake() && !this.dragon.m_21824_() || this.dragon.isTimeToWake() && this.dragon.m_21824_() || this.dragon.m_5448_() != null || !this.dragon.m_20197_().isEmpty())) {
            this.dragon.setInSittingPose(false);
        }
        if (this.dragon.isOrderedToSit() && this.dragon.getControllingPassenger() != null) {
            this.dragon.setOrderedToSit(false);
        }
        if (this.dragon.blockBreakCounter <= 0) {
            this.dragon.blockBreakCounter = IafConfig.dragonBreakBlockCooldown;
        }
        this.dragon.updateBurnTarget();
        if (this.dragon.isOrderedToSit()) {
            if (this.dragon.getCommand() != 1 || this.dragon.getControllingPassenger() != null) {
                this.dragon.setOrderedToSit(false);
            }
        } else if (this.dragon.getCommand() == 1 && this.dragon.getControllingPassenger() == null) {
            this.dragon.setOrderedToSit(true);
        }
        if (this.dragon.isOrderedToSit()) {
            this.dragon.m_21573_().stop();
        }
        if (this.dragon.m_27593_()) {
            this.dragon.m_9236_().broadcastEntityEvent(this.dragon, (byte) 18);
        }
        if (new Vec3i((int) this.dragon.f_19854_, (int) this.dragon.f_19855_, (int) this.dragon.f_19856_).distSqr(this.dragon.m_20183_()) <= 0.5) {
            this.dragon.ticksStill++;
        } else {
            this.dragon.ticksStill = 0;
        }
        if (this.dragon.getControllingPassenger() == null && this.dragon.isTackling() && !this.dragon.isFlying() && this.dragon.m_20096_()) {
            this.dragon.tacklingTicks++;
            if (this.dragon.tacklingTicks == 40) {
                this.dragon.tacklingTicks = 0;
                this.dragon.setTackling(false);
                this.dragon.setFlying(false);
            }
        }
        if (this.dragon.m_217043_().nextInt(500) == 0 && !this.dragon.isModelDead() && !this.dragon.isSleeping()) {
            this.dragon.roar();
        }
        if (this.dragon.isFlying() && this.dragon.m_5448_() != null) {
            if (this.dragon.airAttack == IafDragonAttacks.Air.TACKLE) {
                this.dragon.setTackling(true);
            }
            if (this.dragon.isTackling() && this.dragon.m_20191_().expandTowards(2.0, 2.0, 2.0).intersects(this.dragon.m_5448_().m_20191_())) {
                this.dragon.usingGroundAttack = true;
                this.dragon.randomizeAttacks();
                this.attackTarget(this.dragon.m_5448_(), null, (float) (this.dragon.getDragonStage() * 3));
                this.dragon.setFlying(false);
                this.dragon.setHovering(false);
            }
        }
        if (this.dragon.getControllingPassenger() == null && this.dragon.isTackling() && (this.dragon.m_5448_() == null || this.dragon.airAttack != IafDragonAttacks.Air.TACKLE)) {
            this.dragon.setTackling(false);
            this.dragon.randomizeAttacks();
        }
        if (this.dragon.m_20159_()) {
            this.dragon.setFlying(false);
            this.dragon.setHovering(false);
            this.dragon.setInSittingPose(false);
        }
        if (this.dragon.isFlying() && this.dragon.f_19797_ % 40 == 0 || this.dragon.isFlying() && this.dragon.isSleeping()) {
            this.dragon.setInSittingPose(false);
        }
        if (!this.dragon.canMove()) {
            if (this.dragon.m_5448_() != null) {
                this.dragon.setTarget(null);
                this.ticksAfterClearingTarget = this.dragon.m_9236_().getGameTime();
            }
            this.dragon.m_21573_().stop();
        }
        if (!this.dragon.m_21824_()) {
            this.dragon.updateCheckPlayer();
        }
        if (this.dragon.isModelDead() && (this.dragon.isFlying() || this.dragon.isHovering())) {
            this.dragon.setFlying(false);
            this.dragon.setHovering(false);
        }
        if (this.dragon.getControllingPassenger() == null) {
            if ((this.dragon.useFlyingPathFinder() || this.dragon.isHovering()) && this.dragon.navigatorType != 1) {
                this.dragon.switchNavigator(1);
            }
        } else if ((this.dragon.useFlyingPathFinder() || this.dragon.isHovering()) && this.dragon.navigatorType != 2) {
            this.dragon.switchNavigator(2);
        }
        if (this.dragon.getControllingPassenger() == null && !this.dragon.useFlyingPathFinder() && !this.dragon.isHovering() && this.dragon.navigatorType != 0) {
            this.dragon.switchNavigator(0);
        }
        if (this.dragon.getControllingPassenger() == null && !this.dragon.isOverAir() && this.dragon.doesWantToLand() && (this.dragon.isFlying() || this.dragon.isHovering()) && !this.dragon.isInWater()) {
            this.dragon.setFlying(false);
            this.dragon.setHovering(false);
        }
        if (this.dragon.isHovering()) {
            if (this.dragon.isFlying() && this.dragon.flyTicks > 40) {
                this.dragon.setHovering(false);
                this.dragon.setFlying(true);
            }
            this.dragon.hoverTicks++;
        } else {
            this.dragon.hoverTicks = 0;
        }
        if (this.dragon.isHovering() && !this.dragon.isFlying()) {
            if (this.dragon.isSleeping()) {
                this.dragon.setHovering(false);
            }
            if (this.dragon.getControllingPassenger() == null && this.dragon.doesWantToLand() && !this.dragon.m_20096_() && !this.dragon.isInWater()) {
                this.dragon.m_20256_(this.dragon.m_20184_().add(0.0, -0.25, 0.0));
            } else {
                if ((this.dragon.getControllingPassenger() == null || this.dragon.getControllingPassenger() instanceof EntityDreadQueen) && !this.dragon.isBeyondHeight()) {
                    double up = this.dragon.isInWater() ? 0.12 : 0.08;
                    this.dragon.m_20256_(this.dragon.m_20184_().add(0.0, up, 0.0));
                }
                if (this.dragon.hoverTicks > 40) {
                    this.dragon.setHovering(false);
                    this.dragon.setFlying(true);
                    this.dragon.flyHovering = 0;
                    this.dragon.hoverTicks = 0;
                    this.dragon.flyTicks = 0;
                }
            }
        }
        if (this.dragon.isSleeping()) {
            this.dragon.m_21573_().stop();
        }
        if ((this.dragon.m_20096_() || this.dragon.isInWater()) && this.dragon.flyTicks != 0) {
            this.dragon.flyTicks = 0;
        }
        if (this.dragon.isAllowedToTriggerFlight() && this.dragon.isFlying() && this.dragon.doesWantToLand()) {
            this.dragon.setFlying(false);
            this.dragon.setHovering(this.dragon.isOverAir());
            if (!this.dragon.isOverAir()) {
                this.dragon.flyTicks = 0;
                this.dragon.setFlying(false);
            }
        }
        if (this.dragon.isFlying()) {
            this.dragon.flyTicks++;
        }
        if ((this.dragon.isHovering() || this.dragon.isFlying()) && this.dragon.isSleeping()) {
            this.dragon.setFlying(false);
            this.dragon.setHovering(false);
        }
        if (!this.dragon.isFlying() && !this.dragon.isHovering() && (this.dragon.isAllowedToTriggerFlight() || this.dragon.m_20186_() < (double) this.dragon.m_9236_().m_141937_()) && (this.dragon.m_217043_().nextInt(this.dragon.getFlightChancePerTick()) == 0 || this.dragon.m_20186_() < (double) this.dragon.m_9236_().m_141937_() || this.dragon.m_5448_() != null && Math.abs(this.dragon.m_5448_().m_20186_() - this.dragon.m_20186_()) > 5.0 || this.dragon.isInWater())) {
            this.dragon.setHovering(true);
            this.dragon.setInSittingPose(false);
            this.dragon.setOrderedToSit(false);
            this.dragon.flyHovering = 0;
            this.dragon.hoverTicks = 0;
            this.dragon.flyTicks = 0;
        }
        if (this.dragon.m_5448_() != null && !DragonUtils.isAlive(this.dragon.m_5448_())) {
            this.dragon.setTarget(null);
            this.ticksAfterClearingTarget = this.dragon.m_9236_().getGameTime();
        }
        if (!this.dragon.isAgingDisabled()) {
            this.dragon.setAgeInTicks(this.dragon.getAgeInTicks() + 1);
            if (this.dragon.getAgeInTicks() % 24000 == 0) {
                this.dragon.updateAttributes();
                this.dragon.growDragon(0);
            }
        }
        if (this.dragon.f_19797_ % IafConfig.dragonHungerTickRate == 0 && IafConfig.dragonHungerTickRate > 0 && this.dragon.getHunger() > 0) {
            this.dragon.setHunger(this.dragon.getHunger() - 1);
        }
        if (this.dragon.groundAttack == IafDragonAttacks.Ground.FIRE && this.dragon.getDragonStage() < 2) {
            this.dragon.usingGroundAttack = true;
            this.dragon.randomizeAttacks();
            this.dragon.playSound(this.dragon.getBabyFireSound(), 1.0F, 1.0F);
        }
        if (this.dragon.isBreathingFire()) {
            if (this.dragon.isSleeping() || this.dragon.isModelDead()) {
                this.dragon.setBreathingFire(false);
                this.dragon.randomizeAttacks();
                this.dragon.fireTicks = 0;
            }
            if (this.dragon.burningTarget == null && (this.dragon.fireTicks > this.dragon.getDragonStage() * 25 || this.dragon.m_269323_() != null && this.dragon.m_20197_().contains(this.dragon.m_269323_()) && this.dragon.fireStopTicks <= 0)) {
                this.dragon.setBreathingFire(false);
                this.dragon.randomizeAttacks();
                this.dragon.fireTicks = 0;
            }
            if (this.dragon.fireStopTicks > 0 && this.dragon.m_269323_() != null && this.dragon.m_20197_().contains(this.dragon.m_269323_())) {
                this.dragon.fireStopTicks--;
            }
        }
        if (this.dragon.isFlying()) {
            if (this.dragon.m_5448_() != null && this.dragon.m_20191_().expandTowards(3.0, 3.0, 3.0).intersects(this.dragon.m_5448_().m_20191_())) {
                this.dragon.doHurtTarget(this.dragon.m_5448_());
            }
            if (this.dragon.airAttack == IafDragonAttacks.Air.TACKLE && (this.dragon.f_19862_ || this.dragon.m_20096_())) {
                this.dragon.usingGroundAttack = true;
                if (this.dragon.getControllingPassenger() == null) {
                    this.dragon.setFlying(false);
                    this.dragon.setHovering(false);
                }
            }
            if (this.dragon.usingGroundAttack) {
                this.dragon.airAttack = IafDragonAttacks.Air.TACKLE;
            }
            if (this.dragon.airAttack == IafDragonAttacks.Air.TACKLE && this.dragon.m_5448_() != null && this.dragon.isTargetBlocked(this.dragon.m_5448_().m_20182_())) {
                this.dragon.randomizeAttacks();
            }
        }
    }

    public boolean attackTarget(Entity target, Player ridingPlayer, float damage) {
        return ridingPlayer == null ? target.hurt(target.level().damageSources().mobAttack(this.dragon), damage) : target.hurt(target.level().damageSources().indirectMagic(this.dragon, ridingPlayer), damage);
    }

    public void updateDragonClient() {
        if (!this.dragon.isModelDead()) {
            this.dragon.turn_buffer.calculateChainSwingBuffer(50.0F, 0, 4.0F, this.dragon);
            this.dragon.tail_buffer.calculateChainSwingBuffer(90.0F, 20, 5.0F, this.dragon);
            if (!this.dragon.m_20096_()) {
                this.dragon.roll_buffer.calculateChainFlapBuffer(55.0F, 1, 2.0F, 0.5F, this.dragon);
                this.dragon.pitch_buffer.calculateChainWaveBuffer(90.0F, 10, 1.0F, 0.5F, this.dragon);
                this.dragon.pitch_buffer_body.calculateChainWaveBuffer(80.0F, 10, 1.0F, 0.5F, this.dragon);
            }
        }
        if (this.dragon.walkCycle < 39) {
            this.dragon.walkCycle++;
        } else {
            this.dragon.walkCycle = 0;
        }
        if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST && (this.dragon.getAnimationTick() == 17 || this.dragon.getAnimationTick() == 22 || this.dragon.getAnimationTick() == 28)) {
            this.dragon.spawnGroundEffects();
        }
        this.dragon.legSolver.update(this.dragon, this.dragon.getRenderSize() / 3.0F);
        if (this.dragon.flightCycle == 11) {
            this.dragon.spawnGroundEffects();
        }
        if (this.dragon.isModelDead() && this.dragon.flightCycle != 0) {
            this.dragon.flightCycle = 0;
        }
    }

    public void updateDragonCommon() {
        if (this.dragon.isBreathingFire()) {
            this.dragon.fireTicks++;
            if (this.dragon.burnProgress < 40) {
                this.dragon.burnProgress++;
            }
        } else {
            this.dragon.burnProgress = 0;
        }
        if (this.dragon.flightCycle == 2 && !this.dragon.isDiving() && (this.dragon.isFlying() || this.dragon.isHovering())) {
            float dragonSoundVolume = (float) IafConfig.dragonFlapNoiseDistance;
            float dragonSoundPitch = this.dragon.m_6100_();
            this.dragon.playSound(IafSoundRegistry.DRAGON_FLIGHT, dragonSoundVolume, dragonSoundPitch);
        }
        if (this.dragon.flightCycle < 58) {
            this.dragon.flightCycle += 2;
        } else {
            this.dragon.flightCycle = 0;
        }
        boolean flying = this.dragon.isFlying();
        if (flying) {
            if (this.dragon.flyProgress < 20.0F) {
                this.dragon.flyProgress += 0.5F;
            }
        } else if (this.dragon.flyProgress > 0.0F) {
            this.dragon.flyProgress -= 2.0F;
        }
        boolean sleeping = this.dragon.isSleeping() && !this.dragon.isHovering() && !flying;
        if (sleeping) {
            if (this.dragon.sleepProgress < 20.0F) {
                this.dragon.sleepProgress += 0.5F;
            }
        } else if (this.dragon.sleepProgress > 0.0F) {
            this.dragon.sleepProgress -= 0.5F;
        }
        boolean sitting = this.dragon.isOrderedToSit() && !this.dragon.isModelDead() && !sleeping;
        if (sitting) {
            if (this.dragon.sitProgress < 20.0F) {
                this.dragon.sitProgress += 0.5F;
            }
        } else if (this.dragon.sitProgress > 0.0F) {
            this.dragon.sitProgress -= 0.5F;
        }
        boolean fireBreathing = this.dragon.isBreathingFire();
        this.dragon.prevFireBreathProgress = this.dragon.fireBreathProgress;
        if (fireBreathing) {
            if (this.dragon.fireBreathProgress < 10.0F) {
                this.dragon.fireBreathProgress += 0.5F;
            }
        } else if (this.dragon.fireBreathProgress > 0.0F) {
            this.dragon.fireBreathProgress -= 0.5F;
        }
        boolean hovering = this.dragon.isHovering() || this.dragon.isFlying() && this.dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST && this.dragon.m_5448_() != null && this.dragon.m_20270_(this.dragon.m_5448_()) < 17.0F;
        if (hovering) {
            if (this.dragon.hoverProgress < 20.0F) {
                this.dragon.hoverProgress += 0.5F;
            }
        } else if (this.dragon.hoverProgress > 0.0F) {
            this.dragon.hoverProgress -= 2.0F;
        }
        boolean diving = this.dragon.isDiving();
        if (diving) {
            if (this.dragon.diveProgress < 10.0F) {
                this.dragon.diveProgress++;
            }
        } else if (this.dragon.diveProgress > 0.0F) {
            this.dragon.diveProgress -= 2.0F;
        }
        boolean tackling = this.dragon.isTackling() && this.dragon.isOverAir();
        if (tackling) {
            if (this.dragon.tackleProgress < 5.0F) {
                this.dragon.tackleProgress += 0.5F;
            }
        } else if (this.dragon.tackleProgress > 0.0F) {
            this.dragon.tackleProgress--;
        }
        boolean modelDead = this.dragon.isModelDead();
        if (modelDead) {
            if (this.dragon.modelDeadProgress < 20.0F) {
                this.dragon.modelDeadProgress += 0.5F;
            }
        } else if (this.dragon.modelDeadProgress > 0.0F) {
            this.dragon.modelDeadProgress -= 0.5F;
        }
        boolean riding = this.dragon.m_20159_() && this.dragon.m_20202_() != null && this.dragon.m_20202_() instanceof Player;
        if (riding) {
            if (this.dragon.ridingProgress < 20.0F) {
                this.dragon.ridingProgress += 0.5F;
            }
        } else if (this.dragon.ridingProgress > 0.0F) {
            this.dragon.ridingProgress -= 0.5F;
        }
        if (this.dragon.hasHadHornUse) {
            this.dragon.hasHadHornUse = false;
        }
        if (this.dragon.groundAttack == IafDragonAttacks.Ground.FIRE && this.dragon.getDragonStage() < 2) {
            if (this.dragon.m_9236_().isClientSide) {
                this.dragon.spawnBabyParticles();
            }
            this.dragon.randomizeAttacks();
        }
    }

    public void updateDragonAttack() {
        Player ridingPlayer = this.dragon.getRidingPlayer();
        if (this.dragon.isPlayingAttackAnimation() && this.dragon.m_5448_() != null && this.dragon.m_142582_(this.dragon.m_5448_())) {
            LivingEntity target = this.dragon.m_5448_();
            double dist = (double) this.dragon.m_20270_(target);
            if (dist < (double) this.dragon.getRenderSize() * 0.2574 * 2.0 + 2.0) {
                if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_BITE) {
                    if (this.dragon.getAnimationTick() > 15 && this.dragon.getAnimationTick() < 25) {
                        this.attackTarget(target, ridingPlayer, (float) ((int) this.dragon.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                        this.dragon.usingGroundAttack = this.dragon.m_217043_().nextBoolean();
                        this.dragon.randomizeAttacks();
                    }
                } else if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_TAILWHACK) {
                    if (this.dragon.getAnimationTick() > 20 && this.dragon.getAnimationTick() < 30) {
                        this.attackTarget(target, ridingPlayer, (float) ((int) this.dragon.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                        target.knockback((double) ((float) this.dragon.getDragonStage() * 0.6F), (double) Mth.sin(this.dragon.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.dragon.m_146908_() * (float) (Math.PI / 180.0))));
                        this.dragon.usingGroundAttack = this.dragon.m_217043_().nextBoolean();
                        this.dragon.randomizeAttacks();
                    }
                } else if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST && (this.dragon.getAnimationTick() == 15 || this.dragon.getAnimationTick() == 25 || this.dragon.getAnimationTick() == 35)) {
                    this.attackTarget(target, ridingPlayer, (float) ((int) this.dragon.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                    target.knockback((double) ((float) this.dragon.getDragonStage() * 0.6F), (double) Mth.sin(this.dragon.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.dragon.m_146908_() * (float) (Math.PI / 180.0))));
                    this.dragon.usingGroundAttack = this.dragon.m_217043_().nextBoolean();
                    this.dragon.randomizeAttacks();
                }
            }
        }
    }

    public void debug() {
        String side = this.dragon.m_9236_().isClientSide ? "CLIENT" : "SERVER";
        String owner = this.dragon.m_269323_() == null ? "null" : this.dragon.m_269323_().m_7755_().getString();
        String attackTarget = this.dragon.m_5448_() == null ? "null" : this.dragon.m_5448_().m_7755_().getString();
        IceAndFire.LOGGER.warn("DRAGON DEBUG[" + side + "]:\nStage: " + this.dragon.getDragonStage() + "\nAge: " + this.dragon.getAgeInDays() + "\nVariant: " + this.dragon.getVariantName(this.dragon.getVariant()) + "\nOwner: " + owner + "\nAttack Target: " + attackTarget + "\nFlying: " + this.dragon.isFlying() + "\nHovering: " + this.dragon.isHovering() + "\nHovering Time: " + this.dragon.hoverTicks + "\nWidth: " + this.dragon.m_20205_() + "\nMoveHelper: " + this.dragon.m_21566_() + "\nGround Attack: " + this.dragon.groundAttack + "\nAir Attack: " + this.dragon.airAttack + "\nTackling: " + this.dragon.isTackling());
    }

    public void debugPathfinder(Path currentPath) {
        if (IceAndFire.DEBUG) {
            try {
                for (int i = 0; i < currentPath.getNodeCount(); i++) {
                    Node point = currentPath.getNode(i);
                    IceAndFire.sendMSGToAll(new MessageSpawnParticleAt((double) point.x, (double) point.y, (double) point.z, 2));
                }
                if (currentPath.getNextNodePos() != null) {
                    Vec3 point = Vec3.atCenterOf(currentPath.getNextNodePos());
                    IceAndFire.sendMSGToAll(new MessageSpawnParticleAt(point.x, point.y, point.z, 1));
                }
            } catch (Exception var4) {
            }
        }
    }
}