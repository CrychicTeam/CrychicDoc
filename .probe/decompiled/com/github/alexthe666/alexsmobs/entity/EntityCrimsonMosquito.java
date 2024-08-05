package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoDismount;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityCrimsonMosquito extends Monster {

    public static final ResourceLocation FULL_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_full");

    public static final ResourceLocation FROM_FLY_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_fly");

    public static final ResourceLocation FROM_FLY_FULL_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_fly_full");

    protected static final EntityDimensions FLIGHT_SIZE = EntityDimensions.fixed(1.2F, 1.8F);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> BLOOD_LEVEL = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SHRINKING = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_FLY = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> MOSQUITO_SCALE = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> SICK = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> LURING_LAVIATHAN = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> FLEEING_ENTITY = SynchedEntityData.defineId(EntityCrimsonMosquito.class, EntityDataSerializers.INT);

    private static final Predicate<LivingEntity> REPELLENT = mob -> mob.hasEffect(AMEffectRegistry.MOSQUITO_REPELLENT.get()) || mob instanceof EntityTriops;

    private static final Predicate<LivingEntity> NO_REPELLENT = mob -> !mob.hasEffect(AMEffectRegistry.MOSQUITO_REPELLENT.get());

    public float prevFlyProgress;

    public float flyProgress;

    public float prevShootProgress;

    public float shootProgress;

    public int shootingTicks;

    public int randomWingFlapTick = 0;

    private int flightTicks = 0;

    private int sickTicks = 0;

    private boolean prevFlying = false;

    private int spitCooldown = 0;

    private int loopSoundTick = 0;

    private int drinkTime = 0;

    public float prevMosquitoScale = 1.0F;

    private int repellentCheckTime = 0;

    private Vec3 fleePos = null;

    protected EntityCrimsonMosquito(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new EntityCrimsonMosquito.MoveHelperController(this);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    public boolean hasLuringLaviathan() {
        return this.f_19804_.get(LURING_LAVIATHAN) != -1;
    }

    public void onSpawnFromFly() {
        this.prevMosquitoScale = 0.2F;
        this.setShrink(false);
        this.setMosquitoScale(0.2F);
        this.setFromFly(true);
        for (int j = 0; j < 4; j++) {
            this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + this.f_19796_.nextDouble() / 2.0, this.m_20227_(0.5), this.m_20189_() + this.f_19796_.nextDouble() / 2.0, this.f_19796_.nextDouble() * 0.5 + 0.5, 0.0, 0.0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MOSQUITO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MOSQUITO_DIE.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.crimsonMosquitoSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        if (this.getBloodLevel() > 0) {
            return this.isFromFly() ? FROM_FLY_FULL_LOOT : FULL_LOOT;
        } else {
            return this.isFromFly() ? FROM_FLY_LOOT : super.m_7582_();
        }
    }

    public boolean canRiderInteract() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(2, new EntityCrimsonMosquito.FlyTowardsTarget(this));
        this.f_21345_.addGoal(2, new EntityCrimsonMosquito.FlyAwayFromTarget(this));
        this.f_21345_.addGoal(3, new EntityCrimsonMosquito.RandomFlyGoal(this));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityCrimsonMosquito.class, EntityWarpedMosco.class));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, 20, true, false, NO_REPELLENT));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, LivingEntity.class, 50, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CRIMSON_MOSQUITO_TARGETS)));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, EntityTriops.class, 16.0F, 1.3, 1.0));
    }

    public static boolean canMosquitoSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.below();
        boolean spawnBlock = worldIn.m_8055_(blockpos).m_60815_();
        return reason == MobSpawnType.SPAWNER || spawnBlock && worldIn.m_8055_(blockpos).m_60643_(worldIn, blockpos, typeIn) && m_219009_(worldIn, pos, randomIn) && m_217057_(AMEntityRegistry.CRIMSON_MOSQUITO.get(), worldIn, reason, pos, randomIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("FlightTicks", this.flightTicks);
        compound.putInt("SickTicks", this.sickTicks);
        compound.putFloat("MosquitoScale", this.getMosquitoScale());
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Shrinking", this.isShrinking());
        compound.putBoolean("IsFromFly", this.isFromFly());
        compound.putBoolean("Sick", this.isSick());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.flightTicks = compound.getInt("FlightTicks");
        this.sickTicks = compound.getInt("SickTicks");
        this.setMosquitoScale(compound.getFloat("MosquitoScale"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setShrink(compound.getBoolean("Shrinking"));
        this.setFromFly(compound.getBoolean("IsFromFly"));
        this.setSick(compound.getBoolean("Sick"));
    }

    private void spit(LivingEntity target) {
        if (!this.isSick()) {
            EntityMosquitoSpit llamaspitentity = new EntityMosquitoSpit(this.m_9236_(), this);
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20227_(0.3333333333333333) - llamaspitentity.m_20186_();
            double d2 = target.m_20189_() - this.m_20189_();
            float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
            llamaspitentity.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
            if (!this.m_20067_()) {
                this.m_146850_(GameEvent.PROJECTILE_SHOOT);
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.LLAMA_SPIT, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
            }
            if (this.getBloodLevel() > 0) {
                this.setBloodLevel(this.getBloodLevel() - 1);
            }
            this.m_9236_().m_7967_(llamaspitentity);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.m_6673_(source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && this.m_20201_() == source.getEntity().getRootVehicle()) {
            return super.m_6469_(source, amount * 0.333F);
        } else {
            if (this.flightTicks < 0) {
                this.flightTicks = 0;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.isAlive()) {
            this.m_8127_();
        } else {
            this.m_20334_(0.0, 0.0, 0.0);
            this.tick();
            if (this.m_20159_()) {
                Entity mount = this.m_20202_();
                if (mount instanceof LivingEntity livingEntity) {
                    this.f_20883_ = livingEntity.yBodyRot;
                    this.m_146922_(livingEntity.m_146908_());
                    this.f_20885_ = livingEntity.yHeadRot;
                    this.f_19859_ = livingEntity.yHeadRot;
                    float radius = 1.0F;
                    float angle = (float) (Math.PI / 180.0) * livingEntity.yBodyRot;
                    double extraX = (double) (1.0F * Mth.sin((float) Math.PI + angle));
                    double extraZ = (double) (1.0F * Mth.cos(angle));
                    this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) (mount.getEyeHeight() * 0.25F), mount.getY()), mount.getZ() + extraZ);
                    if (!mount.isAlive() || mount instanceof Player && ((Player) mount).isCreative()) {
                        this.m_6038_();
                    }
                    if (!this.m_9236_().isClientSide) {
                        if (this.drinkTime % 20 == 0 && this.m_6084_()) {
                            boolean mungus = AMConfig.warpedMoscoTransformation && mount instanceof EntityMungus && ((EntityMungus) mount).isWarpedMoscoReady();
                            if (mount.hurt(this.m_269291_().mobAttack(this), mungus ? 7.0F : 2.0F)) {
                                if (mungus) {
                                    ((EntityMungus) mount).disableExplosion();
                                }
                                boolean sick = this.isNonMungusWarpedTrigger(mount);
                                if (sick || mungus) {
                                    if (!this.isSick()) {
                                        for (ServerPlayer serverplayerentity : this.m_9236_().m_45976_(ServerPlayer.class, this.m_20191_().inflate(40.0, 25.0, 40.0))) {
                                            AMAdvancementTriggerRegistry.MOSQUITO_SICK.trigger(serverplayerentity);
                                        }
                                    }
                                    this.setSick(true);
                                    this.setFlying(false);
                                    this.flightTicks = -150 - this.f_19796_.nextInt(200);
                                }
                                this.m_146850_(GameEvent.EAT);
                                this.m_5496_(SoundEvents.HONEY_DRINK, this.m_6121_(), this.m_6100_());
                                this.setBloodLevel(this.getBloodLevel() + 1);
                                if (this.getBloodLevel() > 3) {
                                    this.m_6038_();
                                    AlexsMobs.sendMSGToAll(new MessageMosquitoDismount(this.m_19879_(), mount.getId()));
                                    this.setFlying(false);
                                    this.flightTicks = -15;
                                }
                            }
                        }
                        if (this.drinkTime > 81) {
                            this.drinkTime = -20 - this.f_19796_.nextInt(20);
                            this.m_6038_();
                            AlexsMobs.sendMSGToAll(new MessageMosquitoDismount(this.m_19879_(), mount.getId()));
                            this.setFlying(false);
                            this.flightTicks = -15;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(SHOOTING, false);
        this.f_19804_.define(SICK, false);
        this.f_19804_.define(BLOOD_LEVEL, 0);
        this.f_19804_.define(SHRINKING, false);
        this.f_19804_.define(FROM_FLY, false);
        this.f_19804_.define(MOSQUITO_SCALE, 1.0F);
        this.f_19804_.define(LURING_LAVIATHAN, -1);
        this.f_19804_.define(FLEEING_ENTITY, -1);
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public void setupShooting() {
        this.f_19804_.set(SHOOTING, true);
        this.shootingTicks = 5;
    }

    public int getLuringLaviathan() {
        return this.f_19804_.get(LURING_LAVIATHAN);
    }

    public void setLuringLaviathan(int lure) {
        this.f_19804_.set(LURING_LAVIATHAN, lure);
    }

    public int getFleeingEntityId() {
        return this.f_19804_.get(FLEEING_ENTITY);
    }

    public void setFleeingEntityId(int lure) {
        this.f_19804_.set(FLEEING_ENTITY, lure);
    }

    public int getBloodLevel() {
        return Math.min(this.f_19804_.get(BLOOD_LEVEL), 4);
    }

    public void setBloodLevel(int bloodLevel) {
        this.f_19804_.set(BLOOD_LEVEL, bloodLevel);
    }

    public boolean isShrinking() {
        return this.f_19804_.get(SHRINKING);
    }

    public boolean isFromFly() {
        return this.f_19804_.get(FROM_FLY);
    }

    public void setShrink(boolean shrink) {
        this.f_19804_.set(SHRINKING, shrink);
    }

    public void setFromFly(boolean fromFly) {
        this.f_19804_.set(FROM_FLY, fromFly);
    }

    public float getMosquitoScale() {
        return this.f_19804_.get(MOSQUITO_SCALE);
    }

    public void setMosquitoScale(float scale) {
        this.f_19804_.set(MOSQUITO_SCALE, scale);
    }

    public boolean isSick() {
        return this.f_19804_.get(SICK);
    }

    public void setSick(boolean shrink) {
        this.f_19804_.set(SICK, shrink);
    }

    @Override
    public void tick() {
        super.m_8119_();
        boolean shooting = this.f_19804_.get(SHOOTING);
        if (this.prevFlying != this.isFlying()) {
            this.m_6210_();
        }
        if (shooting) {
            if (this.shootProgress < 5.0F) {
                this.shootProgress++;
            }
        } else if (this.shootProgress > 0.0F) {
            this.shootProgress--;
        }
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20159_()) {
                this.setFlying(false);
            }
            if (this.isFlying()) {
                this.m_20242_(true);
            } else {
                this.m_20242_(false);
            }
            LivingEntity target = this.m_5448_();
            label207: if (this.getFleeingEntityId() != -1) {
                Entity fleeing = this.m_9236_().getEntity(this.getFleeingEntityId());
                if (fleeing instanceof LivingEntity living && REPELLENT.test(living) && this.m_20270_(living) < 20.0F) {
                    this.m_6710_(null);
                    this.m_6703_(null);
                    if (this.m_20159_()) {
                        this.m_8127_();
                    }
                    if (this.fleePos != null && !(this.fleePos.distanceTo(this.m_20182_()) < 3.0) && this.f_19796_.nextInt(40) != 0) {
                        this.setFlying(true);
                        this.f_21342_.setWantedPosition(this.fleePos.x, this.fleePos.y + 1.0, this.fleePos.z, 1.2F);
                        break label207;
                    }
                    Vec3 vec = LandRandomPos.getPosAway(this, 8, 4, fleeing.position());
                    if (vec != null) {
                        this.fleePos = vec;
                    }
                    break label207;
                }
                this.setFleeingEntityId(-1);
            } else {
                if (target == null && this.f_19797_ - this.repellentCheckTime > 50) {
                    this.repellentCheckTime = this.f_19797_;
                    LivingEntity closestRepel = null;
                    for (LivingEntity entity : this.m_9236_().m_6443_(LivingEntity.class, this.m_20191_().inflate(30.0), REPELLENT)) {
                        if (closestRepel == null || entity.m_20270_(this) < closestRepel.m_20270_(this)) {
                            closestRepel = entity;
                        }
                    }
                    if (closestRepel != null) {
                        this.setFleeingEntityId(closestRepel.m_19879_());
                    }
                }
                if (target != null && REPELLENT.test(target) && this.m_20270_(target) < 20.0F) {
                    this.setFleeingEntityId(target.m_19879_());
                }
            }
            if (this.hasLuringLaviathan()) {
                this.m_6710_(null);
                this.m_6703_(null);
                Entity entityx = this.m_9236_().getEntity(this.getLuringLaviathan());
                if (entityx instanceof EntityLaviathan && ((EntityLaviathan) entityx).isChilling()) {
                    Vec3 vec = ((EntityLaviathan) entityx).getLureMosquitoPos();
                    this.setFlying(true);
                    this.m_21391_(entityx, 10.0F, 10.0F);
                    this.m_21566_().setWantedPosition(vec.x, vec.y, vec.z, 0.7F);
                } else {
                    this.setLuringLaviathan(-1);
                }
            }
        }
        if (this.flyProgress == 0.0F && this.f_19796_.nextInt(200) == 0) {
            this.randomWingFlapTick = 5 + this.f_19796_.nextInt(15);
        }
        if (this.randomWingFlapTick > 0) {
            this.randomWingFlapTick--;
        }
        if (!this.m_9236_().isClientSide && this.m_20096_() && !this.isFlying() && (this.flightTicks >= 0 && this.f_19796_.nextInt(5) == 0 || this.m_5448_() != null)) {
            this.setFlying(true);
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_6853_(false);
            this.f_19812_ = true;
        }
        if (this.flightTicks < 0) {
            this.flightTicks++;
        }
        if (!this.m_9236_().isClientSide && this.isFlying()) {
            this.flightTicks++;
            if (this.flightTicks > 200 && (this.m_5448_() == null || !this.m_5448_().isAlive())) {
                BlockPos above = this.getGroundPosition(this.m_20183_().above());
                if (this.m_9236_().getFluidState(above).isEmpty() && !this.m_9236_().getBlockState(above).m_60795_()) {
                    this.m_20184_().add(0.0, -0.2, 0.0);
                    if (this.m_20096_()) {
                        this.setFlying(false);
                        this.flightTicks = -150 - this.f_19796_.nextInt(200);
                    }
                }
            }
        }
        this.prevMosquitoScale = this.getMosquitoScale();
        if (this.isShrinking()) {
            if (this.getMosquitoScale() > 0.4F) {
                this.setMosquitoScale(this.getMosquitoScale() - 0.1F);
            }
        } else if (this.getMosquitoScale() < 1.0F && !this.isSick()) {
            this.setMosquitoScale(this.getMosquitoScale() + 0.05F);
        }
        if (!this.m_9236_().isClientSide && this.shootingTicks > 0) {
            this.shootingTicks--;
            if (this.shootingTicks == 0) {
                if (this.m_5448_() != null && this.getBloodLevel() > 0) {
                    this.spit(this.m_5448_());
                }
                this.f_19804_.set(SHOOTING, false);
            }
        }
        if (this.isFlying()) {
            if (this.loopSoundTick == 0) {
                this.m_146850_(GameEvent.ENTITY_ROAR);
                this.m_5496_(AMSoundRegistry.MOSQUITO_LOOP.get(), this.m_6121_(), this.m_6100_());
            }
            this.loopSoundTick++;
            if (this.loopSoundTick > 100) {
                this.loopSoundTick = 0;
            }
        }
        if (this.m_20159_()) {
            if (this.drinkTime < 0) {
                this.drinkTime = 0;
            }
            this.drinkTime++;
        } else {
            this.drinkTime = 0;
        }
        this.prevFlyProgress = this.flyProgress;
        this.prevShootProgress = this.shootProgress;
        this.prevFlying = this.isFlying();
        if (this.isSick()) {
            this.sickTicks++;
            if (this.m_5448_() != null && !this.m_20159_()) {
                this.m_6710_(null);
            }
            if (this.sickTicks > 100) {
                this.setShrink(false);
                this.setMosquitoScale(this.getMosquitoScale() + 0.015F);
                if (this.sickTicks > 160) {
                    EntityWarpedMosco mosco = AMEntityRegistry.WARPED_MOSCO.get().create(this.m_9236_());
                    mosco.m_20359_(this);
                    if (!this.m_9236_().isClientSide) {
                        mosco.m_6518_((ServerLevelAccessor) this.m_9236_(), this.m_9236_().getCurrentDifficultyAt(this.m_20183_()), MobSpawnType.CONVERSION, null, null);
                    }
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 79);
                        this.m_9236_().m_7967_(mosco);
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 79) {
            for (int i = 0; i < 27; i++) {
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.EXPLOSION, this.m_20208_(1.6), this.m_20186_() + (double) (this.f_19796_.nextFloat() * 3.4F), this.m_20262_(1.6), d0, d1, d2);
            }
        } else {
            super.m_7822_(id);
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isFlying() ? FLIGHT_SIZE : super.m_6972_(poseIn);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.m_20096_() && !this.isFlying()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (item == AMItemRegistry.WARPED_MIXTURE.get() && !this.isSick()) {
            this.m_19983_(item.getCraftingRemainingItem(itemstack));
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setSick(true);
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    private BlockPos getGroundPosition(BlockPos radialPos) {
        while (radialPos.m_123342_() > 1 && this.m_9236_().m_46859_(radialPos)) {
            radialPos = radialPos.below();
        }
        return radialPos;
    }

    public boolean isNonMungusWarpedTrigger(Entity entity) {
        ResourceLocation mobtype = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return mobtype != null && !AMConfig.warpedMoscoMobTriggers.isEmpty() && AMConfig.warpedMoscoMobTriggers.contains(mobtype.toString());
    }

    public static class FlyAwayFromTarget extends Goal {

        private final EntityCrimsonMosquito parentEntity;

        private int spitCooldown = 0;

        private BlockPos shootPos = null;

        public FlyAwayFromTarget(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.parentEntity.isFlying() || this.parentEntity.getBloodLevel() <= 0 && this.parentEntity.drinkTime >= 0 || this.parentEntity.getFleeingEntityId() != -1) {
                return false;
            } else if (!this.parentEntity.m_20159_() && this.parentEntity.m_5448_() != null) {
                this.shootPos = this.getBlockInTargetsViewMosquito(this.parentEntity.m_5448_());
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null && (this.parentEntity.getBloodLevel() > 0 || this.parentEntity.drinkTime < 0) && this.parentEntity.isFlying() && !this.parentEntity.f_19862_;
        }

        @Override
        public void stop() {
            this.spitCooldown = 20;
        }

        @Override
        public void tick() {
            if (this.spitCooldown > 0) {
                this.spitCooldown--;
            }
            if (this.parentEntity.m_5448_() != null) {
                if (this.shootPos == null) {
                    this.shootPos = this.getBlockInTargetsViewMosquito(this.parentEntity.m_5448_());
                } else {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.shootPos.m_123341_() + 0.5, (double) this.shootPos.m_123342_() + 0.5, (double) this.shootPos.m_123343_() + 0.5, 1.0);
                    this.parentEntity.m_21391_(this.parentEntity.m_5448_(), 30.0F, 30.0F);
                    if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.shootPos)) < 2.5) {
                        if (this.spitCooldown == 0 && this.parentEntity.getBloodLevel() > 0) {
                            this.parentEntity.setupShooting();
                            this.spitCooldown = 20;
                        }
                        this.shootPos = null;
                    }
                }
            }
        }

        public BlockPos getBlockInTargetsViewMosquito(LivingEntity target) {
            float radius = (float) (4 + this.parentEntity.m_217043_().nextInt(5));
            float angle = (float) (Math.PI / 180.0) * (target.yHeadRot + 90.0F + (float) this.parentEntity.m_217043_().nextInt(180));
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos ground = AMBlockPos.fromCoords(target.m_20185_() + extraX, target.m_20186_() + 1.0, target.m_20189_() + extraZ);
            return this.parentEntity.m_20238_(Vec3.atCenterOf(ground)) > 30.0 && !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(ground)) && this.parentEntity.m_20238_(Vec3.atCenterOf(ground)) > 6.0 ? ground : this.parentEntity.m_20183_();
        }
    }

    public static class FlyTowardsTarget extends Goal {

        private final EntityCrimsonMosquito parentEntity;

        public FlyTowardsTarget(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.isFlying() && this.parentEntity.getBloodLevel() <= 0 && this.parentEntity.drinkTime >= 0 && this.parentEntity.getFleeingEntityId() == -1 ? !this.parentEntity.m_20159_() && this.parentEntity.m_5448_() != null && !this.isBittenByMosquito(this.parentEntity.m_5448_()) : false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.drinkTime >= 0 && this.parentEntity.getFleeingEntityId() == -1 && this.parentEntity.m_5448_() != null && !this.isBittenByMosquito(this.parentEntity.m_5448_()) && !this.parentEntity.f_19862_ && this.parentEntity.getBloodLevel() == 0 && this.parentEntity.isFlying() && this.parentEntity.m_21566_().hasWanted();
        }

        public boolean isBittenByMosquito(Entity entity) {
            for (Entity e : entity.getPassengers()) {
                if (e instanceof EntityCrimsonMosquito) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void stop() {
        }

        @Override
        public void tick() {
            if (this.parentEntity.m_5448_() != null) {
                this.parentEntity.m_21566_().setWantedPosition(this.parentEntity.m_5448_().m_20185_(), this.parentEntity.m_5448_().m_20186_(), this.parentEntity.m_5448_().m_20189_(), 1.0);
                if (this.parentEntity.m_20191_().inflate(0.3F, 0.3F, 0.3F).intersects(this.parentEntity.m_5448_().m_20191_()) && !this.isBittenByMosquito(this.parentEntity.m_5448_()) && this.parentEntity.drinkTime == 0) {
                    this.parentEntity.m_7998_(this.parentEntity.m_5448_(), true);
                    if (!this.parentEntity.m_9236_().isClientSide) {
                        AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.parentEntity.m_19879_(), this.parentEntity.m_5448_().m_19879_()));
                    }
                }
            }
        }
    }

    static class MoveHelperController extends MoveControl {

        private final EntityCrimsonMosquito parentEntity;

        public MoveHelperController(EntityCrimsonMosquito sunbird) {
            super(sunbird);
            this.parentEntity = sunbird;
        }

        @Override
        public void tick() {
            if (this.f_24978_ >= 1.0 && this.parentEntity.isSick()) {
                this.f_24978_ = 0.35;
            }
            if (this.parentEntity.isFlying()) {
                if (this.f_24981_ == MoveControl.Operation.STRAFE) {
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.length();
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(0.0, vector3d.scale(this.f_24978_ * 0.05 / d0).y(), 0.0));
                    float f = (float) this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED);
                    float f1 = (float) this.f_24978_ * f;
                    this.f_24979_ = 1.0F;
                    this.f_24980_ = 0.0F;
                    this.f_24974_.setSpeed(f1);
                    this.f_24974_.setZza(this.f_24979_);
                    this.f_24974_.setXxa(this.f_24980_);
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.length();
                    if (d0 < this.parentEntity.m_20191_().getSize()) {
                        this.f_24981_ = MoveControl.Operation.WAIT;
                        this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                    } else {
                        this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.05 / d0)));
                        if (this.parentEntity.m_5448_() == null) {
                            Vec3 vector3d1 = this.parentEntity.m_20184_();
                            this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        } else {
                            double d2 = this.parentEntity.m_5448_().m_20185_() - this.parentEntity.m_20185_();
                            double d1 = this.parentEntity.m_5448_().m_20189_() - this.parentEntity.m_20189_();
                            this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        }
                    }
                }
            } else {
                this.f_24981_ = MoveControl.Operation.WAIT;
                this.f_24974_.setSpeed(0.0F);
                this.f_24974_.setZza(0.0F);
                this.f_24974_.setXxa(0.0F);
            }
        }

        private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class RandomFlyGoal extends Goal {

        private final EntityCrimsonMosquito parentEntity;

        private BlockPos target = null;

        public RandomFlyGoal(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (!this.parentEntity.isFlying() || this.parentEntity.m_5448_() != null || this.parentEntity.hasLuringLaviathan() || this.parentEntity.getFleeingEntityId() != -1) {
                return false;
            } else if (movementcontroller.hasWanted() && this.target != null) {
                return false;
            } else {
                this.target = this.getBlockInViewMosquito();
                if (this.target != null) {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                }
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.parentEntity.isFlying() && this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) > 2.4 && this.parentEntity.m_21566_().hasWanted() && !this.parentEntity.f_19862_;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.target = this.getBlockInViewMosquito();
            }
            if (this.target != null) {
                this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewMosquito() {
            float radius = (float) (1 + this.parentEntity.m_217043_().nextInt(5));
            float neg = this.parentEntity.m_217043_().nextBoolean() ? 1.0F : -1.0F;
            float renderYawOffset = this.parentEntity.f_20883_;
            float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.parentEntity.m_217043_().nextFloat() * neg;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_() + 2.0, this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getGroundPosition(radialPos);
            int up = this.parentEntity.isSick() ? 2 : 6;
            BlockPos newPos = ground.above(1 + this.parentEntity.m_217043_().nextInt(up));
            return !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.parentEntity.m_20238_(Vec3.atCenterOf(newPos)) > 6.0 ? newPos : null;
        }
    }
}