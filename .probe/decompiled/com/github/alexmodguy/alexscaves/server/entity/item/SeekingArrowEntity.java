package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SeekingArrowEntity extends AbstractArrow {

    private static final EntityDataAccessor<Integer> ARC_TOWARDS_ENTITY_ID = SynchedEntityData.defineId(SeekingArrowEntity.class, EntityDataSerializers.INT);

    private boolean stopSeeking;

    public SeekingArrowEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public SeekingArrowEntity(Level level, LivingEntity shooter) {
        super(ACEntityRegistry.SEEKING_ARROW.get(), shooter, level);
    }

    public SeekingArrowEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.SEEKING_ARROW.get(), x, y, z, level);
    }

    public SeekingArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.SEEKING_ARROW.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ARC_TOWARDS_ENTITY_ID, -1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        int id = this.getArcTowardsID();
        if (!this.f_36703_ && !this.stopSeeking) {
            if (id == -1) {
                if (!this.m_9236_().isClientSide) {
                    Entity closest = null;
                    Entity owner = this.m_19749_();
                    float boxExpandBy = (float) Math.min(10, 3 + this.f_19797_ / 4);
                    for (Entity entity : this.m_9236_().getEntities(this, this.m_20191_().inflate((double) boxExpandBy), x$0 -> this.m_5603_(x$0))) {
                        if ((closest == null || entity.distanceTo(this) < closest.distanceTo(this)) && !this.m_150171_(entity) && (owner == null || !entity.isAlliedTo(owner))) {
                            closest = entity;
                        }
                    }
                    if (closest != null) {
                        this.m_5496_(ACSoundRegistry.SEEKING_ARROW_LOCKON.get(), 5.0F, 1.0F);
                        this.setArcTowardsID(closest.getId());
                    }
                }
            } else {
                Entity arcTowards = this.m_9236_().getEntity(id);
                if (arcTowards != null) {
                    Vec3 arcVec = arcTowards.position().add(0.0, (double) (0.65F * arcTowards.getBbHeight()), 0.0).subtract(this.m_20182_());
                    if (arcVec.length() > (double) arcTowards.getBbWidth()) {
                        this.m_20256_(this.m_20184_().scale(0.3F).add(arcVec.normalize().scale(0.7F)));
                    }
                }
            }
        }
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            Vec3 center = this.m_20182_().add(this.m_20184_());
            Vec3 vec3 = center.add(new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)));
            this.m_9236_().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), center.x, center.y, center.z, vec3.x, vec3.y, vec3.z);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        this.stopSeeking = true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ACItemRegistry.SEEKING_ARROW.get());
    }

    private int getArcTowardsID() {
        return this.f_19804_.get(ARC_TOWARDS_ENTITY_ID);
    }

    private void setArcTowardsID(int id) {
        this.f_19804_.set(ARC_TOWARDS_ENTITY_ID, id);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ACSoundRegistry.SEEKING_ARROW_HIT.get();
    }
}