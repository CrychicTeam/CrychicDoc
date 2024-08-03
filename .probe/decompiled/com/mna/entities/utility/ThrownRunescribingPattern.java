package com.mna.entities.utility;

import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class ThrownRunescribingPattern extends ThrowableItemProjectile {

    ItemStack defaultStack;

    public ThrownRunescribingPattern(EntityType<? extends ThrownRunescribingPattern> p_i50159_1_, Level p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
        this.defaultStack = new ItemStack(ItemInit.RUNE_PATTERN.get());
    }

    public ThrownRunescribingPattern(Level worldIn, LivingEntity throwerIn) {
        super(EntityInit.THROWN_RUNESCRIBE_PATTERN.get(), throwerIn, worldIn);
    }

    public ThrownRunescribingPattern(Level worldIn, double x, double y, double z) {
        super(EntityInit.THROWN_RUNESCRIBE_PATTERN.get(), x, y, z, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.RUNE_PATTERN.get();
    }

    @OnlyIn(Dist.CLIENT)
    private ParticleOptions makeParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.defaultStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions iparticledata = this.makeParticle();
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(iparticledata, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        this.m_5496_(SoundEvents.GLASS_BREAK, 1.0F, (float) (0.9F + Math.random() * 0.2F));
        if (!this.m_9236_().isClientSide()) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}