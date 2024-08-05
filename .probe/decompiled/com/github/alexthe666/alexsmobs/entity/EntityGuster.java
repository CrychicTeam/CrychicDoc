package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityGuster extends Monster {

    private static final EntityDataAccessor<Integer> LIFT_ENTITY = SynchedEntityData.defineId(EntityGuster.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityGuster.class, EntityDataSerializers.INT);

    private LivingEntity liftedEntity;

    private int liftingTime = 0;

    private int maxLiftTime = 40;

    private int shootingTicks;

    public static final ResourceLocation RED_LOOT = new ResourceLocation("alexsmobs", "entities/guster_red");

    public static final ResourceLocation SOUL_LOOT = new ResourceLocation("alexsmobs", "entities/guster_soul");

    protected EntityGuster(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_274367_(1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 80;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.GUSTER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GUSTER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GUSTER_HURT.get();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.getVariant() == 2 ? SOUL_LOOT : (this.getVariant() == 1 ? RED_LOOT : super.m_7582_());
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    public static boolean canGusterSpawn(EntityType animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(BlockTags.SAND);
        return spawnBlock && (!AMConfig.limitGusterSpawnsToWeather || worldIn.getLevelData() != null && (worldIn.getLevelData().isThundering() || worldIn.getLevelData().isRaining()) || isBiomeNether(worldIn, pos));
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.gusterSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new EntityGuster.MeleeGoal());
        this.f_21345_.addGoal(1, new AnimalAIWanderRanged(this, 60, 1.0, 10, 7));
        this.f_21345_.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, true));
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void doPush(Entity entityIn) {
        if (this.getLiftedEntity() == null && this.liftingTime >= 0 && !(entityIn instanceof EntityGuster)) {
            this.setLiftedEntity(entityIn.getId());
            this.maxLiftTime = 30 + this.f_19796_.nextInt(30);
        }
    }

    public boolean hasLiftedEntity() {
        return this.f_19804_.get(LIFT_ENTITY) != 0;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(LIFT_ENTITY, 0);
        this.f_19804_.define(VARIANT, 0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                amount = (amount + 1.0F) / 3.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    private void spit(LivingEntity target) {
        EntitySandShot sghot = new EntitySandShot(this.m_9236_(), this);
        double d0 = target.m_20185_() - this.m_20185_();
        double d1 = target.m_20227_(0.3333333333333333) - sghot.m_20186_();
        double d2 = target.m_20189_() - this.m_20189_();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.35F;
        sghot.shoot(d0, d1 + (double) f, d2, 1.0F, 10.0F);
        sghot.setVariant(this.getVariant());
        if (!this.m_20067_()) {
            this.m_146850_(GameEvent.PROJECTILE_SHOOT);
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.SAND_BREAK, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
        }
        this.m_9236_().m_7967_(sghot);
    }

    @Override
    public double getEyeY() {
        return this.m_20186_() + 1.0;
    }

    @Nullable
    public Entity getLiftedEntity() {
        return !this.hasLiftedEntity() ? null : this.m_9236_().getEntity(this.f_19804_.get(LIFT_ENTITY));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (isBiomeNether(worldIn, this.m_20183_())) {
            this.setVariant(2);
        } else if (isBiomeRed(worldIn, this.m_20183_())) {
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        this.m_20301_(this.m_6062_());
        this.m_146926_(0.0F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void setLiftedEntity(int p_175463_1_) {
        this.f_19804_.set(LIFT_ENTITY, p_175463_1_);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Entity lifted = this.getLiftedEntity();
        if (lifted == null && !this.m_9236_().isClientSide && this.f_19797_ % 15 == 0) {
            List<ItemEntity> list = this.m_9236_().m_45976_(ItemEntity.class, this.m_20191_().inflate(0.8F));
            ItemEntity closestItem = null;
            for (int i = 0; i < list.size(); i++) {
                ItemEntity entity = (ItemEntity) list.get(i);
                if (entity.m_20096_() && (closestItem == null || this.m_20270_(closestItem) > this.m_20270_(entity))) {
                    closestItem = entity;
                }
            }
            if (closestItem != null) {
                this.setLiftedEntity(closestItem.m_19879_());
                this.maxLiftTime = 30 + this.f_19796_.nextInt(30);
            }
        }
        float f = (float) this.m_20186_();
        if (this.m_6084_()) {
            ParticleOptions type = this.getVariant() == 2 ? AMParticleRegistry.GUSTER_SAND_SPIN_SOUL.get() : (this.getVariant() == 1 ? AMParticleRegistry.GUSTER_SAND_SPIN_RED.get() : AMParticleRegistry.GUSTER_SAND_SPIN.get());
            for (int j = 0; j < 4; j++) {
                float f1 = (this.f_19796_.nextFloat() * 2.0F - 1.0F) * this.m_20205_() * 0.95F;
                float f2 = (this.f_19796_.nextFloat() * 2.0F - 1.0F) * this.m_20205_() * 0.95F;
                this.m_9236_().addParticle(type, this.m_20185_() + (double) f1, (double) f, this.m_20189_() + (double) f2, this.m_20185_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) + 0.2F, this.m_20189_());
            }
        }
        if (lifted != null && this.liftingTime >= 0) {
            this.liftingTime++;
            float resist = 1.0F;
            if (lifted instanceof LivingEntity) {
                resist = (float) Mth.clamp(1.0 - ((LivingEntity) lifted).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.0, 1.0);
            }
            float radius = 1.0F + (float) this.liftingTime * 0.05F;
            if (lifted instanceof ItemEntity) {
                radius = 0.2F + (float) this.liftingTime * 0.025F;
            }
            float angle = (float) this.liftingTime * -0.25F;
            double extraX = this.m_20185_() + (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = this.m_20189_() + (double) (radius * Mth.cos(angle));
            double d0 = (extraX - lifted.getX()) * (double) resist;
            double d1 = (extraZ - lifted.getZ()) * (double) resist;
            lifted.setDeltaMovement(d0, 0.1 * (double) resist, d1);
            lifted.hasImpulse = true;
            if (this.liftingTime > this.maxLiftTime) {
                this.setLiftedEntity(0);
                this.liftingTime = -20;
                this.maxLiftTime = 30 + this.f_19796_.nextInt(30);
            }
        } else if (this.liftingTime < 0) {
            this.liftingTime++;
        } else if (this.m_5448_() != null && this.m_20270_(this.m_5448_()) < this.m_20205_() + 1.0F && !(this.m_5448_() instanceof EntityGuster)) {
            this.setLiftedEntity(this.m_5448_().m_19879_());
            this.maxLiftTime = 30 + this.f_19796_.nextInt(30);
        }
        if (!this.m_9236_().isClientSide && this.shootingTicks >= 0) {
            if (this.shootingTicks <= 0) {
                if (this.m_5448_() != null && (lifted == null || lifted.getId() != this.m_5448_().m_19879_()) && this.m_6084_()) {
                    this.spit(this.m_5448_());
                }
                this.shootingTicks = 40 + this.f_19796_.nextInt(40);
            } else {
                this.shootingTicks--;
            }
        }
        Vec3 vector3d = this.m_20184_();
        if (!this.m_20096_() && vector3d.y < 0.0) {
            this.m_20256_(vector3d.multiply(1.0, 0.6, 1.0));
        }
    }

    public boolean isGooglyEyes() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("tweester");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    private static boolean isBiomeRed(LevelAccessor worldIn, BlockPos position) {
        return worldIn.m_204166_(position).is(AMTagRegistry.SPAWNS_RED_GUSTERS);
    }

    private static boolean isBiomeNether(LevelAccessor worldIn, BlockPos position) {
        return worldIn.m_204166_(position).is(AMTagRegistry.SPAWNS_SOUL_GUSTERS);
    }

    public static int getColorForVariant(int variant) {
        if (variant == 2) {
            return 5127475;
        } else {
            return variant == 1 ? 13000999 : 15975305;
        }
    }

    private class MeleeGoal extends Goal {

        public MeleeGoal() {
        }

        @Override
        public boolean canUse() {
            return EntityGuster.this.m_5448_() != null;
        }

        @Override
        public void tick() {
            Entity thrownEntity = EntityGuster.this.getLiftedEntity();
            if (EntityGuster.this.m_5448_() != null) {
                if (thrownEntity != null && thrownEntity.getId() == EntityGuster.this.m_5448_().m_19879_()) {
                    EntityGuster.this.m_21573_().stop();
                } else {
                    EntityGuster.this.m_21573_().moveTo(EntityGuster.this.m_5448_(), 1.25);
                }
            }
        }
    }
}