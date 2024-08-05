package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityEnderiophageRocket extends FireworkRocketEntity {

    private int phageAge = 0;

    public EntityEnderiophageRocket(EntityType p_i50164_1_, Level p_i50164_2_) {
        super(p_i50164_1_, p_i50164_2_);
    }

    public EntityEnderiophageRocket(Level worldIn, double x, double y, double z, ItemStack givenItem) {
        super(AMEntityRegistry.ENDERIOPHAGE_ROCKET.get(), worldIn);
        this.m_6034_(x, y, z);
        if (!givenItem.isEmpty() && givenItem.hasTag()) {
            this.f_19804_.set(f_37019_, givenItem.copy());
        }
        this.m_20334_(this.f_19796_.nextGaussian() * 0.001, 0.05, this.f_19796_.nextGaussian() * 0.001);
        this.f_37023_ = 18 + this.f_19796_.nextInt(14);
    }

    public EntityEnderiophageRocket(Level p_i231581_1_, @Nullable Entity p_i231581_2_, double p_i231581_3_, double p_i231581_5_, double p_i231581_7_, ItemStack p_i231581_9_) {
        this(p_i231581_1_, p_i231581_3_, p_i231581_5_, p_i231581_7_, p_i231581_9_);
        this.m_5602_(p_i231581_2_);
    }

    public EntityEnderiophageRocket(Level p_i47367_1_, ItemStack p_i47367_2_, LivingEntity p_i47367_3_) {
        this(p_i47367_1_, p_i47367_3_, p_i47367_3_.m_20185_(), p_i47367_3_.m_20186_(), p_i47367_3_.m_20189_(), p_i47367_2_);
        this.f_19804_.set(f_37020_, OptionalInt.of(p_i47367_3_.m_19879_()));
    }

    public EntityEnderiophageRocket(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.ENDERIOPHAGE_ROCKET.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.phageAge++;
        if (this.m_9236_().isClientSide) {
            this.m_9236_().addParticle(ParticleTypes.END_ROD, this.m_20185_(), this.m_20186_() - 0.3, this.m_20189_(), this.f_19796_.nextGaussian() * 0.05, -this.m_20184_().y * 0.5, this.f_19796_.nextGaussian() * 0.05);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 17) {
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.05, 0.005, this.f_19796_.nextGaussian() * 0.05);
            for (int i = 0; i < this.f_19796_.nextInt(15) + 30; i++) {
                this.m_9236_().addParticle(AMParticleRegistry.DNA.get(), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.25, this.f_19796_.nextGaussian() * 0.25, this.f_19796_.nextGaussian() * 0.25);
            }
            for (int i = 0; i < this.f_19796_.nextInt(15) + 15; i++) {
                this.m_9236_().addParticle(ParticleTypes.END_ROD, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.15, this.f_19796_.nextGaussian() * 0.15, this.f_19796_.nextGaussian() * 0.15);
            }
            SoundEvent soundEvent = AlexsMobs.PROXY.isFarFromCamera(this.m_20185_(), this.m_20186_(), this.m_20189_()) ? SoundEvents.FIREWORK_ROCKET_BLAST : SoundEvents.FIREWORK_ROCKET_BLAST_FAR;
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), soundEvent, SoundSource.AMBIENT, 20.0F, 0.95F + this.f_19796_.nextFloat() * 0.1F, true);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getItem() {
        return new ItemStack(AMItemRegistry.ENDERIOPHAGE_ROCKET.get());
    }
}