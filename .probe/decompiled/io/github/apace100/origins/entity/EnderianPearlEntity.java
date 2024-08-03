package io.github.apace100.origins.entity;

import io.github.apace100.origins.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EnderianPearlEntity extends ThrownEnderpearl {

    public EnderianPearlEntity(EntityType<? extends EnderianPearlEntity> entityType, Level world) {
        super(entityType, world);
    }

    public EnderianPearlEntity(EntityType<? extends EnderianPearlEntity> entityType, LivingEntity owner, Level world) {
        super(entityType, world);
        this.m_6034_(owner.m_20185_(), owner.m_20188_() - 0.1, owner.m_20189_());
        this.m_5602_(owner);
    }

    public EnderianPearlEntity(Level world, LivingEntity owner) {
        this(ModEntities.ENDERIAN_PEARL.get(), owner, world);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }

    @Override
    protected void onHit(HitResult result) {
        HitResult.Type type = result.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
        } else if (type == HitResult.Type.BLOCK) {
            this.m_8060_((BlockHitResult) result);
        }
        if (type != HitResult.Type.MISS) {
            this.m_146852_(GameEvent.PROJECTILE_LAND, this.m_19749_());
        }
        for (int i = 0; i < 32; i++) {
            this.m_9236_().addParticle(ParticleTypes.PORTAL, this.m_20185_(), this.m_20186_() + this.f_19796_.nextDouble() * 2.0, this.m_20189_(), this.f_19796_.nextGaussian(), 0.0, this.f_19796_.nextGaussian());
        }
        if (!this.m_9236_().isClientSide && !this.m_213877_()) {
            Entity entity = this.m_19749_();
            if (entity instanceof ServerPlayer serverplayer) {
                if (serverplayer.connection.isAcceptingMessages() && serverplayer.m_9236_() == this.m_9236_() && !serverplayer.m_5803_()) {
                    EntityTeleportEvent.EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverplayer, this.m_20185_(), this.m_20186_(), this.m_20189_(), this, 0.0F, result);
                    if (!event.isCanceled()) {
                        if (entity.isPassenger()) {
                            serverplayer.dismountTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                        } else {
                            entity.teleportTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                        }
                        entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entity.fallDistance = 0.0F;
                        if (event.getAttackDamage() > 0.0F) {
                            entity.hurt(entity.damageSources().fall(), event.getAttackDamage());
                        }
                    }
                }
            } else if (entity != null) {
                entity.teleportTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                entity.fallDistance = 0.0F;
            }
            this.m_146870_();
        }
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}