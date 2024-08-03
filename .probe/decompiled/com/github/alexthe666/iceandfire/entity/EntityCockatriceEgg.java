package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityCockatriceEgg extends ThrowableItemProjectile {

    public EntityCockatriceEgg(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ThrowableItemProjectile> type, Level worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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
            if (this.f_19796_.nextInt(4) == 0) {
                int i = 1;
                if (this.f_19796_.nextInt(32) == 0) {
                    i = 4;
                }
                for (int j = 0; j < i; j++) {
                    EntityCockatrice cockatrice = new EntityCockatrice(IafEntityRegistry.COCKATRICE.get(), this.m_9236_());
                    cockatrice.m_146762_(-24000);
                    cockatrice.setHen(this.f_19796_.nextBoolean());
                    cockatrice.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                    if (thrower instanceof Player) {
                        cockatrice.m_21828_((Player) thrower);
                    }
                    this.m_9236_().m_7967_(cockatrice);
                }
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return IafItemRegistry.ROTTEN_EGG.get();
    }
}