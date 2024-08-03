package com.mna.entities.utility;

import com.mna.entities.EntityInit;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class PresentItem extends ItemEntity {

    public PresentItem(EntityType<? extends ItemEntity> entityType, Level world) {
        super(entityType, world);
        this.m_20242_(true);
        this.m_32064_();
    }

    public PresentItem(Level worldIn, double x, double y, double z) {
        this(EntityInit.PRESENTATION_ENTITY.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
        this.m_20334_(this.f_19796_.nextDouble() * 0.2 - 0.1, 0.2, this.f_19796_.nextDouble() * 0.2 - 0.1);
    }

    public PresentItem(Level worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        this.m_32045_(stack);
        this.m_20242_(true);
        this.m_32064_();
        this.m_20334_(0.0, 0.0, 0.0);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}