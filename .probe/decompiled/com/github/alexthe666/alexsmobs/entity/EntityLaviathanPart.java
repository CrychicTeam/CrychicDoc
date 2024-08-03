package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class EntityLaviathanPart extends PartEntity<EntityLaviathan> {

    private final EntityDimensions size;

    public float scale = 1.0F;

    public EntityLaviathanPart(EntityLaviathan parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.m_6210_();
    }

    public EntityLaviathanPart(EntityLaviathan entityCachalotWhale, float sizeX, float sizeY, EntityDimensions size) {
        super(entityCachalotWhale);
        this.size = size;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) this.m_20192_() * 0.15F, (double) (this.m_20205_() * 0.1F));
    }

    protected void collideWithNearbyEntities() {
    }

    public InteractionResult getEntityInteractionResult(Player player, InteractionHand hand) {
        return this.getParent() == null ? InteractionResult.PASS : this.getParent().mobInteract(player, hand);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    protected void collideWithEntity(Entity entityIn) {
        entityIn.push(this);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !this.m_6673_(source) && this.getParent().attackEntityPartFrom(this, source, amount);
    }

    @Override
    public boolean is(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.size == null ? EntityDimensions.scalable(0.0F, 0.0F) : this.size.scale(this.scale);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.m_8119_();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }
}