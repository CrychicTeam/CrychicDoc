package com.mna.entities;

import com.mna.ManaAndArtifice;
import com.mna.entities.state.EntityStateMachine;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class LivingUtilityEntity extends LivingEntity {

    protected static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<Byte> STATE = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<String> CURRENT_ANIMATION = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<String> CURRENT_SEQUENCE = SynchedEntityData.defineId(LivingUtilityEntity.class, EntityDataSerializers.STRING);

    protected int stateTicks = 0;

    protected float lastTickAnimationPct;

    protected int lastAnimationChangeTimer = 0;

    protected float animationPct;

    protected EntityStateMachine<LivingUtilityEntity> stateMachine = new EntityStateMachine<>(this, CURRENT_SEQUENCE);

    protected Player caster;

    protected LivingUtilityEntity(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
        this.m_20242_(true);
        this.m_20225_(false);
        this.m_20331_(true);
        this.f_19794_ = true;
    }

    @Override
    public void tick() {
        super.tick();
        this.stateTicks++;
        this.lastAnimationChangeTimer++;
        this.lastTickAnimationPct = this.animationPct;
        if (this.caster == null) {
            this.caster = this.getCaster();
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstanceIn) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CASTER_UUID, "");
        this.f_19804_.define(STATE, (byte) 0);
        this.f_19804_.define(CURRENT_ANIMATION, "neutral");
        this.f_19804_.define(CURRENT_SEQUENCE, "");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("caster", this.f_19804_.get(CASTER_UUID));
        compound.putByte("state", this.getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("caster")) {
            this.f_19804_.set(CASTER_UUID, compound.getString("caster"));
        }
        if (compound.contains("state")) {
            this.f_19804_.set(STATE, compound.getByte("state"));
        }
    }

    @Nullable
    protected Player getCaster() {
        UUID caster = this.getCasterUUID();
        return caster != null ? this.m_9236_().m_46003_(caster) : null;
    }

    public UUID getCasterUUID() {
        try {
            return UUID.fromString(this.f_19804_.get(CASTER_UUID));
        } catch (Exception var5) {
            return null;
        } finally {
            ;
        }
    }

    public void setCasterUUID(UUID casterUUID) {
        if (casterUUID != null) {
            this.f_19804_.set(CASTER_UUID, casterUUID.toString());
        } else {
            ManaAndArtifice.LOGGER.error("Received null UUID for ritual caster.  Some effects may not apply!");
        }
    }

    protected void setState(byte state) {
        this.f_19804_.set(STATE, state);
        this.stateTicks = 0;
    }

    public byte getState() {
        return this.f_19804_.get(STATE);
    }

    public float getAnimationPct(float partialTicks) {
        return MathUtils.lerpf(this.lastTickAnimationPct, this.animationPct, partialTicks);
    }

    public void setCurrentAnimation(String anim) {
        this.f_19804_.set(CURRENT_ANIMATION, anim);
        this.lastAnimationChangeTimer = 0;
        this.lastTickAnimationPct = 0.0F;
        this.animationPct = 0.0F;
    }

    public String getCurrentAnimation() {
        return this.f_19804_.get(CURRENT_ANIMATION);
    }

    public int getLastAnimChangeTimer() {
        return this.lastAnimationChangeTimer;
    }

    protected void enableFlightFor(Player player) {
        if (player != null) {
            player.m_20242_(true);
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
        }
    }

    protected void disableFlightFor(Player player) {
        if (player != null) {
            player.m_20242_(false);
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
        }
    }
}