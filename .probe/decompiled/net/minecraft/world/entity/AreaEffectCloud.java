package net.minecraft.world.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.slf4j.Logger;

public class AreaEffectCloud extends Entity implements TraceableEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int TIME_BETWEEN_APPLICATIONS = 5;

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);

    private static final float MAX_RADIUS = 32.0F;

    private static final float MINIMAL_RADIUS = 0.5F;

    private static final float DEFAULT_RADIUS = 3.0F;

    public static final float DEFAULT_WIDTH = 6.0F;

    public static final float HEIGHT = 0.5F;

    private Potion potion = Potions.EMPTY;

    private final List<MobEffectInstance> effects = Lists.newArrayList();

    private final Map<Entity, Integer> victims = Maps.newHashMap();

    private int duration = 600;

    private int waitTime = 20;

    private int reapplicationDelay = 20;

    private boolean fixedColor;

    private int durationOnUse;

    private float radiusOnUse;

    private float radiusPerTick;

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    public AreaEffectCloud(EntityType<? extends AreaEffectCloud> entityTypeExtendsAreaEffectCloud0, Level level1) {
        super(entityTypeExtendsAreaEffectCloud0, level1);
        this.f_19794_ = true;
    }

    public AreaEffectCloud(Level level0, double double1, double double2, double double3) {
        this(EntityType.AREA_EFFECT_CLOUD, level0);
        this.m_6034_(double1, double2, double3);
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_COLOR, 0);
        this.m_20088_().define(DATA_RADIUS, 3.0F);
        this.m_20088_().define(DATA_WAITING, false);
        this.m_20088_().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(float float0) {
        if (!this.m_9236_().isClientSide) {
            this.m_20088_().set(DATA_RADIUS, Mth.clamp(float0, 0.0F, 32.0F));
        }
    }

    @Override
    public void refreshDimensions() {
        double $$0 = this.m_20185_();
        double $$1 = this.m_20186_();
        double $$2 = this.m_20189_();
        super.refreshDimensions();
        this.m_6034_($$0, $$1, $$2);
    }

    public float getRadius() {
        return this.m_20088_().get(DATA_RADIUS);
    }

    public void setPotion(Potion potion0) {
        this.potion = potion0;
        if (!this.fixedColor) {
            this.updateColor();
        }
    }

    private void updateColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.m_20088_().set(DATA_COLOR, 0);
        } else {
            this.m_20088_().set(DATA_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }
    }

    public void addEffect(MobEffectInstance mobEffectInstance0) {
        this.effects.add(mobEffectInstance0);
        if (!this.fixedColor) {
            this.updateColor();
        }
    }

    public int getColor() {
        return this.m_20088_().get(DATA_COLOR);
    }

    public void setFixedColor(int int0) {
        this.fixedColor = true;
        this.m_20088_().set(DATA_COLOR, int0);
    }

    public ParticleOptions getParticle() {
        return this.m_20088_().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions particleOptions0) {
        this.m_20088_().set(DATA_PARTICLE, particleOptions0);
    }

    protected void setWaiting(boolean boolean0) {
        this.m_20088_().set(DATA_WAITING, boolean0);
    }

    public boolean isWaiting() {
        return this.m_20088_().get(DATA_WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int int0) {
        this.duration = int0;
    }

    @Override
    public void tick() {
        super.tick();
        boolean $$0 = this.isWaiting();
        float $$1 = this.getRadius();
        if (this.m_9236_().isClientSide) {
            if ($$0 && this.f_19796_.nextBoolean()) {
                return;
            }
            ParticleOptions $$2 = this.getParticle();
            int $$3;
            float $$4;
            if ($$0) {
                $$3 = 2;
                $$4 = 0.2F;
            } else {
                $$3 = Mth.ceil((float) Math.PI * $$1 * $$1);
                $$4 = $$1;
            }
            for (int $$7 = 0; $$7 < $$3; $$7++) {
                float $$8 = this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                float $$9 = Mth.sqrt(this.f_19796_.nextFloat()) * $$4;
                double $$10 = this.m_20185_() + (double) (Mth.cos($$8) * $$9);
                double $$11 = this.m_20186_();
                double $$12 = this.m_20189_() + (double) (Mth.sin($$8) * $$9);
                double $$14;
                double $$15;
                double $$16;
                if ($$2.getType() == ParticleTypes.ENTITY_EFFECT) {
                    int $$13 = $$0 && this.f_19796_.nextBoolean() ? 16777215 : this.getColor();
                    $$14 = (double) ((float) ($$13 >> 16 & 0xFF) / 255.0F);
                    $$15 = (double) ((float) ($$13 >> 8 & 0xFF) / 255.0F);
                    $$16 = (double) ((float) ($$13 & 0xFF) / 255.0F);
                } else if ($$0) {
                    $$14 = 0.0;
                    $$15 = 0.0;
                    $$16 = 0.0;
                } else {
                    $$14 = (0.5 - this.f_19796_.nextDouble()) * 0.15;
                    $$15 = 0.01F;
                    $$16 = (0.5 - this.f_19796_.nextDouble()) * 0.15;
                }
                this.m_9236_().addAlwaysVisibleParticle($$2, $$10, $$11, $$12, $$14, $$15, $$16);
            }
        } else {
            if (this.f_19797_ >= this.waitTime + this.duration) {
                this.m_146870_();
                return;
            }
            boolean $$23 = this.f_19797_ < this.waitTime;
            if ($$0 != $$23) {
                this.setWaiting($$23);
            }
            if ($$23) {
                return;
            }
            if (this.radiusPerTick != 0.0F) {
                $$1 += this.radiusPerTick;
                if ($$1 < 0.5F) {
                    this.m_146870_();
                    return;
                }
                this.setRadius($$1);
            }
            if (this.f_19797_ % 5 == 0) {
                this.victims.entrySet().removeIf(p_287380_ -> this.f_19797_ >= (Integer) p_287380_.getValue());
                List<MobEffectInstance> $$24 = Lists.newArrayList();
                for (MobEffectInstance $$25 : this.potion.getEffects()) {
                    $$24.add(new MobEffectInstance($$25.getEffect(), $$25.mapDuration(p_267926_ -> p_267926_ / 4), $$25.getAmplifier(), $$25.isAmbient(), $$25.isVisible()));
                }
                $$24.addAll(this.effects);
                if ($$24.isEmpty()) {
                    this.victims.clear();
                } else {
                    List<LivingEntity> $$26 = this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_());
                    if (!$$26.isEmpty()) {
                        for (LivingEntity $$27 : $$26) {
                            if (!this.victims.containsKey($$27) && $$27.isAffectedByPotions()) {
                                double $$28 = $$27.m_20185_() - this.m_20185_();
                                double $$29 = $$27.m_20189_() - this.m_20189_();
                                double $$30 = $$28 * $$28 + $$29 * $$29;
                                if ($$30 <= (double) ($$1 * $$1)) {
                                    this.victims.put($$27, this.f_19797_ + this.reapplicationDelay);
                                    for (MobEffectInstance $$31 : $$24) {
                                        if ($$31.getEffect().isInstantenous()) {
                                            $$31.getEffect().applyInstantenousEffect(this, this.getOwner(), $$27, $$31.getAmplifier(), 0.5);
                                        } else {
                                            $$27.addEffect(new MobEffectInstance($$31), this);
                                        }
                                    }
                                    if (this.radiusOnUse != 0.0F) {
                                        $$1 += this.radiusOnUse;
                                        if ($$1 < 0.5F) {
                                            this.m_146870_();
                                            return;
                                        }
                                        this.setRadius($$1);
                                    }
                                    if (this.durationOnUse != 0) {
                                        this.duration = this.duration + this.durationOnUse;
                                        if (this.duration <= 0) {
                                            this.m_146870_();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public void setRadiusOnUse(float float0) {
        this.radiusOnUse = float0;
    }

    public float getRadiusPerTick() {
        return this.radiusPerTick;
    }

    public void setRadiusPerTick(float float0) {
        this.radiusPerTick = float0;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public void setDurationOnUse(int int0) {
        this.durationOnUse = int0;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int int0) {
        this.waitTime = int0;
    }

    public void setOwner(@Nullable LivingEntity livingEntity0) {
        this.owner = livingEntity0;
        this.ownerUUID = livingEntity0 == null ? null : livingEntity0.m_20148_();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            Entity $$0 = ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
            if ($$0 instanceof LivingEntity) {
                this.owner = (LivingEntity) $$0;
            }
        }
        return this.owner;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.f_19797_ = compoundTag0.getInt("Age");
        this.duration = compoundTag0.getInt("Duration");
        this.waitTime = compoundTag0.getInt("WaitTime");
        this.reapplicationDelay = compoundTag0.getInt("ReapplicationDelay");
        this.durationOnUse = compoundTag0.getInt("DurationOnUse");
        this.radiusOnUse = compoundTag0.getFloat("RadiusOnUse");
        this.radiusPerTick = compoundTag0.getFloat("RadiusPerTick");
        this.setRadius(compoundTag0.getFloat("Radius"));
        if (compoundTag0.hasUUID("Owner")) {
            this.ownerUUID = compoundTag0.getUUID("Owner");
        }
        if (compoundTag0.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(compoundTag0.getString("Particle")), BuiltInRegistries.PARTICLE_TYPE.asLookup()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", compoundTag0.getString("Particle"), var5);
            }
        }
        if (compoundTag0.contains("Color", 99)) {
            this.setFixedColor(compoundTag0.getInt("Color"));
        }
        if (compoundTag0.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(compoundTag0));
        }
        if (compoundTag0.contains("Effects", 9)) {
            ListTag $$2 = compoundTag0.getList("Effects", 10);
            this.effects.clear();
            for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
                MobEffectInstance $$4 = MobEffectInstance.load($$2.getCompound($$3));
                if ($$4 != null) {
                    this.addEffect($$4);
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putInt("Age", this.f_19797_);
        compoundTag0.putInt("Duration", this.duration);
        compoundTag0.putInt("WaitTime", this.waitTime);
        compoundTag0.putInt("ReapplicationDelay", this.reapplicationDelay);
        compoundTag0.putInt("DurationOnUse", this.durationOnUse);
        compoundTag0.putFloat("RadiusOnUse", this.radiusOnUse);
        compoundTag0.putFloat("RadiusPerTick", this.radiusPerTick);
        compoundTag0.putFloat("Radius", this.getRadius());
        compoundTag0.putString("Particle", this.getParticle().writeToString());
        if (this.ownerUUID != null) {
            compoundTag0.putUUID("Owner", this.ownerUUID);
        }
        if (this.fixedColor) {
            compoundTag0.putInt("Color", this.getColor());
        }
        if (this.potion != Potions.EMPTY) {
            compoundTag0.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        }
        if (!this.effects.isEmpty()) {
            ListTag $$1 = new ListTag();
            for (MobEffectInstance $$2 : this.effects) {
                $$1.add($$2.save(new CompoundTag()));
            }
            compoundTag0.put("Effects", $$1);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_RADIUS.equals(entityDataAccessor0)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(entityDataAccessor0);
    }

    public Potion getPotion() {
        return this.potion;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }
}