package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityAmphithereArrow extends AbstractArrow {

    public EntityAmphithereArrow(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.m_36781_(2.5);
    }

    public EntityAmphithereArrow(EntityType<? extends AbstractArrow> type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(2.5);
    }

    public EntityAmphithereArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.AMPHITHERE_ARROW.get(), world);
    }

    public EntityAmphithereArrow(EntityType type, LivingEntity shooter, Level worldIn) {
        super(type, shooter, worldIn);
        this.m_36781_(2.5);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.f_19797_ == 1 || this.f_19797_ % 70 == 0) && !this.f_36703_ && !this.m_20096_()) {
            this.m_5496_(IafSoundRegistry.AMPHITHERE_GUST, 1.0F, 1.0F);
        }
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d3 = 10.0;
            double xRatio = this.m_20184_().x * (double) this.m_20205_();
            double zRatio = this.m_20184_().z * (double) this.m_20205_();
            this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * 10.0, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * 10.0, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * 10.0, d0, d1, d2);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        living.f_19812_ = true;
        double xRatio = this.m_20184_().x;
        double zRatio = this.m_20184_().z;
        float strength = -1.4F;
        float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        living.m_20256_(living.m_20184_().multiply(0.5, 1.0, 0.5).subtract(xRatio / (double) f * (double) strength, 0.0, zRatio / (double) f * (double) strength).add(0.0, 0.6, 0.0));
        this.spawnExplosionParticle();
    }

    public void spawnExplosionParticle() {
        if (this.m_9236_().isClientSide) {
            for (int height = 0; height < 1 + this.f_19796_.nextInt(2); height++) {
                for (int i = 0; i < 20; i++) {
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d3 = 10.0;
                    double xRatio = this.m_20184_().x * (double) this.m_20205_();
                    double zRatio = this.m_20184_().z * (double) this.m_20205_();
                    this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * d3, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * d3, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * d3, d0, d1, d2);
                }
            }
        } else {
            this.m_9236_().broadcastEntityEvent(this, (byte) 20);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        } else {
            super.m_7822_(id);
        }
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.AMPHITHERE_ARROW.get());
    }
}