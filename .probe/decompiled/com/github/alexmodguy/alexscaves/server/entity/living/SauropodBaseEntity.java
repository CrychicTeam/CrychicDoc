package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.item.CrushedBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.FallingTreeBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.KaijuMob;
import com.github.alexmodguy.alexscaves.server.entity.util.LuxtructosaurusLegSolver;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import com.github.alexmodguy.alexscaves.server.entity.util.ShakesScreen;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.ITallWalker;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class SauropodBaseEntity extends DinosaurEntity implements ShakesScreen, IAnimatedEntity, KaijuMob, ITallWalker {

    protected static final EntityDataAccessor<Boolean> WALKING = SynchedEntityData.defineId(SauropodBaseEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Animation ANIMATION_SPEAK = Animation.create(15);

    public static final Animation ANIMATION_ROAR = Animation.create(60);

    public static final Animation ANIMATION_EPIC_DEATH = Animation.create(120);

    public static final Animation ANIMATION_SUMMON = Animation.create(120);

    public static final Animation ANIMATION_STOMP = Animation.create(50);

    public static final Animation ANIMATION_SPEW_FLAMES = Animation.create(80);

    public static final Animation ANIMATION_JUMP = Animation.create(45);

    public static final Animation ANIMATION_LEFT_KICK = Animation.create(20);

    public static final Animation ANIMATION_RIGHT_KICK = Animation.create(20);

    public static final Animation ANIMATION_LEFT_WHIP = Animation.create(40);

    public static final Animation ANIMATION_RIGHT_WHIP = Animation.create(40);

    public static final Animation ANIMATION_EAT_LEAVES = Animation.create(100);

    private static final int STOMP_CRUSH_HEIGHT = 6;

    public LuxtructosaurusLegSolver legSolver = new LuxtructosaurusLegSolver(0.2F, 2.0F, 1.2F, 1.9F, 2.0F);

    private Animation currentAnimation;

    private int animationTick;

    private final SauropodPartEntity[] allParts;

    public final SauropodPartEntity neckPart1;

    public final SauropodPartEntity neckPart2;

    public final SauropodPartEntity neckPart3;

    public final SauropodPartEntity headPart;

    public final SauropodPartEntity tailPart1;

    public final SauropodPartEntity tailPart2;

    public final SauropodPartEntity tailPart3;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private float prevWalkAnimPosition;

    private float walkAnimPosition;

    private float prevWalkAnimSpeed;

    private float walkAnimSpeed;

    private double lastStompX;

    private double lastStompZ;

    private float prevLegBackAmount = 0.0F;

    private float legBackAmount = 0.0F;

    private float prevRaiseArmsAmount = 0.0F;

    private float raiseArmsAmount = 0.0F;

    protected float neckXRot;

    protected float neckYRot;

    protected float tailXRot;

    protected float tailYRot;

    private float prevScreenShakeAmount;

    protected float screenShakeAmount;

    private float[] yawBuffer = new float[128];

    private int yawPointer = -1;

    private float lastYawBeforeWhip;

    public boolean turningFast;

    private boolean wasPreviouslyChild;

    private int stepSoundCooldown = 0;

    public SauropodBaseEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.f_21342_ = new SauropodBaseEntity.SauropodMoveHelper();
        this.neckPart1 = new SauropodPartEntity(this, this, 3.0F, 3.0F);
        this.neckPart2 = new SauropodPartEntity(this, this.neckPart1, 2.0F, 2.0F);
        this.neckPart3 = new SauropodPartEntity(this, this.neckPart2, 2.0F, 1.5F);
        this.headPart = new SauropodPartEntity(this, this.neckPart3, 2.0F, 2.0F);
        this.tailPart1 = new SauropodPartEntity(this, this, 3.0F, 2.0F);
        this.tailPart2 = new SauropodPartEntity(this, this.tailPart1, 2.5F, 1.5F);
        this.tailPart3 = new SauropodPartEntity(this, this.tailPart2, 2.0F, 1.0F);
        this.allParts = new SauropodPartEntity[] { this.neckPart1, this.neckPart2, this.neckPart3, this.headPart, this.tailPart1, this.tailPart2, this.tailPart3 };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(WALKING, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level);
    }

    public boolean isFakeEntity() {
        return this.f_19803_;
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        this.prevRaiseArmsAmount = this.raiseArmsAmount;
        this.prevScreenShakeAmount = this.screenShakeAmount;
        this.legSolver.update(this, this.f_20883_, this.m_6134_());
        if (this.shouldRaiseArms() && this.raiseArmsAmount < 5.0F) {
            this.raiseArmsAmount++;
        }
        if (!this.shouldRaiseArms() && this.raiseArmsAmount > 0.0F) {
            this.raiseArmsAmount--;
        }
        if (this.screenShakeAmount > 0.0F) {
            this.screenShakeAmount = Math.max(0.0F, this.screenShakeAmount - 0.3F);
        }
        if (this.getAnimation() != ANIMATION_LEFT_WHIP && this.getAnimation() != ANIMATION_RIGHT_WHIP) {
            this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.m_146908_(), this.turningFast ? 10.0F : 2.0F);
            this.lastYawBeforeWhip = this.m_146908_();
        } else {
            float negative = this.getAnimation() == ANIMATION_RIGHT_WHIP ? -1.0F : 1.0F;
            float target = 0.0F;
            if (this.getAnimationTick() > 10) {
                float f = (float) (this.getAnimationTick() - 10) / 30.0F;
                target = f * 230.0F;
            }
            if ((float) this.getAnimationTick() > 30.0F) {
                this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.lastYawBeforeWhip, 15.0F);
            } else {
                this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.lastYawBeforeWhip + negative * target, 90.0F);
            }
        }
        this.tickMultipart();
        this.tickWalking();
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
            } else {
                this.m_20090_();
            }
        }
        if ((this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() > 25 && this.getAnimationTick() < 35 || this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() > 5 && this.getAnimationTick() < 45) && this.screenShakeAmount <= 2.0F) {
            this.screenShakeAmount = 2.0F;
        }
        if (this.wasPreviouslyChild != this.m_6162_()) {
            this.wasPreviouslyChild = this.m_6162_();
            this.m_6210_();
            for (SauropodPartEntity sauropodPartEntity : this.allParts) {
                sauropodPartEntity.m_6210_();
            }
        }
    }

    private void tickWalking() {
        this.prevWalkAnimPosition = this.walkAnimPosition;
        this.prevWalkAnimSpeed = this.walkAnimSpeed;
        this.prevLegBackAmount = this.legBackAmount;
        float f = this.getLegSlamAmount(2.0F, 0.333F);
        float f1 = this.getLegSlamAmount(2.0F, 0.666F);
        Vec3 movement = this.m_20184_();
        float speed = (float) movement.length();
        if (this.areLegsMoving()) {
            this.walkAnimPosition = this.walkAnimPosition + this.walkAnimSpeed;
            if (this.getAnimation() != ANIMATION_LEFT_WHIP && this.getAnimation() != ANIMATION_RIGHT_WHIP) {
                this.walkAnimSpeed = Mth.approach(this.walkAnimSpeed, this.m_6162_() ? 2.0F : 1.0F, this.m_6162_() ? 0.2F : 0.1F);
            } else {
                this.walkAnimSpeed = Mth.approach(this.walkAnimSpeed, 4.0F, 0.66F);
            }
        } else {
            if ((double) f > 0.05) {
                this.walkAnimPosition = this.walkAnimPosition + Math.min(f - 0.05F, this.walkAnimSpeed);
            }
            if (this.walkAnimSpeed > 0.0F) {
                this.walkAnimSpeed = Math.max(0.0F, this.walkAnimSpeed - 0.025F);
            }
        }
        if ((double) f <= 0.05 && this.walkAnimSpeed > 0.0F && this.m_20096_() && (speed > 0.003F || this.m_20160_()) && this.stepSoundCooldown <= 0) {
            this.onStep();
            this.stepSoundCooldown = 5;
        }
        if (f1 < 0.65F) {
            this.lastStompX = this.m_20185_();
            this.lastStompZ = this.m_20189_();
        }
        if (this.stepSoundCooldown > 0) {
            this.stepSoundCooldown--;
        }
        double stompX = this.m_20185_() - this.lastStompX;
        double stompZ = this.m_20189_() - this.lastStompZ;
        float stompDist = Mth.sqrt((float) (stompX * stompX + stompZ * stompZ));
        if ((double) speed <= 0.003 || !this.areLegsMoving()) {
            stompDist = 0.0F;
        }
        if (this.getAnimation() == ANIMATION_SPEAK && this.getAnimationTick() == 2) {
            this.actuallyPlayAmbientSound();
        }
        this.legBackAmount = Mth.clamp(Mth.approach(this.legBackAmount, stompDist, speed), -1.0F, 1.0F);
    }

    protected abstract void onStep();

    public boolean shouldRaiseArms() {
        return this.isDancing() || this.getAnimation() == ANIMATION_LEFT_KICK || this.getAnimation() == ANIMATION_RIGHT_KICK || this.getAnimation() == ANIMATION_STOMP || this.getAnimation() == ANIMATION_JUMP;
    }

    private void tickMultipart() {
        if (this.yawPointer == -1) {
            for (int i = 0; i < this.yawBuffer.length; i++) {
                this.yawBuffer[i] = this.f_20883_;
            }
        }
        if (++this.yawPointer == this.yawBuffer.length) {
            this.yawPointer = 0;
        }
        this.yawBuffer[this.yawPointer] = this.f_20883_;
        Vec3[] avector3d = new Vec3[this.allParts.length];
        for (int j = 0; j < this.allParts.length; j++) {
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
        }
        float neckRotateSpeed = this.getNeckRotateSpeed();
        float tailRotateSpeed = this.getTailRotateSpeed();
        if (this.turningFast) {
            neckRotateSpeed += 30.0F;
            tailRotateSpeed += 30.0F;
        }
        this.neckXRot = this.wrapNeckDegrees(Mth.approachDegrees(this.neckXRot, this.getTargetNeckXRot(), neckRotateSpeed));
        this.neckYRot = this.wrapNeckDegrees(Mth.approachDegrees(this.neckYRot, this.getTargetNeckYRot(), neckRotateSpeed));
        this.tailXRot = this.wrapNeckDegrees(Mth.approachDegrees(this.tailXRot, this.getTargetTailXRot(), tailRotateSpeed));
        this.tailYRot = this.wrapNeckDegrees(Mth.approachDegrees(this.tailYRot, this.getTargetTailYRot(), tailRotateSpeed));
        Vec3 center = this.m_20182_().add(0.0, (double) (this.m_20206_() * 0.5F - this.getLegSolverBodyOffset()), 0.0);
        float headXStep = this.neckXRot / 4.0F;
        float headYStep = this.neckYRot / 4.0F;
        float tailXStep = this.tailXRot / 3.0F;
        float tailYStep = this.tailYRot / 3.0F;
        float neckAdditionalY = 0.0F;
        float neckAdditionalZ = 0.0F;
        if (this.getAnimation() == ANIMATION_STOMP) {
            float f = ACMath.cullAnimationTick(this.getAnimationTick(), 2.0F, ANIMATION_STOMP, 1.0F, 0, 30);
            neckAdditionalY = 4.0F * f;
            neckAdditionalZ = -4.0F * f;
        } else if (this.isDancing()) {
            float f = this.getDanceProgress(1.0F);
            neckAdditionalY = 4.0F * f;
            neckAdditionalZ = -4.0F * f;
            headYStep *= 1.0F - f;
        }
        this.neckPart1.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, (double) (2.0F + neckAdditionalY), (double) (5.0F + neckAdditionalZ)).scale((double) this.m_6134_()), headXStep, this.f_20883_ + headYStep).add(center));
        this.neckPart2.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, 2.5).scale((double) this.m_6134_()), headXStep, this.f_20883_ + headYStep * 2.0F).add(this.neckPart1.centeredPosition()));
        this.neckPart3.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, 2.0).scale((double) this.m_6134_()), headXStep, this.f_20883_ + headYStep * 3.0F).add(this.neckPart2.centeredPosition()));
        this.headPart.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, 2.5).scale((double) this.m_6134_()), headXStep, this.f_20883_ + headYStep * 4.0F).add(this.neckPart3.centeredPosition()));
        this.tailPart1.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, -0.5, -3.5).scale((double) this.m_6134_()), tailXStep, this.f_20883_ + tailYStep).add(center));
        this.tailPart2.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, -0.25, -3.25).scale((double) this.m_6134_()), tailXStep, this.f_20883_ + tailYStep * 2.0F).add(this.tailPart1.centeredPosition()));
        this.tailPart3.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, -2.5).scale((double) this.m_6134_()), tailXStep, this.f_20883_ + tailYStep * 3.0F).add(this.tailPart2.centeredPosition()));
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    private float wrapNeckDegrees(float f) {
        return f % 360.0F;
    }

    protected void crushBlocksInRing(int width, int ringStartX, int ringStartZ, float dropChance) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        float lowestFoot = 0.0F;
        for (LuxtructosaurusLegSolver.Leg leg : this.legSolver.legs) {
            float height = leg.getHeight(1.0F);
            if (height > lowestFoot) {
                lowestFoot = height;
            }
        }
        int feetY = this.m_20183_().m_123342_() - (int) lowestFoot;
        BlockPos center = new BlockPos(ringStartX, feetY, ringStartZ);
        if (ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) || this.m_20160_() && this.m_6688_() instanceof Player) {
            for (int y = 0; y <= 6; y++) {
                List<MovingBlockData> dataPerYLevel = new ArrayList();
                int currentBlocksInChunk = 0;
                for (int i = -width - 1; i <= width + 1; i++) {
                    for (int j = -width - 1; j <= width + 1; j++) {
                        mutableBlockPos.set(this.m_146903_() + i, feetY + y, this.m_146907_() + j);
                        double dist = Math.sqrt(mutableBlockPos.m_123331_(center));
                        if (dist <= (double) width && this.m_9236_().isLoaded(mutableBlockPos)) {
                            BlockState state = this.m_9236_().getBlockState(mutableBlockPos);
                            if (!state.m_204336_(ACTagRegistry.UNMOVEABLE) && !state.m_60795_() && !state.m_247087_() && !(state.m_60734_().getExplosionResistance() > (float) AlexsCaves.COMMON_CONFIG.atlatitanMaxExplosionResistance.get().intValue())) {
                                BlockEntity te = this.m_9236_().getBlockEntity(mutableBlockPos);
                                BlockPos offset = mutableBlockPos.immutable().subtract(center);
                                MovingBlockData data = new MovingBlockData(state, state.m_60808_(this.m_9236_(), mutableBlockPos), offset, te == null ? null : te.saveWithoutMetadata());
                                dataPerYLevel.add(data);
                                if (currentBlocksInChunk < 16) {
                                    currentBlocksInChunk++;
                                } else {
                                    CrushedBlockEntity crushed = ACEntityRegistry.CRUSHED_BLOCK.get().create(this.m_9236_());
                                    crushed.m_20219_(Vec3.atCenterOf(center.above(y)));
                                    crushed.setAllBlockData(FallingTreeBlockEntity.createTagFromData(dataPerYLevel));
                                    crushed.setPlacementCooldown(10);
                                    crushed.setDropChance(dropChance);
                                    this.m_9236_().m_7967_(crushed);
                                    dataPerYLevel.clear();
                                    currentBlocksInChunk = 0;
                                }
                                this.m_9236_().setBlockAndUpdate(mutableBlockPos, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
                if (!dataPerYLevel.isEmpty()) {
                    CrushedBlockEntity crushed = ACEntityRegistry.CRUSHED_BLOCK.get().create(this.m_9236_());
                    crushed.m_20219_(Vec3.atCenterOf(center.above(y)));
                    crushed.setAllBlockData(FallingTreeBlockEntity.createTagFromData(dataPerYLevel));
                    crushed.setDropChance(dropChance);
                    crushed.setPlacementCooldown(1);
                    this.m_9236_().m_7967_(crushed);
                }
            }
        }
    }

    protected Vec3 rotateOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(-xRot * (float) (Math.PI / 180.0)).yRot(-yRot * (float) (Math.PI / 180.0));
    }

    public float getLegSolverBodyOffset() {
        float heightBackLeft = this.legSolver.backLeft.getHeight(1.0F);
        float heightBackRight = this.legSolver.backRight.getHeight(1.0F);
        float heightFrontLeft = this.legSolver.frontLeft.getHeight(1.0F);
        float heightFrontRight = this.legSolver.frontRight.getHeight(1.0F);
        float armsWalkAmount = 1.0F - this.raiseArmsAmount / 5.0F;
        return Math.max(Math.max(heightBackLeft, heightBackRight), armsWalkAmount * Math.max(heightFrontLeft, heightFrontRight)) * 0.8F;
    }

    @Override
    public int getMaxHeadYRot() {
        return 90;
    }

    @Override
    public int getHeadRotSpeed() {
        return 3;
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
            this.setAnimation(ANIMATION_SPEAK);
        }
    }

    public void actuallyPlayAmbientSound() {
        SoundEvent soundevent = this.m_7515_();
        if (soundevent != null) {
            this.m_5496_(soundevent, this.m_6121_(), this.m_6100_());
        }
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    public float getYawFromBuffer(int pointer, float partialTick) {
        int i = this.yawPointer - pointer & 127;
        int j = this.yawPointer - pointer - 1 & 127;
        float d0 = this.yawBuffer[j];
        float d1 = this.yawBuffer[i] - d0;
        return d0 + d1 * partialTick;
    }

    public float getTargetNeckXRot() {
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() < 50) {
            return -140.0F;
        } else if (this.getAnimation() == ANIMATION_EPIC_DEATH && this.getAnimationTick() < 110) {
            return -140.0F;
        } else if (this.getAnimation() == ANIMATION_SUMMON && this.getAnimationTick() < 70) {
            return -60.0F;
        } else if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() <= 30) {
            return 30.0F;
        } else if (this.getAnimation() == ANIMATION_SPEW_FLAMES && this.getAnimationTick() < 70) {
            return 60.0F + (float) (Math.sin((double) ((float) this.animationTick * 0.4F)) * 10.0);
        } else {
            return this.isDancing() ? 30.0F : -30.0F;
        }
    }

    public float getTargetNeckYRot() {
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() < 50) {
            return (float) (Math.sin((double) ((float) this.animationTick * 0.2F)) * 40.0);
        } else if (this.getAnimation() == ANIMATION_EPIC_DEATH && this.getAnimationTick() < 110) {
            return (float) (Math.sin((double) ((float) this.animationTick * 0.1F)) * 20.0);
        } else if (this.getAnimation() == ANIMATION_SUMMON && this.getAnimationTick() < 50) {
            return 110.0F;
        } else if (this.getAnimation() == ANIMATION_SPEW_FLAMES && this.getAnimationTick() < 70) {
            return (float) (Math.sin((double) ((float) this.animationTick * 0.15F)) * 40.0);
        } else {
            float buffered = this.getYawFromBuffer(10, 1.0F) - this.f_20883_;
            return this.m_6080_() - this.f_20883_ + buffered;
        }
    }

    private float getNeckRotateSpeed() {
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() < 50) {
            return 30.0F;
        } else if (this.getAnimation() == ANIMATION_SUMMON) {
            return 2.0F;
        } else if (this.getAnimation() == ANIMATION_SPEW_FLAMES && this.getAnimationTick() < 70) {
            return 40.0F;
        } else {
            return this.getAnimation() != ANIMATION_LEFT_WHIP && this.getAnimation() != ANIMATION_RIGHT_WHIP ? 10.0F : 30.0F;
        }
    }

    public float getTargetTailXRot() {
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() < 50) {
            return -20.0F;
        } else if (this.getAnimation() == ANIMATION_EPIC_DEATH && this.getAnimationTick() < 110) {
            return -20.0F;
        } else if (this.getAnimation() == ANIMATION_SUMMON) {
            return -100.0F;
        } else if (this.getAnimation() != ANIMATION_LEFT_WHIP && this.getAnimation() != ANIMATION_RIGHT_WHIP) {
            return 0.0F;
        } else {
            return this.getAnimationTick() > 20 ? -20.0F : 20.0F;
        }
    }

    public float getTargetTailYRot() {
        if (this.getAnimation() == ANIMATION_LEFT_WHIP) {
            return this.getAnimationTick() > 24 ? 70.0F : -70.0F;
        } else if (this.getAnimation() == ANIMATION_RIGHT_WHIP) {
            return this.getAnimationTick() > 24 ? -70.0F : 70.0F;
        } else {
            return this.getYawFromBuffer(20, 1.0F) - this.f_20883_;
        }
    }

    private float getTailRotateSpeed() {
        return this.getAnimation() != ANIMATION_LEFT_WHIP && this.getAnimation() != ANIMATION_RIGHT_WHIP ? 10.0F : 30.0F;
    }

    public boolean areLegsMoving() {
        return (this.f_19804_.get(WALKING) || this.getAnimation() == ANIMATION_LEFT_WHIP || this.getAnimation() == ANIMATION_RIGHT_WHIP) && !this.isImmobile() && !this.m_21525_();
    }

    public float getLegSlamAmount(float speed, float offset) {
        float walkSpeed = 0.05F;
        return Math.abs((float) Math.cos((double) (this.getWalkAnimPosition(1.0F) * walkSpeed * speed) - Math.PI * (double) offset));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    public float getStepHeight() {
        return 3.2F;
    }

    public boolean hurtEntitiesAround(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean setsOnFire, boolean disablesShields) {
        AABB aabb = new AABB(center.subtract((double) radius, (double) radius, (double) radius), center.add((double) radius, (double) radius, (double) radius));
        boolean flag = false;
        DamageSource damageSource = this.m_269291_().mobAttack(this);
        for (LivingEntity living : this.m_9236_().m_6443_(LivingEntity.class, aabb, EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
            if (!living.m_7306_(this) && !living.m_7307_(this) && living.m_6095_() != this.m_6095_() && living.m_20275_(center.x, center.y, center.z) <= (double) (radius * radius)) {
                if (living.isDamageSourceBlocked(damageSource) && disablesShields && living instanceof Player player) {
                    player.disableShield(true);
                }
                if (living.hurt(damageSource, damageAmount)) {
                    flag = true;
                    if (setsOnFire) {
                        living.m_20254_(10);
                    }
                    living.knockback((double) knockbackAmount, center.x - living.m_20185_(), center.z - living.m_20189_());
                }
            }
        }
        return flag;
    }

    @Override
    public boolean isImmobile() {
        return super.m_6107_() || this.getAnimation() == ANIMATION_SUMMON;
    }

    @Override
    public float getScreenShakeAmount(float partialTicks) {
        return !this.m_6084_() ? 0.0F : this.prevScreenShakeAmount + (this.screenShakeAmount - this.prevScreenShakeAmount) * partialTicks;
    }

    public float getRaiseArmsAmount(float partialTicks) {
        return (this.prevRaiseArmsAmount + (this.raiseArmsAmount - this.prevRaiseArmsAmount) * partialTicks) * 0.2F;
    }

    public float getWalkAnimPosition(float partialTicks) {
        return this.prevWalkAnimPosition + (this.walkAnimPosition - this.prevWalkAnimPosition) * partialTicks;
    }

    public float getWalkAnimSpeed(float partialTicks) {
        return this.prevWalkAnimSpeed + (this.walkAnimSpeed - this.prevWalkAnimSpeed) * partialTicks;
    }

    public float getLegBackAmount(float partialTicks) {
        return this.prevLegBackAmount + (this.legBackAmount - this.prevLegBackAmount) * partialTicks;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SPEAK, ANIMATION_ROAR, ANIMATION_EPIC_DEATH, ANIMATION_SUMMON, ANIMATION_STOMP, ANIMATION_SPEW_FLAMES, ANIMATION_JUMP, ANIMATION_LEFT_KICK, ANIMATION_RIGHT_KICK, ANIMATION_LEFT_WHIP, ANIMATION_RIGHT_WHIP, ANIMATION_EAT_LEAVES };
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= this.getProjectileDamageReduction();
        }
        return super.m_6469_(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.m_6673_(damageSource) || damageSource.is(DamageTypes.IN_WALL);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(2.0);
    }

    public float getProjectileDamageReduction() {
        return 0.75F;
    }

    @Override
    public int getMaxNavigableDistanceToGround() {
        return 3;
    }

    private class SauropodMoveHelper extends MoveControl {

        public SauropodMoveHelper() {
            super(SauropodBaseEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.WAIT) {
                SauropodBaseEntity.this.f_19804_.set(SauropodBaseEntity.WALKING, false);
                this.f_24978_ = 0.0;
            } else {
                SauropodBaseEntity.this.f_19804_.set(SauropodBaseEntity.WALKING, true);
                float f = SauropodBaseEntity.this.getLegSlamAmount(2.0F, 0.66F);
                boolean flag = true;
                if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                    double d0 = this.f_24975_ - this.f_24974_.m_20185_();
                    double d1 = this.f_24977_ - this.f_24974_.m_20189_();
                    float moveToRot = (float) (Mth.atan2(d1, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    float difference = Mth.degreesDifferenceAbs(SauropodBaseEntity.this.f_20883_, moveToRot);
                    flag = difference < 15.0F;
                }
                if (SauropodBaseEntity.this.getAnimation() == SauropodBaseEntity.ANIMATION_LEFT_WHIP || SauropodBaseEntity.this.getAnimation() == SauropodBaseEntity.ANIMATION_RIGHT_WHIP) {
                    flag = true;
                }
                float threshold = 0.65F;
                if (f >= threshold && flag) {
                    float f1 = (f - threshold) / (1.0F - threshold);
                    if (SauropodBaseEntity.this.m_6162_()) {
                        f1 *= 0.5F;
                    }
                    this.f_24978_ = (double) f1;
                } else {
                    this.f_24978_ = 0.0;
                }
            }
            super.tick();
        }
    }
}