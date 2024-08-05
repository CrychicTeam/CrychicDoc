package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityCockroachEgg extends ThrowableItemProjectile {

    public EntityCockroachEgg(EntityType p_i50154_1_, Level p_i50154_2_) {
        super(p_i50154_1_, p_i50154_2_);
    }

    public EntityCockroachEgg(Level worldIn, LivingEntity throwerIn) {
        super(AMEntityRegistry.COCKROACH_EGG.get(), throwerIn, worldIn);
    }

    public EntityCockroachEgg(Level worldIn, double x, double y, double z) {
        super(AMEntityRegistry.COCKROACH_EGG.get(), x, y, z, worldIn);
    }

    public EntityCockroachEgg(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.COCKROACH_EGG.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            int i = this.f_19796_.nextInt(3);
            for (int j = 0; j < i; j++) {
                EntityCockroach croc = AMEntityRegistry.COCKROACH.get().create(this.m_9236_());
                croc.m_146762_(-24000);
                croc.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                croc.m_6518_((ServerLevel) this.m_9236_(), this.m_9236_().getCurrentDifficultyAt(this.m_20183_()), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                croc.m_21446_(this.m_20183_(), 20);
                this.m_9236_().m_7967_(croc);
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return AMItemRegistry.COCKROACH_OOTHECA.get();
    }
}