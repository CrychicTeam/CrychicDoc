package net.minecraft.world.entity.boss;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.ItemStack;

public class EnderDragonPart extends Entity {

    public final EnderDragon parentMob;

    public final String name;

    private final EntityDimensions size;

    public EnderDragonPart(EnderDragon enderDragon0, String string1, float float2, float float3) {
        super(enderDragon0.m_6095_(), enderDragon0.m_9236_());
        this.size = EntityDimensions.scalable(float2, float3);
        this.m_6210_();
        this.parentMob = enderDragon0;
        this.name = string1;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.parentMob.m_142340_();
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return this.m_6673_(damageSource0) ? false : this.parentMob.hurt(this, damageSource0, float1);
    }

    @Override
    public boolean is(Entity entity0) {
        return this == entity0 || this.parentMob == entity0;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}