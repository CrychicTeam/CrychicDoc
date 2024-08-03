package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class InkBombEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Boolean> GLOWING_BOMB = SynchedEntityData.defineId(InkBombEntity.class, EntityDataSerializers.BOOLEAN);

    public InkBombEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public InkBombEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.INK_BOMB.get(), level);
    }

    public InkBombEntity(Level level, LivingEntity thrower) {
        super(ACEntityRegistry.INK_BOMB.get(), thrower, level);
    }

    public InkBombEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.INK_BOMB.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(GLOWING_BOMB, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGlowingBomb(tag.getBoolean("GlowingBomb"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("GlowingBomb", this.isGlowingBomb());
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
        if (hitResult.getEntity() instanceof SubmarineEntity submarine) {
            submarine.setLightsOn(false);
            if (submarine.m_146895_() instanceof Player player) {
                player.m_7292_(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                if (this.isGlowingBomb()) {
                    player.m_7292_(new MobEffectInstance(MobEffects.GLOWING, 300));
                }
                if (!player.isCreative()) {
                    player.m_21195_(MobEffects.NIGHT_VISION);
                    player.m_21195_(MobEffects.CONDUIT_POWER);
                }
            }
        }
        if (hitResult.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            if (!(living instanceof Player player) || !player.isCreative()) {
                living.removeEffect(MobEffects.NIGHT_VISION);
                living.removeEffect(MobEffects.CONDUIT_POWER);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.m_6532_(hitResult);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_() + 0.2F, this.m_20189_());
            areaeffectcloud.setParticle(this.isGlowingBomb() ? ParticleTypes.GLOW_SQUID_INK : ParticleTypes.SQUID_INK);
            areaeffectcloud.setFixedColor(0);
            areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            if (this.isGlowingBomb()) {
                areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300));
            }
            areaeffectcloud.setRadius(2.0F);
            areaeffectcloud.setDuration(60);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
            this.m_9236_().m_7967_(areaeffectcloud);
        }
    }

    public boolean isGlowingBomb() {
        return this.f_19804_.get(GLOWING_BOMB);
    }

    public void setGlowingBomb(boolean bool) {
        this.f_19804_.set(GLOWING_BOMB, bool);
    }

    @Override
    protected Item getDefaultItem() {
        return this.isGlowingBomb() ? ACItemRegistry.GLOW_INK_BOMB.get() : ACItemRegistry.INK_BOMB.get();
    }
}