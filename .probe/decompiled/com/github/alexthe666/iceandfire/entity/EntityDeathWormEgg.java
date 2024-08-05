package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityDeathWormEgg extends ThrowableItemProjectile implements IEntityAdditionalSpawnData {

    private boolean giant;

    public EntityDeathWormEgg(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDeathWormEgg(EntityType<? extends ThrowableItemProjectile> type, LivingEntity throwerIn, Level worldIn, boolean giant) {
        super(type, throwerIn, worldIn);
        this.giant = giant;
    }

    public EntityDeathWormEgg(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level worldIn, boolean giant) {
        super(type, x, y, z, worldIn);
        this.giant = giant;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.giant);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.giant = additionalData.readBoolean();
    }

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
        Entity thrower = this.m_19749_();
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().hurt(this.m_9236_().damageSources().thrown(this, thrower), 0.0F);
        }
        if (!this.m_9236_().isClientSide) {
            float wormSize = 0.25F + (float) (Math.random() * 0.35F);
            EntityDeathWorm deathworm = new EntityDeathWorm(IafEntityRegistry.DEATH_WORM.get(), this.m_9236_());
            deathworm.setVariant(this.f_19796_.nextInt(3));
            deathworm.m_7105_(true);
            deathworm.setWormHome(this.m_20183_());
            deathworm.setWormAge(1);
            deathworm.setDeathWormScale(this.giant ? wormSize * 4.0F : wormSize);
            deathworm.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
            if (thrower instanceof Player) {
                deathworm.m_21816_(thrower.getUUID());
            }
            this.m_9236_().m_7967_(deathworm);
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return this.giant ? IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get() : IafItemRegistry.DEATHWORM_EGG.get();
    }
}