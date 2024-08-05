package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityMobSkull extends Animal implements IBlacklistedFromStatues, IDeadMob {

    private static final EntityDataAccessor<Float> SKULL_DIRECTION = SynchedEntityData.defineId(EntityMobSkull.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> SKULL_ENUM = SynchedEntityData.defineId(EntityMobSkull.class, EntityDataSerializers.INT);

    public EntityMobSkull(EntityType t, Level worldIn) {
        super(t, worldIn);
        this.f_19811_ = true;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null;
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    public boolean isOnWall() {
        return this.m_9236_().m_46859_(this.m_20183_().below());
    }

    public void onUpdate() {
        this.f_20884_ = 0.0F;
        this.f_20886_ = 0.0F;
        this.f_20883_ = 0.0F;
        this.f_20885_ = 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.m_20088_().define(SKULL_DIRECTION, 0.0F);
        this.m_20088_().define(SKULL_ENUM, 0);
    }

    public float getYaw() {
        return this.m_20088_().get(SKULL_DIRECTION);
    }

    public void setYaw(float var1) {
        this.m_20088_().set(SKULL_DIRECTION, var1);
    }

    private int getEnumOrdinal() {
        return this.m_20088_().get(SKULL_ENUM);
    }

    private void setEnumOrdinal(int var1) {
        this.m_20088_().set(SKULL_ENUM, var1);
    }

    public EnumSkullType getSkullType() {
        return EnumSkullType.values()[Mth.clamp(this.getEnumOrdinal(), 0, EnumSkullType.values().length - 1)];
    }

    public void setSkullType(EnumSkullType skullType) {
        this.setEnumOrdinal(skullType.ordinal());
    }

    @Override
    public boolean hurt(@NotNull DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.hurt(var1, var2);
    }

    public void turnIntoItem() {
        if (!this.m_213877_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            ItemStack stack = new ItemStack(this.getSkullType().skull_item.get(), 1);
            if (!this.m_9236_().isClientSide) {
                this.m_5552_(stack, 0.0F);
            }
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (player.m_6144_()) {
            this.setYaw(player.m_146908_());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setYaw(compound.getFloat("SkullYaw"));
        this.setEnumOrdinal(compound.getInt("SkullType"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("SkullYaw", this.getYaw());
        compound.putInt("SkullType", this.getEnumOrdinal());
        super.addAdditionalSaveData(compound);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}