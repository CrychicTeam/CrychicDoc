package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntitySeaSerpentArrow extends AbstractArrow {

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrow> t, Level worldIn) {
        super(t, worldIn);
        this.m_36781_(3.0);
    }

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrow> t, Level worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(3.0);
    }

    public EntitySeaSerpentArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.SEA_SERPENT_ARROW.get(), world);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntitySeaSerpentArrow(EntityType t, Level worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.m_36781_(3.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d3 = 10.0;
            double xRatio = this.m_20184_().x * (double) this.m_20206_();
            double zRatio = this.m_20184_().z * (double) this.m_20206_();
            this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * 10.0, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * 10.0, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * 10.0, d0, d1, d2);
            this.m_9236_().addParticle(ParticleTypes.SPLASH, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * 10.0, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * 10.0, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * 10.0, d0, d1, d2);
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.SEA_SERPENT_ARROW.get());
    }
}