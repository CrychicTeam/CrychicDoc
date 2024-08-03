package org.violetmoon.quark.content.mobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.module.WraithModule;

public class Wraith extends Zombie {

    public static final ResourceLocation LOOT_TABLE = new ResourceLocation("quark:entities/wraith");

    private static final EntityDataAccessor<String> IDLE_SOUND = SynchedEntityData.defineId(Wraith.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<String> HURT_SOUND = SynchedEntityData.defineId(Wraith.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<String> DEATH_SOUND = SynchedEntityData.defineId(Wraith.class, EntityDataSerializers.STRING);

    private static final String TAG_IDLE_SOUND = "IdleSound";

    private static final String TAG_HURT_SOUND = "HurtSound";

    private static final String TAG_DEATH_SOUND = "DeathSound";

    boolean aggroed = false;

    public Wraith(EntityType<? extends Wraith> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(IDLE_SOUND, "");
        this.f_19804_.define(HURT_SOUND, "");
        this.f_19804_.define(DEATH_SOUND, "");
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 35.0).add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ARMOR, 0.0).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource rand, @NotNull DifficultyInstance difficulty) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getSound(IDLE_SOUND);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.getSound(HURT_SOUND);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.getSound(DEATH_SOUND);
    }

    @Override
    public float getVoicePitch() {
        return this.f_19796_.nextFloat() * 0.1F + 0.75F;
    }

    public SoundEvent getSound(EntityDataAccessor<String> param) {
        ResourceLocation loc = new ResourceLocation(this.f_19804_.get(param));
        return BuiltInRegistries.SOUND_EVENT.get(loc);
    }

    @Override
    public void tick() {
        super.tick();
        double pad = 0.2;
        AABB aabb = this.m_20191_();
        double x = aabb.minX + Math.random() * (aabb.maxX - aabb.minX + pad * 2.0) - pad;
        double y = aabb.minY + Math.random() * (aabb.maxY - aabb.minY + pad * 2.0) - pad;
        double z = aabb.minZ + Math.random() * (aabb.maxZ - aabb.minZ + pad * 2.0) - pad;
        this.m_9236_().addParticle(ParticleTypes.MYCELIUM, x, y, z, 0.0, 0.0, 0.0);
        if (Math.random() < 0.1) {
            y = aabb.minY + 0.1;
            this.m_9236_().addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        boolean did = super.doHurtTarget(entityIn);
        if (did) {
            if (entityIn instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            }
            double dx = this.m_20185_() - entityIn.getX();
            double dz = this.m_20189_() - entityIn.getZ();
            Vec3 vec = new Vec3(dx, 0.0, dz).normalize().add(0.0, 0.5, 0.0).normalize().scale(0.85);
            this.m_20256_(vec);
        }
        return did;
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        int idx = this.f_19796_.nextInt(WraithModule.validWraithSounds.size());
        String sound = (String) WraithModule.validWraithSounds.get(idx);
        String[] split = sound.split("\\|");
        this.f_19804_.set(IDLE_SOUND, split[0]);
        this.f_19804_.set(HURT_SOUND, split[1]);
        this.f_19804_.set(DEATH_SOUND, split[2]);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("IdleSound", this.f_19804_.get(IDLE_SOUND));
        compound.putString("HurtSound", this.f_19804_.get(HURT_SOUND));
        compound.putString("DeathSound", this.f_19804_.get(DEATH_SOUND));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.f_19804_.set(IDLE_SOUND, compound.getString("IdleSound"));
        this.f_19804_.set(HURT_SOUND, compound.getString("HurtSound"));
        this.f_19804_.set(DEATH_SOUND, compound.getString("DeathSound"));
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    public void setBaby(boolean childZombie) {
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader worldIn) {
        BlockState state = worldIn.m_8055_(pos);
        return state.m_204336_(WraithModule.wraithSpawnableTag) ? 1.0F : 0.0F;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!super.hurt(source, amount)) {
            return false;
        } else {
            if (source != null && source.getDirectEntity() instanceof Player) {
                this.aggroed = true;
            }
            return this.m_9236_() instanceof ServerLevel;
        }
    }

    @Override
    public void setTarget(LivingEntity target) {
        if (this.aggroed) {
            super.m_6710_(target);
        }
    }

    @Override
    protected void handleAttributes(float difficulty) {
    }
}