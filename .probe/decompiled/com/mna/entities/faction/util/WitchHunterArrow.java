package com.mna.entities.faction.util;

import com.mna.entities.EntityInit;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class WitchHunterArrow extends AbstractArrow {

    private int ticksInGround = 0;

    public WitchHunterArrow(EntityType<? extends WitchHunterArrow> type, Level worldIn) {
        super(type, worldIn);
    }

    public WitchHunterArrow(Level worldIn, double x, double y, double z) {
        super(EntityInit.WITCH_HUNTER_ARROW.get(), x, y, z, worldIn);
    }

    public WitchHunterArrow(Level worldIn, LivingEntity shooter) {
        super(EntityInit.WITCH_HUNTER_ARROW.get(), shooter, worldIn);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target.getType() != EntityInit.SPELLBREAKER.get() && target.getType() != EntityInit.WITCH_HUNTER.get() ? super.canHitEntity(target) : false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void tickDespawn() {
        this.ticksInGround++;
        if (this.ticksInGround >= 60) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }
}