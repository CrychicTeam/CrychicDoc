package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.DragonFireEvent;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageDragonSyncFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
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

public class EntityLightningDragon extends EntityDragonBase {

    public static final ResourceLocation FEMALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/lightning_dragon_female");

    public static final ResourceLocation MALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/lightning_dragon_male");

    public static final ResourceLocation SKELETON_LOOT = new ResourceLocation("iceandfire", "entities/dragon/lightning_dragon_skeleton");

    private static final EntityDataAccessor<Boolean> HAS_LIGHTNING_TARGET = SynchedEntityData.defineId(EntityLightningDragon.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> LIGHTNING_TARGET_X = SynchedEntityData.defineId(EntityLightningDragon.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> LIGHTNING_TARGET_Y = SynchedEntityData.defineId(EntityLightningDragon.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> LIGHTNING_TARGET_Z = SynchedEntityData.defineId(EntityLightningDragon.class, EntityDataSerializers.FLOAT);

    public EntityLightningDragon(Level worldIn) {
        this(IafEntityRegistry.LIGHTNING_DRAGON.get(), worldIn);
    }

    public EntityLightningDragon(EntityType<?> t, Level worldIn) {
        super(t, worldIn, DragonType.LIGHTNING, 1.0, (double) (1 + IafConfig.dragonAttackDamage), IafConfig.dragonHealth * 0.04, IafConfig.dragonHealth, 0.15F, 0.4F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
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

    public void onStruckByLightning(LightningBolt lightningBolt) {
        this.m_5634_(15.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(HAS_LIGHTNING_TARGET, false);
        this.f_19804_.define(LIGHTNING_TARGET_X, 0.0F);
        this.f_19804_.define(LIGHTNING_TARGET_Y, 0.0F);
        this.f_19804_.define(LIGHTNING_TARGET_Z, 0.0F);
    }

    @Override
    public int getStartMetaForType() {
        return 8;
    }

    @Override
    protected boolean shouldTarget(Entity entity) {
        return entity instanceof EntityDragonBase && !this.m_21824_() ? entity.getType() != this.m_6095_() && this.m_20205_() >= entity.getBbWidth() && !((EntityDragonBase) entity).isMobDead() : entity instanceof Player || DragonUtils.isDragonTargetable(entity, IafTagRegistry.LIGHTNING_DRAGON_TARGETS) || !this.m_21824_() && DragonUtils.isVillager(entity);
    }

    @Override
    public boolean isTimeToWake() {
        return !this.m_9236_().isDay() || this.getCommand() == 2;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new FloatGoal(this));
    }

    @Override
    public String getVariantName(int variant) {
        switch(variant) {
            case 1:
                return "amythest_";
            case 2:
                return "copper_";
            case 3:
                return "black_";
            default:
                return "electric_";
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        if (i.getMsgId().equals(this.m_9236_().damageSources().lightningBolt().getMsgId())) {
            this.m_5634_(15.0F);
            this.m_7292_(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 1));
            return true;
        } else {
            return super.m_6673_(i);
        }
    }

    @Override
    public Item getVariantScale(int variant) {
        switch(variant) {
            case 1:
                return IafItemRegistry.DRAGONSCALES_AMYTHEST.get();
            case 2:
                return IafItemRegistry.DRAGONSCALES_COPPER.get();
            case 3:
                return IafItemRegistry.DRAGONSCALES_BLACK.get();
            default:
                return IafItemRegistry.DRAGONSCALES_ELECTRIC.get();
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch(variant) {
            case 1:
                return IafItemRegistry.DRAGONEGG_AMYTHEST.get();
            case 2:
                return IafItemRegistry.DRAGONEGG_COPPER.get();
            case 3:
                return IafItemRegistry.DRAGONEGG_BLACK.get();
            default:
                return IafItemRegistry.DRAGONEGG_ELECTRIC.get();
        }
    }

    public void setHasLightningTarget(boolean lightning_target) {
        this.f_19804_.set(HAS_LIGHTNING_TARGET, lightning_target);
    }

    public boolean hasLightningTarget() {
        return this.f_19804_.get(HAS_LIGHTNING_TARGET);
    }

    public void setLightningTargetVec(float x, float y, float z) {
        this.f_19804_.set(LIGHTNING_TARGET_X, x);
        this.f_19804_.set(LIGHTNING_TARGET_Y, y);
        this.f_19804_.set(LIGHTNING_TARGET_Z, z);
    }

    public float getLightningTargetX() {
        return this.f_19804_.get(LIGHTNING_TARGET_X);
    }

    public float getLightningTargetY() {
        return this.f_19804_.get(LIGHTNING_TARGET_Y);
    }

    public float getLightningTargetZ() {
        return this.f_19804_.get(LIGHTNING_TARGET_Z);
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING.get();
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
        if (!this.isBreathingFire()) {
            this.setHasLightningTarget(false);
        }
    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                this.m_146922_(this.f_20883_);
                if (this.fireTicks % 7 == 0) {
                    this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4.0F, 1.0F);
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
                this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH_CRACKLE, 4.0F, 1.0F);
                double d2 = controller.getLookAngle().x;
                double d3 = controller.getLookAngle().y;
                double d4 = controller.getLookAngle().z;
                float inaccuracy = 1.0F;
                d2 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                d3 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                d4 += this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy;
                EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                entitylargefireball.m_6034_(headVec.x, headVec.y, headVec.z);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(entitylargefireball);
                }
            }
        } else if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                this.m_146922_(this.f_20883_);
                if (this.fireTicks % 7 == 0) {
                    this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4.0F, 1.0F);
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
    public Item getBloodItem() {
        return IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get();
    }

    @Override
    public Item getFleshItem() {
        return IafItemRegistry.LIGHTNING_DRAGON_FLESH.get();
    }

    @Override
    public ItemLike getHeartItem() {
        return IafItemRegistry.LIGHTNING_DRAGON_HEART.get();
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
                    this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4.0F, 1.0F);
                    EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                    if (this.m_6162_()) {
                        float var10000 = 0.4F;
                    } else {
                        float var15 = this.m_6125_() ? 1.3F : 0.8F;
                    }
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
                        this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH, 4.0F, 1.0F);
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
                    this.m_5496_(IafSoundRegistry.LIGHTNINGDRAGON_BREATH_CRACKLE, 4.0F, 1.0F);
                    EntityDragonLightningCharge entitylargefireball = new EntityDragonLightningCharge(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), this.m_9236_(), this, d2, d3, d4);
                    if (this.m_6162_()) {
                        float var10000 = 0.4F;
                    } else {
                        float var41 = this.m_6125_() ? 1.3F : 0.8F;
                    }
                    entitylargefireball.m_6034_(headVec.x, headVec.y, headVec.z);
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(entitylargefireball);
                    }
                }
            } else {
                this.burnParticleX = burnX;
                this.burnParticleY = burnY;
                this.burnParticleZ = burnZ;
                Vec3 headPos = this.getHeadPosition();
                double d2x = burnX - headPos.x;
                double d3x = burnY - headPos.y;
                double d4x = burnZ - headPos.z;
                float particleScale = Mth.clamp(this.getRenderSize() * 0.08F, 0.55F, 3.0F);
                double distance = Math.max(2.5 * this.m_20275_(burnX, burnY, burnZ), 0.0);
                double conqueredDistance = (double) this.burnProgress / 40.0 * distance;
                int increment = (int) Math.ceil(conqueredDistance / 100.0);
                for (int i = 0; (double) i < conqueredDistance; i += increment) {
                    double progressX = headPos.x + d2x * (double) ((float) i / (float) distance);
                    double progressY = headPos.y + d3x * (double) ((float) i / (float) distance);
                    double progressZ = headPos.z + d4x * (double) ((float) i / (float) distance);
                    if (this.canPositionBeSeen(progressX, progressY, progressZ)) {
                        this.setHasLightningTarget(true);
                        this.setLightningTargetVec((float) burnX, (float) burnY, (float) burnZ);
                    } else if (!this.m_9236_().isClientSide) {
                        HitResult result = this.m_9236_().m_45547_(new ClipContext(new Vec3(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_()), new Vec3(progressX, progressY, progressZ), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        Vec3 vec3 = result.getLocation();
                        BlockPos pos = BlockPos.containing(vec3);
                        IafDragonDestructionManager.destroyAreaBreath(this.m_9236_(), pos, this);
                        this.setHasLightningTarget(true);
                        this.setLightningTargetVec((float) result.getLocation().x, (float) result.getLocation().y, (float) result.getLocation().z);
                    }
                }
                if ((double) this.burnProgress >= 40.0 && this.canPositionBeSeen(burnX, burnY, burnZ)) {
                    double spawnX = burnX + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    double spawnY = burnY + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    double spawnZ = burnZ + (double) this.f_19796_.nextFloat() * 3.0 - 1.5;
                    this.setHasLightningTarget(true);
                    this.setLightningTargetVec((float) spawnX, (float) spawnY, (float) spawnZ);
                    if (!this.m_9236_().isClientSide) {
                        IafDragonDestructionManager.destroyAreaBreath(this.m_9236_(), BlockPos.containing(spawnX, spawnY, spawnZ), this);
                    }
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_IDLE : (this.m_6125_() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_IDLE : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_IDLE);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_HURT : (this.m_6125_() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_HURT : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_HURT);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_DEATH : (this.m_6125_() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_DEATH : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_DEATH);
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSoundRegistry.LIGHTNINGDRAGON_TEEN_ROAR : (this.m_6125_() ? IafSoundRegistry.LIGHTNINGDRAGON_ADULT_ROAR : IafSoundRegistry.LIGHTNINGDRAGON_CHILD_ROAR);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, ANIMATION_TAILWHACK, ANIMATION_FIRECHARGE, ANIMATION_WINGBLAST, ANIMATION_ROAR, ANIMATION_EPIC_ROAR };
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.LIGHTNING_STEW.get();
    }

    @Override
    protected void spawnDeathParticles() {
        for (int k = 0; k < 3; k++) {
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            if (this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(ParticleTypes.RAIN, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d2, d0, d1);
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
        return new ItemStack(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get());
    }
}