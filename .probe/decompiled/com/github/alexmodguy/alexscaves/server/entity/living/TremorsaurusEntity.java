package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetUntamedGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.TremorsaurusMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import com.github.alexmodguy.alexscaves.server.entity.util.ShakesScreen;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.animation.LegSolver;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.ITallWalker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TremorsaurusEntity extends DinosaurEntity implements KeybindUsingMount, IAnimatedEntity, ShakesScreen, ITallWalker {

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> HELD_MOB_ID = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TAME_ATTEMPTS = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> METER_AMOUNT = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.FLOAT);

    public final LegSolver legSolver = new LegSolver(new LegSolver.Leg(-0.45F, 0.75F, 1.0F, false), new LegSolver.Leg(-0.45F, -0.75F, 1.0F, false));

    private Animation currentAnimation;

    private int animationTick;

    private float prevScreenShakeAmount;

    private float screenShakeAmount;

    private int lastScareTimestamp = 0;

    private boolean hasRunningAttributes = false;

    private int roarCooldown = 0;

    public static final Animation ANIMATION_SNIFF = Animation.create(30);

    public static final Animation ANIMATION_SPEAK = Animation.create(15);

    public static final Animation ANIMATION_ROAR = Animation.create(55);

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public static final Animation ANIMATION_SHAKE_PREY = Animation.create(40);

    private double lastStompX = 0.0;

    private double lastStompZ = 0.0;

    private int roarScatterTime = 0;

    private Entity riderHitEntity = null;

    public TremorsaurusEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new TremorsaurusMeleeGoal(this));
        this.f_21345_.addGoal(3, new AnimalFollowOwnerGoal(this, 1.2, 5.0F, 2.0F, false) {

            @Override
            public boolean shouldFollow() {
                return TremorsaurusEntity.this.getCommand() == 2;
            }
        });
        this.f_21345_.addGoal(4, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(5, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.1, Ingredient.of(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(), ACBlockRegistry.DINOSAUR_CHOP.get()), false));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 30));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, TremorsaurusEntity.class));
        this.f_21346_.addGoal(2, new MobTargetClosePlayers(this, 50, 8.0F));
        this.f_21346_.addGoal(3, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new MobTargetUntamedGoal(this, GrottoceratopsEntity.class, 100, true, false, null));
        this.f_21346_.addGoal(4, new MobTargetUntamedGoal(this, SubterranodonEntity.class, 50, true, false, null));
        this.f_21346_.addGoal(5, new MobTargetUntamedGoal(this, RelicheirusEntity.class, 250, true, false, null));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(RUNNING, false);
        this.f_19804_.define(HELD_MOB_ID, -1);
        this.f_19804_.define(TAME_ATTEMPTS, 0);
        this.f_19804_.define(METER_AMOUNT, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 14.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 150.0).add(Attributes.ARMOR, 8.0);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevScreenShakeAmount = this.screenShakeAmount;
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.m_21529_());
        this.legSolver.update(this, this.f_20883_, this.getScale());
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.screenShakeAmount > 0.0F) {
            this.screenShakeAmount = Math.max(0.0F, this.screenShakeAmount - 0.34F);
        }
        if (this.m_20096_() && !this.isInFluidType() && this.f_267362_.speed() > 0.1F && !this.m_6162_()) {
            float f = (float) Math.cos((double) (this.f_267362_.position() * 0.8F - 1.5F));
            if ((double) Math.abs(f) < 0.2) {
                if ((double) this.screenShakeAmount <= 0.3) {
                    this.m_5496_(ACSoundRegistry.TREMORSAURUS_STOMP.get(), 2.0F, 1.0F);
                    this.shakeWater();
                }
                this.screenShakeAmount = 1.0F;
            }
        }
        if (this.f_19797_ % 100 == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(2.0F);
        }
        if (this.isRunning() && !this.hasRunningAttributes) {
            this.hasRunningAttributes = true;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
        }
        if (!this.isRunning() && this.hasRunningAttributes) {
            this.hasRunningAttributes = false;
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playRoarSound();
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() >= 5 && this.getAnimationTick() <= 40 && !this.m_6162_()) {
            this.screenShakeAmount = 1.0F;
            this.roarScatterTime = 30;
            if (this.getAnimationTick() % 5 == 0 && this.m_9236_().isClientSide) {
                this.shakeWater();
            }
        }
        if (this.roarScatterTime > 0) {
            this.roarScatterTime--;
            this.scareMobs();
        }
        if (this.getAnimation() == ANIMATION_SPEAK && this.getAnimationTick() == 5) {
            this.actuallyPlayAmbientSound();
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20184_().horizontalDistance() < 0.05 && this.getAnimation() == NO_ANIMATION && !this.isDancing() && !this.m_21825_()) {
                if (this.f_19796_.nextInt(180) == 0) {
                    this.setAnimation(ANIMATION_SNIFF);
                }
                if (this.f_19796_.nextInt(600) == 0 && !this.m_20160_()) {
                    this.tryRoar();
                }
            }
            boolean held = false;
            if (this.riderHitEntity != null && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() > 10 && this.getAnimationTick() <= 12 && this.m_142582_(this.riderHitEntity) && (double) this.m_20270_(this.riderHitEntity) < (double) (this.m_20205_() + this.riderHitEntity.getBbWidth()) + 2.0) {
                this.riderHitEntity.hurt(this.riderHitEntity.damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                if (this.riderHitEntity instanceof LivingEntity living) {
                    living.knockback(0.5, this.m_20185_() - this.riderHitEntity.getX(), this.m_20189_() - this.riderHitEntity.getZ());
                }
                this.riderHitEntity = null;
            }
            LivingEntity target = this.riderHitEntity instanceof LivingEntity ? (LivingEntity) this.riderHitEntity : this.m_5448_();
            if (target != null && target.isAlive() && target.m_20270_(this) < (this.m_20160_() ? 10.0F : 5.5F) && this.getAnimation() == ANIMATION_SHAKE_PREY && this.getAnimationTick() <= 35) {
                held = true;
                this.setHeldMobId(target.m_19879_());
            }
            if (!held && this.getHeldMobId() != -1) {
                this.setHeldMobId(-1);
                this.m_216990_(ACSoundRegistry.TREMORSAURUS_THROW.get());
                this.riderHitEntity = null;
            }
        } else {
            Player player = AlexsCaves.PROXY.getClientSidePlayer();
            if (player != null && player.m_20365_(this)) {
                if (AlexsCaves.PROXY.isKeyDown(2) && this.getMeterAmount() >= 1.0F) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 2));
                }
                if (AlexsCaves.PROXY.isKeyDown(3) && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 3));
                }
            }
        }
        if (this.m_20160_()) {
            if (this.getMeterAmount() < 1.0F) {
                this.setMeterAmount(Math.min(this.getMeterAmount() + 0.0035F, 1.0F));
            }
        } else {
            this.setMeterAmount(0.0F);
        }
        if (this.getAnimation() == ANIMATION_SHAKE_PREY && this.getHeldMobId() != -1) {
            Entity entity = this.m_9236_().getEntity(this.getHeldMobId());
            if (entity != null) {
                if (this.getAnimationTick() <= 35) {
                    Vec3 shakePreyPos = this.getShakePreyPos();
                    Vec3 minus = new Vec3(shakePreyPos.x - entity.getX(), shakePreyPos.y - entity.getY(), shakePreyPos.z - entity.getZ());
                    entity.setDeltaMovement(minus);
                    if (this.getAnimationTick() % 10 == 0) {
                        entity.hurt(this.m_269291_().mobAttack(this), (float) (5 + this.m_217043_().nextInt(2)));
                    }
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.6F));
                }
            }
        }
        if (this.roarCooldown > 0) {
            this.roarCooldown--;
        }
        this.lastStompX = this.m_20185_();
        this.lastStompZ = this.m_20189_();
    }

    private void playRoarSound() {
        if (this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.TREMORSAURUS_ROAR.get(), 1.0F, 1.5F);
        } else {
            this.m_5496_(ACSoundRegistry.TREMORSAURUS_ROAR.get(), 4.0F, 1.0F);
        }
    }

    private void scareMobs() {
        if (this.f_19797_ - this.lastScareTimestamp > 3) {
            this.lastScareTimestamp = this.f_19797_;
        }
        for (LivingEntity e : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(30.0, 10.0, 30.0))) {
            if (!e.m_6095_().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR) && !this.m_7307_(e)) {
                if (e instanceof PathfinderMob) {
                    PathfinderMob mob = (PathfinderMob) e;
                    if (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose()) {
                        mob.m_6710_(null);
                        mob.m_6703_(null);
                        if (mob.m_20096_()) {
                            Vec3 randomShake = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), 0.0, (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.1F);
                            mob.m_20256_(mob.m_20184_().multiply(0.7F, 1.0, 0.7F).add(randomShake));
                        }
                        if (this.lastScareTimestamp == this.f_19797_) {
                            mob.m_21573_().stop();
                        }
                        if (mob.m_21573_().isDone()) {
                            Vec3 vec = LandRandomPos.getPosAway(mob, 15, 7, this.m_20182_());
                            if (vec != null) {
                                mob.m_21573_().moveTo(vec.x, vec.y, vec.z, 2.0);
                            }
                        }
                    }
                }
                if (this.m_21824_()) {
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0, true, true));
                }
            }
        }
    }

    private void shakeWater() {
        if (this.m_9236_().isClientSide) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int radius = 8;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        mutableBlockPos.set(this.m_20185_() + (double) x, this.m_20186_() + 5.0, this.m_20189_() + (double) z);
                        while (mutableBlockPos.m_123342_() > this.m_9236_().m_141937_() && this.m_9236_().getBlockState(mutableBlockPos).m_60795_()) {
                            mutableBlockPos.move(Direction.DOWN);
                        }
                        float water = this.getWaterLevelForBlock(this.m_9236_(), mutableBlockPos);
                        if (water > 0.0F) {
                            this.m_9236_().addParticle(ACParticleRegistry.WATER_TREMOR.get(), (double) ((float) mutableBlockPos.m_123341_() + 0.5F), (double) ((float) mutableBlockPos.m_123342_() + water) + 0.01, (double) ((float) mutableBlockPos.m_123343_() + 0.5F), 0.0, 0.0, 0.0);
                        }
                    }
                }
            }
        }
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean bool) {
        this.f_19804_.set(RUNNING, bool);
    }

    public void setHeldMobId(int i) {
        this.f_19804_.set(HELD_MOB_ID, i);
    }

    public int getHeldMobId() {
        return this.f_19804_.get(HELD_MOB_ID);
    }

    public void setTameAttempts(int i) {
        this.f_19804_.set(TAME_ATTEMPTS, i);
    }

    public int getTameAttempts() {
        return this.f_19804_.get(TAME_ATTEMPTS);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0, 3.0, 3.0);
    }

    public Entity getHeldMob() {
        int id = this.getHeldMobId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public float getScreenShakeAmount(float partialTicks) {
        return this.prevScreenShakeAmount + (this.screenShakeAmount - this.prevScreenShakeAmount) * partialTicks;
    }

    public Vec3 getShakePreyPos() {
        Vec3 jaw = new Vec3(0.0, -0.75, 3.0);
        if (this.getAnimation() == ANIMATION_SHAKE_PREY) {
            if (this.getAnimationTick() <= 5) {
                jaw = jaw.subtract(0.0, (double) (1.5F * ((float) this.getAnimationTick() / 5.0F)), 0.0);
            } else if (this.getAnimationTick() < 35) {
                jaw = jaw.yRot(0.8F * (float) Math.cos((double) ((float) this.f_19797_ * 0.6F)));
            }
        }
        Vec3 head = jaw.xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0));
        return this.m_146892_().add(head);
    }

    public void tryRoar() {
        if (this.roarCooldown == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ROAR);
            this.roarCooldown = 200 + this.f_19796_.nextInt(200);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ACEntityRegistry.TREMORSAURUS.get().create(level);
    }

    @Override
    public boolean wantsToAttack(LivingEntity living, LivingEntity owner) {
        if (living instanceof TremorsaurusEntity tremorsaurus && (tremorsaurus.getTameAttempts() > 0 || tremorsaurus.m_21023_(ACEffectRegistry.STUNNED.get()))) {
            return false;
        }
        return super.m_7757_(living, owner);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.getAnimation() == ANIMATION_ROAR || this.getAnimation() == ANIMATION_SHAKE_PREY) {
            vec3d = Vec3.ZERO;
        }
        super.travel(vec3d);
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.25F : 1.0F;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.lastStompX, 0.0, this.m_20189_() - this.lastStompZ);
        float walkSpeed = 4.0F;
        if (this.m_20160_()) {
            walkSpeed = 1.5F;
        } else if (this.isRunning()) {
            walkSpeed = 2.0F;
        }
        float f2 = Math.min(f1 * walkSpeed, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
            this.setAnimation(ANIMATION_SPEAK);
        }
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean playing) {
        this.onClientPlayMusicDisc(this.m_19879_(), pos, playing);
    }

    public void actuallyPlayAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.m_5496_(soundevent, this.m_6121_(), this.m_6100_());
        }
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
        return new Animation[] { ANIMATION_SNIFF, ANIMATION_SPEAK, ANIMATION_ROAR, ANIMATION_BITE, ANIMATION_SHAKE_PREY };
    }

    private float getWaterLevelForBlock(Level level, BlockPos pos) {
        BlockState state = this.m_9236_().getBlockState(pos);
        if (state.m_60713_(Blocks.WATER_CAULDRON)) {
            return (6.0F + (float) ((Integer) state.m_61143_(LayeredCauldronBlock.LEVEL)).intValue() * 3.0F) / 16.0F;
        } else {
            return this.f_19796_.nextFloat() < 0.33F && state.m_60819_().is(FluidTags.WATER) ? state.m_60819_().getHeight(level, pos) : 0.0F;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TameAttempts", this.getTameAttempts());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTameAttempts(compound.getInt("TameAttempts"));
    }

    @Override
    public BlockState createEggBlockState() {
        return ACBlockRegistry.TREMORSAURUS_EGG.get().defaultBlockState();
    }

    public float getStepHeight() {
        return 1.1F;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
        return new Vec3((double) (player.f_20900_ * 0.35F), 0.0, (double) (player.f_20902_ * 0.8F * f));
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.m_5616_(player.m_6080_());
            this.m_6710_(null);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    public boolean tamesFromHatching() {
        return true;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            Vec3 seatOffset = new Vec3(0.0, 0.1F, 0.6F).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            passenger.setYBodyRot(this.f_20883_);
            passenger.fallDistance = 0.0F;
            this.clampRotation(living, 105.0F);
            float heightBackLeft = this.legSolver.legs[0].getHeight(1.0F);
            float heightBackRight = this.legSolver.legs[1].getHeight(1.0F);
            float maxLegSolverHeight = (1.0F - ACMath.smin(1.0F - heightBackLeft, 1.0F - heightBackRight, 0.1F)) * 0.8F;
            moveFunction.accept(passenger, this.m_20185_() + seatOffset.x, this.m_20186_() + seatOffset.y + this.m_6048_() - (double) maxLegSolverHeight, this.m_20189_() + seatOffset.z);
            return;
        }
        super.m_19956_(passenger, moveFunction);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        return new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
    }

    @Override
    public boolean hasRidingMeter() {
        return true;
    }

    @Override
    public float getMeterAmount() {
        return this.f_19804_.get(METER_AMOUNT);
    }

    public void setMeterAmount(float roarPower) {
        this.f_19804_.set(METER_AMOUNT, roarPower);
    }

    @Override
    public boolean onFeedMixture(ItemStack itemStack, Player player) {
        if (itemStack.is(ACItemRegistry.SERENE_SALAD.get()) && this.m_21023_(ACEffectRegistry.STUNNED.get())) {
            this.m_21195_(ACEffectRegistry.STUNNED.get());
            AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(this.m_19879_(), this.m_19879_(), 3, 0, true));
            if (!this.m_9236_().isClientSide) {
                this.setTameAttempts(this.getTameAttempts() + 1);
                if ((this.getTameAttempts() <= 3 || this.m_217043_().nextInt(2) != 0) && this.getTameAttempts() <= 8) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                } else {
                    this.m_21828_(player);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return this.m_21824_() && (stack.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().asItem()) || stack.is(ACBlockRegistry.DINOSAUR_CHOP.get().asItem()));
    }

    @Override
    public boolean canOwnerMount(Player player) {
        return !this.m_6162_();
    }

    @Override
    public boolean canOwnerCommand(Player ownerPlayer) {
        return ownerPlayer.m_6144_();
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this)) {
            if (type == 2 && this.getMeterAmount() >= 1.0F && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
                this.f_20883_ = keyPresser.getYHeadRot();
                this.m_146922_(keyPresser.getYHeadRot());
                this.setAnimation(ANIMATION_ROAR);
                this.setMeterAmount(0.0F);
            }
            if (type == 3 && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
                HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(keyPresser, entity -> !entity.is(this) && !this.m_7307_(entity), 10.0);
                this.m_5616_(keyPresser.getYHeadRot());
                this.m_146926_(keyPresser.getXRot());
                boolean flag = false;
                if (!(hitresult instanceof EntityHitResult entityHitResult)) {
                    this.riderHitEntity = null;
                } else {
                    this.riderHitEntity = entityHitResult.getEntity();
                    if (this.m_217043_().nextBoolean() && this.riderHitEntity.getBbWidth() < 2.0F || this.riderHitEntity instanceof FlyingAnimal) {
                        flag = true;
                    }
                }
                this.setAnimation(flag ? ANIMATION_SHAKE_PREY : ANIMATION_BITE);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.TREMORSAURUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.TREMORSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.TREMORSAURUS_DEATH.get();
    }

    @Override
    public int getMaxNavigableDistanceToGround() {
        return 2;
    }
}