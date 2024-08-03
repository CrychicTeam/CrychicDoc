package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class GuanoEntity extends ThrowableItemProjectile {

    public GuanoEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public GuanoEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.GUANO.get(), level);
    }

    public GuanoEntity(Level level, LivingEntity thrower) {
        super(ACEntityRegistry.GUANO.get(), thrower, level);
    }

    public GuanoEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.GUANO.get(), x, y, z, level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleEntityEvent(byte message) {
        if (message == 3) {
            double d0 = 0.08;
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.m_5790_(hitResult);
        hitResult.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.m_6532_(hitResult);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ACItemRegistry.GUANO.get();
    }
}