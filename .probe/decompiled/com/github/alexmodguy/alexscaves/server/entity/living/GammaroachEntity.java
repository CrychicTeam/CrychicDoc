package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GammaroachEntity extends PathfinderMob implements IAnimatedEntity {

    private static final Predicate<LivingEntity> IRRADIATED_TARGET = mob -> mob.hasEffect(ACEffectRegistry.IRRADIATED.get()) && !(mob instanceof RaycatEntity);

    private Animation currentAnimation;

    private int animationTick;

    public static final Animation ANIMATION_SPRAY = Animation.create(40);

    public static final Animation ANIMATION_RAM = Animation.create(25);

    private static final EntityDataAccessor<Integer> SPRAY_COOLDOWN = SynchedEntityData.defineId(GammaroachEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FED = SynchedEntityData.defineId(GammaroachEntity.class, EntityDataSerializers.BOOLEAN);

    public GammaroachEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new GammaroachEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 20, false, true, IRRADIATED_TARGET));
    }

    public static boolean isValidLightLevel(ServerLevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource) {
        if (levelAccessor.m_45517_(LightLayer.SKY, blockPos) > randomSource.nextInt(32)) {
            return false;
        } else {
            int lvt_3_1_ = levelAccessor.getLevel().m_46470_() ? levelAccessor.m_46849_(blockPos, 10) : levelAccessor.m_46803_(blockPos);
            return lvt_3_1_ <= randomSource.nextInt(8);
        }
    }

    public static boolean canMonsterSpawnInLight(EntityType<GammaroachEntity> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return isValidLightLevel(levelAccessor, blockPos, randomSource) && m_217057_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
    }

    public static <T extends Mob> boolean checkGammaroachSpawnRules(EntityType<GammaroachEntity> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SPRAY_COOLDOWN, 0);
        this.f_19804_.define(FED, false);
    }

    public int getSprayCooldown() {
        return this.f_19804_.get(SPRAY_COOLDOWN);
    }

    public void setSprayCooldown(int time) {
        this.f_19804_.set(SPRAY_COOLDOWN, time);
    }

    public boolean isFed() {
        return this.f_19804_.get(FED);
    }

    public void setFed(boolean fed) {
        this.f_19804_.set(FED, fed);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.MAX_HEALTH, 14.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.IRRADIATED.get();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.isFed();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return 0.5F - (float) Math.max(worldIn.m_45517_(LightLayer.BLOCK, pos), worldIn.m_45517_(LightLayer.SKY, pos));
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.getSprayCooldown() > 0) {
            this.setSprayCooldown(this.getSprayCooldown() - 1);
        }
        if (this.getAnimation() == ANIMATION_SPRAY) {
            if (this.getAnimationTick() == 10) {
                AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_() + 0.2F, this.m_20189_());
                areaeffectcloud.setParticle(ACParticleRegistry.GAMMAROACH.get());
                areaeffectcloud.setFixedColor(7853582);
                areaeffectcloud.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 2000));
                areaeffectcloud.setRadius(2.3F);
                areaeffectcloud.setDuration(200);
                areaeffectcloud.setWaitTime(10);
                areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
                this.m_9236_().m_7967_(areaeffectcloud);
            } else if (this.getAnimationTick() >= 10 && this.getAnimationTick() <= 30) {
                Vec3 randomOffset = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)).normalize().scale(1.0).add(this.m_146892_());
                this.m_9236_().addParticle(ACParticleRegistry.GAMMAROACH.get(), this.m_20208_(2.0), this.m_20188_(), this.m_20262_(2.0), randomOffset.x, randomOffset.y + 0.23, randomOffset.z);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
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
        return new Animation[] { ANIMATION_SPRAY, ANIMATION_RAM };
    }

    public void triggerSpraying() {
        if (this.getSprayCooldown() <= 0 && this.getAnimation() == NO_ANIMATION) {
            this.m_216990_(ACSoundRegistry.GAMMAROACH_SPRAY.get());
            this.setAnimation(ANIMATION_SPRAY);
            this.setSprayCooldown(10000 + this.f_19796_.nextInt(24000));
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageAmount) {
        boolean prev = super.m_6469_(damageSource, damageAmount);
        if (prev && damageSource.getEntity() instanceof LivingEntity living && !living.hasEffect(ACEffectRegistry.IRRADIATED.get())) {
            this.triggerSpraying();
        }
        return prev;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.getAnimation() == ANIMATION_RAM || this.getAnimation() == ANIMATION_SPRAY) {
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setSprayCooldown(compound.getInt("SprayCooldown"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("SprayCooldown", this.getSprayCooldown());
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, flying ? this.m_20186_() - this.f_19855_ : 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getStepHeight() {
        return 1.1F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult prev = super.m_6071_(player, hand);
        if (prev != InteractionResult.SUCCESS) {
            ItemStack itemStack = player.m_21120_(hand);
            if (itemStack.is(ACItemRegistry.SPELUNKIE.get()) && (!this.m_9236_().isClientSide && this.m_5448_() == player || !this.isFed())) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.setFed(true);
                this.m_6703_(null);
                this.m_6710_(null);
                this.m_9236_().broadcastEntityEvent(this, (byte) 49);
                return InteractionResult.SUCCESS;
            }
        }
        return prev;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 49) {
            ItemStack itemstack = new ItemStack(ACItemRegistry.SPELUNKIE.get());
            for (int i = 0; i < 8; i++) {
                Vec3 headPos = new Vec3(0.0, 0.1, 0.5).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.m_20185_() + headPos.x, this.m_20227_(0.5) + headPos.y, this.m_20189_() + headPos.z, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.15F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
            }
        } else {
            super.m_7822_(b);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.GAMMAROACH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.GAMMAROACH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.GAMMAROACH_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.GAMMAROACH_STEP.get(), 1.0F, 1.0F);
        }
    }

    private class MeleeGoal extends Goal {

        private int checkForMobsTime = 0;

        private LivingEntity pickupMonster = null;

        public MeleeGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity target = GammaroachEntity.this.m_5448_();
            return target != null && target.isAlive();
        }

        @Override
        public void stop() {
            if (this.pickupMonster != null) {
                if (this.pickupMonster.m_20365_(GammaroachEntity.this)) {
                    this.pickupMonster.stopRiding();
                }
                this.pickupMonster = null;
                this.checkForMobsTime = 20;
            }
        }

        @Override
        public void tick() {
            this.checkForMobsTime--;
            LivingEntity target = GammaroachEntity.this.m_5448_();
            if (target != null && target.isAlive()) {
                if (this.checkForMobsTime < 0) {
                    this.checkForMobsTime = 120 + GammaroachEntity.this.f_19796_.nextInt(100);
                    Predicate<Entity> monsterAway = animal -> animal instanceof Enemy && !(animal instanceof GammaroachEntity) && animal.distanceTo(target) > 5.0F && !animal.isPassenger();
                    List<Mob> list = GammaroachEntity.this.m_9236_().m_6443_(Mob.class, GammaroachEntity.this.m_20191_().inflate(30.0, 12.0, 30.0), EntitySelector.NO_SPECTATORS.and(monsterAway));
                    list.sort(Comparator.comparingDouble(GammaroachEntity.this::m_20280_));
                    if (!list.isEmpty()) {
                        this.pickupMonster = (LivingEntity) list.get(0);
                    }
                }
                if (this.pickupMonster == null || this.pickupMonster.m_20365_(GammaroachEntity.this)) {
                    GammaroachEntity.this.m_21573_().moveTo(target, 1.0);
                    GammaroachEntity.this.m_21391_(target, 180.0F, 30.0F);
                    if (GammaroachEntity.this.m_20270_(target) < 1.5F + target.m_20205_()) {
                        if (GammaroachEntity.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            GammaroachEntity.this.setAnimation(GammaroachEntity.ANIMATION_RAM);
                        } else if (GammaroachEntity.this.getAnimation() == GammaroachEntity.ANIMATION_RAM && GammaroachEntity.this.getAnimationTick() > 8 && GammaroachEntity.this.getAnimationTick() < 15) {
                            GammaroachEntity.this.m_216990_(ACSoundRegistry.GAMMAROACH_ATTACK.get());
                            target.hurt(GammaroachEntity.this.m_269291_().mobAttack(GammaroachEntity.this), (float) GammaroachEntity.this.m_21133_(Attributes.ATTACK_DAMAGE));
                        }
                        if (this.pickupMonster != null) {
                            this.pickupMonster.stopRiding();
                            this.pickupMonster = null;
                        }
                    }
                } else if (this.pickupMonster.isAlive() && !this.pickupMonster.m_20159_()) {
                    GammaroachEntity.this.m_21573_().moveTo(this.pickupMonster, 1.0);
                    GammaroachEntity.this.m_21391_(this.pickupMonster, 180.0F, 30.0F);
                    if (GammaroachEntity.this.m_20270_(this.pickupMonster) < 1.5F + this.pickupMonster.m_20205_()) {
                        this.pickupMonster.m_7998_(GammaroachEntity.this, true);
                        this.pickupMonster.hurt(GammaroachEntity.this.m_269291_().cactus(), 1.0F);
                    }
                } else {
                    this.pickupMonster = null;
                }
            }
        }
    }
}