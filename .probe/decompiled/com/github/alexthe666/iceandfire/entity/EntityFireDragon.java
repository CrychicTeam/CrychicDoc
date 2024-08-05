package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.DragonFireEvent;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageDragonSyncFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class EntityFireDragon extends EntityDragonBase {

    public static final ResourceLocation FEMALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/fire_dragon_female");

    public static final ResourceLocation MALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/fire_dragon_male");

    public static final ResourceLocation SKELETON_LOOT = new ResourceLocation("iceandfire", "entities/dragon/fire_dragon_skeleton");

    public EntityFireDragon(Level worldIn) {
        this(IafEntityRegistry.FIRE_DRAGON.get(), worldIn);
    }

    public EntityFireDragon(EntityType<?> t, Level worldIn) {
        super(t, worldIn, DragonType.FIRE, 1.0, (double) (1 + IafConfig.dragonAttackDamage), IafConfig.dragonHealth * 0.04, IafConfig.dragonHealth, 0.15F, 0.4F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.LAVA, 8.0F);
        ANIMATION_SPEAK = Animation.create(20);
        ANIMATION_BITE = Animation.create(35);
        ANIMATION_SHAKEPREY = Animation.create(65);
        ANIMATION_TAILWHACK = Animation.create(40);
        ANIMATION_FIRECHARGE = Animation.create(30);
        ANIMATION_WINGBLAST = Animation.create(50);
        ANIMATION_ROAR = Animation.create(40);
        ANIMATION_EPIC_ROAR = Animation.create(60);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
    }

    @Override
    protected boolean shouldTarget(Entity entity) {
        return entity instanceof EntityDragonBase && !this.m_21824_() ? entity.getType() != this.m_6095_() && this.m_20205_() >= entity.getBbWidth() && !((EntityDragonBase) entity).isMobDead() : entity instanceof Player || DragonUtils.isDragonTargetable(entity, IafTagRegistry.FIRE_DRAGON_TARGETS) || !this.m_21824_() && DragonUtils.isVillager(entity);
    }

    @Override
    public String getVariantName(int variant) {
        switch(variant) {
            case 1:
                return "green_";
            case 2:
                return "bronze_";
            case 3:
                return "gray_";
            default:
                return "red_";
        }
    }

    @Override
    public Item getVariantScale(int variant) {
        switch(variant) {
            case 1:
                return IafItemRegistry.DRAGONSCALES_GREEN.get();
            case 2:
                return IafItemRegistry.DRAGONSCALES_BRONZE.get();
            case 3:
                return IafItemRegistry.DRAGONSCALES_GRAY.get();
            default:
                return IafItemRegistry.DRAGONSCALES_RED.get();
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch(variant) {
            case 1:
                return IafItemRegistry.DRAGONEGG_GREEN.get();
            case 2:
                return IafItemRegistry.DRAGONEGG_BRONZE.get();
            case 3:
                return IafItemRegistry.DRAGONEGG_GRAY.get();
            default:
                return IafItemRegistry.DRAGONEGG_RED.get();
        }
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItemRegistry.SUMMONING_CRYSTAL_FIRE.get();
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        this.m_21563_().setLookAt(entityIn, 30.0F, 30.0F);
        if (!this.isPlayingAttackAnimation()) {
            switch(this.groundAttack) {
                case BITE:
                    this.setAnimation(ANIMATION_BITE);
                    break;
                case TAIL_WHIP:
                    this.setAnimation(ANIMATION_TAILWHACK);
                    break;
                case SHAKE_PREY:
                    boolean flag = false;
                    if (new Random().nextInt(2) == 0 && this.isDirectPathBetweenPoints(this, this.m_20182_().add(0.0, (double) (this.m_20206_() / 2.0F), 0.0), entityIn.position().add(0.0, (double) (entityIn.getBbHeight() / 2.0F), 0.0)) && entityIn.getBbWidth() < this.m_20205_() * 0.5F && this.m_6688_() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
                        this.setAnimation(ANIMATION_SHAKEPREY);
                        flag = true;
                        entityIn.startRiding(this);
                    }
                    if (!flag) {
                        this.groundAttack = IafDragonAttacks.Ground.BITE;
                        this.setAnimation(ANIMATION_BITE);
                    }
                    break;
                case WING_BLAST:
                    this.setAnimation(ANIMATION_WINGBLAST);
            }
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.m_5448_();
        if (!this.m_9236_().isClientSide && attackTarget != null) {
            if (this.m_20191_().inflate((double) (2.5F + this.getRenderSize() * 0.33F), (double) (2.5F + this.getRenderSize() * 0.33F), (double) (2.5F + this.getRenderSize() * 0.33F)).intersects(attackTarget.m_20191_())) {
                this.doHurtTarget(attackTarget);
            }
            if (this.groundAttack == IafDragonAttacks.Ground.FIRE && (this.usingGroundAttack || this.m_20096_())) {
                this.shootFireAtMob(attackTarget);
            }
            if (this.airAttack == IafDragonAttacks.Air.TACKLE && !this.usingGroundAttack && this.m_20280_(attackTarget) < 100.0) {
                double difX = attackTarget.m_20185_() - this.m_20185_();
                double difY = attackTarget.m_20186_() + (double) attackTarget.m_20206_() - this.m_20186_();
                double difZ = attackTarget.m_20189_() - this.m_20189_();
                this.m_20256_(this.m_20184_().add(difX * 0.1, difY * 0.1, difZ * 0.1));
                if (this.m_20191_().inflate((double) (1.0F + this.getRenderSize() * 0.5F), (double) (1.0F + this.getRenderSize() * 0.5F), (double) (1.0F + this.getRenderSize() * 0.5F)).intersects(attackTarget.m_20191_())) {
                    this.doHurtTarget(attackTarget);
                    this.usingGroundAttack = true;
                    this.randomizeAttacks();
                    this.setFlying(false);
                    this.setHovering(false);
                }
            }
        }
    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                this.m_146922_(this.f_20883_);
                if (this.f_19797_ % 5 == 0) {
                    this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                }
                this.stimulateFire((double) ((float) burningTarget.m_123341_() + 0.5F), (double) ((float) burningTarget.m_123342_() + 0.5F), (double) ((float) burningTarget.m_123343_() + 0.5F), 1);
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    @Override
    public void riderShootFire(Entity controller) {
        if (this.m_217043_().nextInt(5) == 0 && !this.m_6162_()) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                this.setAnimation(ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 20) {
                this.m_146922_(this.f_20883_);
                Vec3 headVec = this.getHeadPosition();
                this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                double d2 = controller.getLookAngle().x;
                double d3 = controller.getLookAngle().y;
                double d4 = controller.getLookAngle().z;
                float inaccuracy = 1.0F;
                d2 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                d3 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                d4 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                EntityDragonFireCharge entitylargefireball = new EntityDragonFireCharge(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                entitylargefireball.m_6034_(headVec.x, headVec.y, headVec.z);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(entitylargefireball);
                }
            }
        } else if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                this.m_146922_(this.f_20883_);
                if (this.f_19797_ % 5 == 0) {
                    this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                }
                HitResult mop = this.rayTraceRider(controller, (double) (10 * this.getDragonStage()), 1.0F);
                if (mop != null) {
                    this.stimulateFire(mop.getLocation().x, mop.getLocation().y, mop.getLocation().z, 1);
                }
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    @Override
    protected float getBlockSpeedFactor() {
        if (this.m_6046_()) {
            return this.getDragonStage() >= 2 ? 1.0F : 0.8F;
        } else {
            return super.m_6041_();
        }
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.m_20077_()) {
            if (this.m_21515_() && this.m_6688_() == null) {
                this.m_19920_(this.m_6113_(), pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.7));
                if (this.m_5448_() == null) {
                }
            } else if (this.allowLocalMotionControl && this.m_6688_() != null && !this.isHovering() && !this.isFlying()) {
                LivingEntity rider = this.m_6688_();
                float speed = (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
                float lavaSpeedMod = (float) (0.28F + 0.1 * Mth.map((double) speed, this.minimumSpeed, this.maximumSpeed, 0.0, 1.5));
                speed *= lavaSpeedMod;
                speed *= rider.m_20142_() ? 1.4F : 1.0F;
                float vertical = 0.0F;
                if (this.isGoingUp() && !this.isGoingDown()) {
                    vertical = 0.8F;
                } else if (this.isGoingDown() && !this.isGoingUp()) {
                    vertical = -0.8F;
                } else if (this.isGoingUp() && this.isGoingDown() && this.m_6109_()) {
                    this.m_20256_(this.m_20184_().multiply(1.0, 0.3F, 1.0));
                }
                Vec3 travelVector = new Vec3((double) rider.xxa, (double) vertical, (double) rider.zza);
                if (this.m_6109_()) {
                    this.m_7910_(speed);
                    this.m_19920_(this.m_6113_(), travelVector);
                    this.m_6478_(MoverType.SELF, this.m_20184_());
                    Vec3 currentMotion = this.m_20184_();
                    if (this.f_19862_) {
                        currentMotion = new Vec3(currentMotion.x, 0.2, currentMotion.z);
                    }
                    this.m_20256_(currentMotion.scale(0.7));
                    this.m_267651_(false);
                } else {
                    this.m_20256_(Vec3.ZERO);
                }
                this.m_146872_();
            } else {
                super.travel(pTravelVector);
            }
        } else {
            if (this.allowLocalMotionControl && this.m_6688_() != null && !this.isHovering() && !this.isFlying() && this.m_9236_().getBlockState(this.m_20099_()).m_60819_().is(FluidTags.LAVA)) {
                LivingEntity riderx = this.m_6688_();
                double forward = (double) riderx.zza;
                double strafing = (double) riderx.xxa;
                double verticalx = pTravelVector.y;
                float speedx = (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
                float groundSpeedModifier = (float) (1.8F * this.getFlightSpeedModifier());
                speedx *= groundSpeedModifier;
                forward *= riderx.m_20142_() ? 1.2F : 1.0;
                forward *= riderx.zza > 0.0F ? 1.0 : 0.2F;
                strafing *= 0.05F;
                if (this.m_6109_()) {
                    float flyingSpeed = speedx * 0.1F;
                    this.m_7910_(speedx);
                    super.travel(new Vec3(strafing, verticalx, forward));
                    Vec3 currentMotion = this.m_20184_();
                    if (this.f_19862_) {
                        currentMotion = new Vec3(currentMotion.x, 0.2, currentMotion.z);
                    }
                    this.m_20256_(currentMotion.scale(0.7));
                } else {
                    this.m_20256_(Vec3.ZERO);
                }
                this.m_146872_();
                return;
            }
            super.travel(pTravelVector);
        }
    }

    @Override
    public ResourceLocation getDeadLootTable() {
        if (this.getDeathStage() >= this.getAgeInDays() / 5 / 2) {
            return SKELETON_LOOT;
        } else {
            return this.isMale() ? MALE_LOOT : FEMALE_LOOT;
        }
    }

    private void shootFireAtMob(LivingEntity entity) {
        if (this.usingGroundAttack && this.groundAttack == IafDragonAttacks.Ground.FIRE || !this.usingGroundAttack && (this.airAttack == IafDragonAttacks.Air.SCORCH_STREAM || this.airAttack == IafDragonAttacks.Air.HOVER_BLAST)) {
            if (this.usingGroundAttack && this.m_217043_().nextInt(5) == 0 || !this.usingGroundAttack && this.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                    this.setAnimation(ANIMATION_FIRECHARGE);
                } else if (this.getAnimationTick() == 20) {
                    this.m_146922_(this.f_20883_);
                    Vec3 headVec = this.getHeadPosition();
                    double d2 = entity.m_20185_() - headVec.x;
                    double d3 = entity.m_20186_() - headVec.y;
                    double d4 = entity.m_20189_() - headVec.z;
                    float inaccuracy = 1.0F;
                    d2 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d3 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d4 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                    EntityDragonFireCharge entitylargefireball = new EntityDragonFireCharge(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                    entitylargefireball.m_6034_(headVec.x, headVec.y, headVec.z);
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(entitylargefireball);
                    }
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathingFire(false);
                    }
                    this.randomizeAttacks();
                }
            } else if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    this.m_146922_(this.f_20883_);
                    if (this.f_19797_ % 5 == 0) {
                        this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                    }
                    this.stimulateFire(entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), 1);
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathingFire(false);
                        this.randomizeAttacks();
                    }
                }
            } else {
                this.setBreathingFire(true);
            }
        }
        this.m_21391_(entity, 360.0F, 360.0F);
    }

    @Override
    public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
        if (!MinecraftForge.EVENT_BUS.post(new DragonFireEvent(this, burnX, burnY, burnZ))) {
            if (syncType == 1 && !this.m_9236_().isClientSide) {
                IceAndFire.sendMSGToAll(new MessageDragonSyncFire(this.m_19879_(), burnX, burnY, burnZ, 0));
            }
            if (syncType == 2 && this.m_9236_().isClientSide) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.m_19879_(), burnX, burnY, burnZ, 0));
            }
            if (syncType == 3 && !this.m_9236_().isClientSide) {
                IceAndFire.sendMSGToAll(new MessageDragonSyncFire(this.m_19879_(), burnX, burnY, burnZ, 5));
            }
            if (syncType == 4 && this.m_9236_().isClientSide) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.m_19879_(), burnX, burnY, burnZ, 5));
            }
            if (syncType > 2 && syncType < 6) {
                if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                    this.setAnimation(ANIMATION_FIRECHARGE);
                } else if (this.getAnimationTick() == 20) {
                    this.m_146922_(this.f_20883_);
                    Vec3 headVec = this.getHeadPosition();
                    double d2 = burnX - headVec.x;
                    double d3 = burnY - headVec.y;
                    double d4 = burnZ - headVec.z;
                    float inaccuracy = 1.0F;
                    d2 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d3 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    d4 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                    this.m_5496_(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                    EntityDragonFireCharge entitylargefireball = new EntityDragonFireCharge(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                    entitylargefireball.m_6034_(headVec.x, headVec.y, headVec.z);
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(entitylargefireball);
                    }
                    this.randomizeAttacks();
                }
            } else {
                this.m_21573_().stop();
                this.burnParticleX = burnX;
                this.burnParticleY = burnY;
                this.burnParticleZ = burnZ;
                Vec3 headPos = this.getHeadPosition();
                double d2 = burnX - headPos.x;
                double d3 = burnY - headPos.y;
                double d4 = burnZ - headPos.z;
                double distance = Math.max(2.5 * this.m_20275_(burnX, burnY, burnZ), 0.0);
                double conqueredDistance = (double) this.burnProgress / 40.0 * distance;
                int increment = (int) Math.ceil(conqueredDistance / 100.0);
                int particleCount = this.getDragonStage() <= 3 ? 6 : 3;
                for (int i = 0; (double) i < conqueredDistance; i += increment) {
                    double progressX = headPos.x + d2 * (double) ((float) i / (float) distance);
                    double progressY = headPos.y + d3 * (double) ((float) i / (float) distance);
                    double progressZ = headPos.z + d4 * (double) ((float) i / (float) distance);
                    if (this.canPositionBeSeen(progressX, progressY, progressZ)) {
                        if (this.m_9236_().isClientSide && this.f_19796_.nextInt(particleCount) == 0) {
                            IceAndFire.PROXY.spawnDragonParticle(EnumParticles.DragonFire, headPos.x, headPos.y, headPos.z, 0.0, 0.0, 0.0, this);
                        }
                    } else if (!this.m_9236_().isClientSide) {
                        HitResult result = this.m_9236_().m_45547_(new ClipContext(new Vec3(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_()), new Vec3(progressX, progressY, progressZ), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        Vec3 vec3 = result.getLocation();
                        BlockPos pos = BlockPos.containing(vec3);
                        IafDragonDestructionManager.destroyAreaBreath(this.m_9236_(), pos, this);
                    }
                }
                if ((double) this.burnProgress >= 40.0 && this.canPositionBeSeen(burnX, burnY, burnZ)) {
                    double spawnX = burnX + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    double spawnY = burnY + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    double spawnZ = burnZ + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    if (!this.m_9236_().isClientSide) {
                        IafDragonDestructionManager.destroyAreaBreath(this.m_9236_(), BlockPos.containing(spawnX, spawnY, spawnZ), this);
                    }
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_IDLE : (this.m_6125_() ? IafSoundRegistry.FIREDRAGON_ADULT_IDLE : IafSoundRegistry.FIREDRAGON_CHILD_IDLE);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_HURT : (this.m_6125_() ? IafSoundRegistry.FIREDRAGON_ADULT_HURT : IafSoundRegistry.FIREDRAGON_CHILD_HURT);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_DEATH : (this.m_6125_() ? IafSoundRegistry.FIREDRAGON_ADULT_DEATH : IafSoundRegistry.FIREDRAGON_CHILD_DEATH);
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSoundRegistry.FIREDRAGON_TEEN_ROAR : (this.m_6125_() ? IafSoundRegistry.FIREDRAGON_ADULT_ROAR : IafSoundRegistry.FIREDRAGON_CHILD_ROAR);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, ANIMATION_TAILWHACK, ANIMATION_FIRECHARGE, ANIMATION_WINGBLAST, ANIMATION_ROAR, ANIMATION_EPIC_ROAR };
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.FIRE_STEW.get();
    }

    @Override
    protected void spawnDeathParticles() {
        for (int k = 0; k < 3; k++) {
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            if (this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(ParticleTypes.FLAME, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d2, d0, d1);
            }
        }
    }

    @Override
    protected void spawnBabyParticles() {
        for (int i = 0; i < 5; i++) {
            float radiusAdd = (float) i * 0.15F;
            float headPosX = (float) (this.m_20185_() + (double) (1.8F * this.getRenderSize() * (0.3F + radiusAdd) * Mth.cos((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
            float headPosZ = (float) (this.m_20186_() + (double) (1.8F * this.getRenderSize() * (0.3F + radiusAdd) * Mth.sin((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
            float headPosY = (float) (this.m_20189_() + 0.5 * (double) this.getRenderSize() * 0.3F);
            this.m_9236_().addParticle(ParticleTypes.LARGE_SMOKE, (double) headPosX, (double) headPosY, (double) headPosZ, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public ItemStack getSkull() {
        return new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get());
    }

    @Override
    public Item getBloodItem() {
        return IafItemRegistry.FIRE_DRAGON_BLOOD.get();
    }

    @Override
    public Item getFleshItem() {
        return IafItemRegistry.FIRE_DRAGON_FLESH.get();
    }

    @Override
    public ItemLike getHeartItem() {
        return IafItemRegistry.FIRE_DRAGON_HEART.get();
    }
}